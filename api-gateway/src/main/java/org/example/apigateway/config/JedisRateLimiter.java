package org.example.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.model.Config;
import org.example.apigateway.model.RateLimitSettings;
import org.example.apigateway.model.RedisSettings;
import org.example.apigateway.utils.CryptoUtils;
import org.example.apigateway.utils.TokenBucketLuaScript;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import redis.clients.jedis.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class JedisRateLimiter implements RateLimiter<Config> {

    private final JedisPool pool;
    private final Map<String, Config> configCache = new ConcurrentHashMap<>();
    private final RateLimitSettings rateLimitSettings;

    public JedisRateLimiter(CryptoUtils cryptoUtils, RedisSettings redisSettings, RateLimitSettings rateLimitSettings) throws Exception {
        var clientCfg = DefaultJedisClientConfig.builder()
                    .user(redisSettings.redisUser())
                    .password(cryptoUtils.decrypt(redisSettings.redisPassword()))
                .timeoutMillis(5_000)
                .ssl(false)
                .build();
        this.rateLimitSettings = rateLimitSettings;

        this.pool = new JedisPool(
                new HostAndPort(redisSettings.redisHost(), redisSettings.redisPort()),
                clientCfg);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String key) {
        Assert.hasText(key, "A non-empty key is required");

        Config cfg = configCache.computeIfAbsent(
                routeId, id -> new Config(rateLimitSettings.replenishRate(), rateLimitSettings.burstCapacity(), Duration.ofSeconds(rateLimitSettings.duration())));

        return Mono.fromCallable(() -> executeLua(routeId, key, cfg))
                .doOnNext(r -> log.debug(
                        "[RateLimiter] route='{}' client='{}' allowed={} remainingTokens={}",
                        routeId, key, r.isAllowed(), r.getHeaders().get("X-RateLimit-Remaining")));
    }

    @Override public Map<String, Config> getConfig()          { return configCache; }
    @Override public Class<Config> getConfigClass()           { return Config.class; }
    @Override public Config newConfig()                       { return new Config(rateLimitSettings.replenishRate(), rateLimitSettings.burstCapacity(), Duration.ofSeconds(rateLimitSettings.duration())); }


    private Response executeLua(String routeId, String clientId, Config cfg) {

        String key = "rl:" + routeId + ':' + clientId;

        List<String> keys = List.of(key);
        List<String> argv = List.of(
                String.valueOf(cfg.replenishRate()),
                String.valueOf(cfg.burstCapacity()),
                String.valueOf(System.currentTimeMillis())
        );

        try (Jedis jedis = pool.getResource()) {
            @SuppressWarnings("unchecked")
            List<Long> r = (List<Long>) jedis.eval(TokenBucketLuaScript.SCRIPT, keys, argv);

            boolean allowed   = r.get(0) == 1;
            long    remaining = r.get(1);

            return new Response(allowed,
                    Map.of("X-RateLimit-Remaining", String.valueOf(remaining)));
        }
    }


}
