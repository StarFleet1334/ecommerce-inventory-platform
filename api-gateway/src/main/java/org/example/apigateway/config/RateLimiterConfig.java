package org.example.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    @Primary
    public JedisRateLimiter customRateLimiter() {
        return new JedisRateLimiter();
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange ->
                Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                        .map(addr -> addr.getAddress().getHostAddress())
                        .defaultIfEmpty("unknown");
    }
}
