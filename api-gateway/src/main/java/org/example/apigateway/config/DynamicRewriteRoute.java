package org.example.apigateway.config;

import org.example.apigateway.utils.HttpRequestTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.function.Function;

@Configuration
public class DynamicRewriteRoute {

    @Value("${rewrite.backend.uri.secondary}")
    private String secondaryBackendUri;

    @Value("${rewrite.backend.uri.primary}")
    private String primaryBackendUri;

    @Value("${rewrite.backend.name.secondary}")
    private String secondBackendName;

    @Value("${rewrite.backend.name.primary}")
    private String primaryBackendName;

    @Autowired
    private KeyResolver ipKeyResolver;

    @Autowired
    private RateLimiter customRateLimiter;

    private final String streamURIPrefix;

    public DynamicRewriteRoute() {
        streamURIPrefix = "/api/v1/";
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(generateIdentifier("customer", secondBackendName), createRewriteRoute("customer", generateInboundPath("customer"), secondaryBackendUri, HttpRequestTypes.GET.getName()))
                .route(generateIdentifier("employee", secondBackendName), createRewriteRoute("employee", generateInboundPath("employee"), secondaryBackendUri, HttpRequestTypes.GET.getName()))
                .route(generateIdentifier("warehouse", secondBackendName), createRewriteRoute("warehouse", generateInboundPath("warehouse"), secondaryBackendUri, HttpRequestTypes.GET.getName()))
                .route(generateIdentifier("supplier", secondBackendName), createRewriteRoute("supplier", generateInboundPath("supplier"), secondaryBackendUri, HttpRequestTypes.GET.getName()))
                .route(generateIdentifier("product", secondBackendName), createRewriteRoute("product", generateInboundPath("product"), secondaryBackendUri, HttpRequestTypes.GET.getName()))
                .route(generateIdentifier("supply", primaryBackendName), createRewriteRoute("supply", generateInboundPath("supply"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("supply", primaryBackendName), createRewriteRoute("supply", generateInboundPath("supply"), primaryBackendUri, HttpRequestTypes.DELETE.getName()))
                .route(generateIdentifier("stock", primaryBackendName), createRewriteRoute("stock", generateInboundPath("stock"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("stock", primaryBackendName), createRewriteRoute("stock", generateInboundPath("stock"), primaryBackendUri, HttpRequestTypes.DELETE.getName()))
                .route(generateIdentifier("customer", primaryBackendName), createRewriteRoute("customer", generateInboundPath("customer"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("customer", primaryBackendName), createRewriteRoute("customer", generateInboundPath("customer"), primaryBackendUri, HttpRequestTypes.DELETE.getName()))
                .route(generateIdentifier("customer", primaryBackendName), createRewriteRoute("customer/order", generateInboundPath("customer-order"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("warehouse", primaryBackendName), createRewriteRoute("warehouse", generateInboundPath("warehouse"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("warehouse", primaryBackendName), createRewriteRoute("warehouse", generateInboundPath("warehouse"), primaryBackendUri, HttpRequestTypes.DELETE.getName()))
                .route(generateIdentifier("supplier", primaryBackendName), createRewriteRoute("supplier", generateInboundPath("supplier"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("supplier", primaryBackendName), createRewriteRoute("supplier", generateInboundPath("supplier"), primaryBackendUri, HttpRequestTypes.DELETE.getName()))
                .route(generateIdentifier("product", primaryBackendName), createRewriteRoute("product", generateInboundPath("product"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("product", primaryBackendName), createRewriteRoute("product", generateInboundPath("product"), primaryBackendUri, HttpRequestTypes.DELETE.getName()))
                .route(generateIdentifier("employee", primaryBackendName), createRewriteRoute("employee", generateInboundPath("employee"), primaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("employee", primaryBackendName), createRewriteRoute("employee", generateInboundPath("employee"), primaryBackendUri, HttpRequestTypes.DELETE.getName()))
                .route(generateIdentifier("customer", primaryBackendName), createRewriteRoute("customer/order/speedUp", generateInboundPath("customer-order-speedUp"), secondaryBackendUri, HttpRequestTypes.POST.getName()))
                .route(generateIdentifier("employee", primaryBackendName), createRewriteRoute("employee/supply/speedUp", generateInboundPath("employee-supply-speedUp"), secondaryBackendUri, HttpRequestTypes.POST.getName()))
                .build();
    }

    private Function<PredicateSpec, Buildable<Route>> createRewriteRoute(
            String resource, String inbound, String backendUri, String httpRequestMethod) {

        String backend = streamURIPrefix + resource;

        return r -> r
                .method(HttpMethod.valueOf(httpRequestMethod))
                .and()
                .path(inbound, inbound + "/**")
                .filters(f -> f.rewritePath(inbound + "(?<segment>/?.*)", backend + "${segment}")
                        .requestRateLimiter(cfg -> cfg
                                .setRateLimiter(customRateLimiter)
                                .setKeyResolver(ipKeyResolver)))
                .uri(backendUri);
    }

    private String generateIdentifier(String resource, String backendName) {
        return String.format("%s-%s", backendName, resource);
    }

    private String generateInboundPath(String resource) {
        return "/" + resource;
    }

}
