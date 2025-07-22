package org.example.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Function;

@Configuration
public class DynamicRewriteRoute {

    @Value("${rewrite.backend.uri.secondary}")
    private String secondaryBackendUri;

    @Value("${rewrite.backend.name.secondary}")
    private String secondBackendName;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(generateIdentifier("customer"),createRewriteRoute("customer"))
                .route(generateIdentifier("employee"),createRewriteRoute("employee"))
                .route(generateIdentifier("warehouse"),createRewriteRoute("warehouse"))
                .route(generateIdentifier("supplier"),createRewriteRoute("supplier"))
                .route(generateIdentifier("product"),createRewriteRoute("product"))
                .build();
    }

    private Function<PredicateSpec, Buildable<Route>> createRewriteRoute(
            String resource) {

        String inbound = "/get-" + resource;
        String backend = "/api/v1/" + resource;

        return r -> r
                .path(inbound, inbound + "/**")
                .filters(f -> f.rewritePath(inbound + "(?<segment>/?.*)",
                        backend + "${segment}"))
                .uri(secondaryBackendUri);
    }

    private String generateIdentifier(String resource) {
        return String.format("%s-get-%s", secondBackendName, resource);
    }

}
