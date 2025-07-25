package org.example.apigateway.config;

import org.example.apigateway.model.RateLimitSettings;
import org.example.apigateway.model.RedisSettings;
import org.example.apigateway.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Value("${redis.password}")
    private String redisPassword;

    @Value("${redis.host}")
    private String redistHost;

    @Value("${redis.port}")
    private String redisPort;

    @Value("${redis.user}")
    private String redisUser;

    @Value("${rt.replenish.rate}")
    private String replenishRate;

    @Value("${rt.burst.capacity}")
    private String burstCapacity;

    @Value("${rt.rate.duration}")
    private String duration;

    @Bean
    public JedisRateLimiter customRateLimiter(CryptoUtils cryptoUtils) {
        RateLimitSettings rateLimitSettings = new RateLimitSettings(Integer.parseInt(replenishRate), Integer.parseInt(burstCapacity), Integer.parseInt(duration));
        RedisSettings redisSettings = new RedisSettings(redistHost,Integer.parseInt(redisPort),redisUser,redisPassword);
        try {
            return new JedisRateLimiter(cryptoUtils,redisSettings,rateLimitSettings);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange ->
                Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                        .map(addr -> addr.getAddress().getHostAddress())
                        .defaultIfEmpty("unknown");
    }
}
