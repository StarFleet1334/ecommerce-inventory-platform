package com.example.rocketmqservice.watcher;

import com.example.rocketmqservice.initializer.RocketMQInitializer;
import com.example.rocketmqservice.service.TopicConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TopicConfigFileWatcher implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(TopicConfigFileWatcher.class);

    private final RocketMQInitializer initializer;
    private final TopicConfigService topicConfigService;

    private WatchService watchService;
    private final ExecutorService        watchPool = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService poller  = Executors.newSingleThreadScheduledExecutor();

    private volatile boolean running = true;
    private volatile FileTime lastModified;

    public TopicConfigFileWatcher(RocketMQInitializer initializer,
                                  TopicConfigService topicConfigService) {
        this.initializer       = initializer;
        this.topicConfigService = topicConfigService;
    }

    // ─────────────────────────────────── lifecycle ────────────────────────────────────

    @PostConstruct
    public void startWatching() throws IOException {
        Path cfg    = topicConfigService.getConfigPath();
        Path cfgDir = cfg.getParent();

        // 1) Inotify – only works when events are propagated
        watchService = FileSystems.getDefault().newWatchService();
        cfgDir.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        watchPool.submit(() -> watchLoop(cfg.getFileName()));
        log.info("Watching {} for changes (inotify + polling)", cfg.toAbsolutePath());

        // 2) Poll‑fallback – catches changes when inotify doesn’t fire (Docker Desktop)
        lastModified = Files.getLastModifiedTime(cfg);
        poller.scheduleAtFixedRate(this::pollFile, 5, 5, TimeUnit.SECONDS);
    }

    // ─────────────────────────────────── inotify branch ───────────────────────────────

    private void watchLoop(Path fileName) {
        while (running) {
            try {
                WatchKey key = watchService.take(); // block
                for (WatchEvent<?> ev : key.pollEvents()) {
                    Path changed = (Path) ev.context();
                    if (!changed.equals(fileName)) continue;

                    if (ev.kind() != StandardWatchEventKinds.ENTRY_DELETE) {
                        triggerReload("inotify:" + ev.kind().name());
                    }
                }
                if (!key.reset()) break; // directory became inaccessible
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ClosedWatchServiceException cwse) {
                break; // normal shutdown
            } catch (Exception ex) {
                log.error("Watcher loop failed", ex);
            }
        }
    }

    // ─────────────────────────────────── polling branch ───────────────────────────────

    private void pollFile() {
        try {
            Path cfg = topicConfigService.getConfigPath();
            FileTime lm = Files.getLastModifiedTime(cfg);
            if (lm.compareTo(lastModified) > 0) {
                lastModified = lm;
                triggerReload("polling");
            }
        } catch (Exception ex) {
            log.warn("Polling failed: {}", ex.toString());
        }
    }

    // ─────────────────────────────────── utilities ────────────────────────────────────

    private void triggerReload(String source) {
        try {
            log.info("topics.json changed ({}), reloading …", source);
            initializer.refreshTopics();
        } catch (Exception ex) {
            log.error("Reload failed", ex);
        }
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        running = false;
        if (watchService != null) watchService.close();
        watchPool.shutdownNow();
        poller.shutdownNow();
        log.info("Stopped file watcher");
    }
}
