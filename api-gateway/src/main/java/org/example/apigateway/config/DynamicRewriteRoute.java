package org.example.apigateway.config;

import org.example.apigateway.utils.HttpRequestTypes;
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

    @Value("${rewrite.backend.uri.primary}")
    private String primaryBackendUri;

    @Value("${rewrite.backend.name.secondary}")
    private String secondBackendName;

    @Value("${rewrite.backend.name.primary}")
    private String primaryBackendName;

    private final String streamURIPrefix;

    public DynamicRewriteRoute() {
        streamURIPrefix = "/api/v1/";
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(generateIdentifier("customer",secondBackendName),createRewriteRoute("customer",generateInboundPath(HttpRequestTypes.GET.getName(),"customer"),secondaryBackendUri))
                .route(generateIdentifier("employee",secondBackendName),createRewriteRoute("employee",generateInboundPath(HttpRequestTypes.GET.getName(),"employee"),secondaryBackendUri))
                .route(generateIdentifier("warehouse",secondBackendName),createRewriteRoute("warehouse",generateInboundPath(HttpRequestTypes.GET.getName(),"warehouse"),secondaryBackendUri))
                .route(generateIdentifier("supplier",secondBackendName),createRewriteRoute("supplier",generateInboundPath(HttpRequestTypes.GET.getName(),"supplier"),secondaryBackendUri))
                .route(generateIdentifier("product",secondBackendName),createRewriteRoute("product",generateInboundPath(HttpRequestTypes.GET.getName(),"product"),secondaryBackendUri))
                .route(generateIdentifier("supply",primaryBackendName),createRewriteRoute("supply",generateInboundPath(HttpRequestTypes.POST.getName(),"supply"),primaryBackendUri))
                .route(generateIdentifier("supply",primaryBackendName),createRewriteRoute("supply",generateInboundPath(HttpRequestTypes.DELETE.getName(),"supply"),primaryBackendUri))
                .route(generateIdentifier("stock",primaryBackendName),createRewriteRoute("stock",generateInboundPath(HttpRequestTypes.POST.getName(),"stock"),primaryBackendUri))
                .route(generateIdentifier("stock",primaryBackendName),createRewriteRoute("stock",generateInboundPath(HttpRequestTypes.DELETE.getName(),"stock"),primaryBackendUri))
                .route(generateIdentifier("customer",primaryBackendName),createRewriteRoute("customer",generateInboundPath(HttpRequestTypes.POST.getName(),"customer"),primaryBackendUri))
                .route(generateIdentifier("customer",primaryBackendName),createRewriteRoute("customer",generateInboundPath(HttpRequestTypes.DELETE.getName(),"customer"),primaryBackendUri))
                .route(generateIdentifier("customer",primaryBackendName),createRewriteRoute("customer/order",generateInboundPath(HttpRequestTypes.POST.getName(),"customer-order"),primaryBackendUri))
                .route(generateIdentifier("warehouse",primaryBackendName),createRewriteRoute("warehouse",generateInboundPath(HttpRequestTypes.POST.getName(),"warehouse"),primaryBackendUri))
                .route(generateIdentifier("warehouse",primaryBackendName),createRewriteRoute("warehouse",generateInboundPath(HttpRequestTypes.DELETE.getName(),"warehouse"),primaryBackendUri))
                .route(generateIdentifier("supplier",primaryBackendName),createRewriteRoute("supplier",generateInboundPath(HttpRequestTypes.POST.getName(),"supplier"),primaryBackendUri))
                .route(generateIdentifier("supplier",primaryBackendName),createRewriteRoute("supplier",generateInboundPath(HttpRequestTypes.DELETE.getName(),"supplier"),primaryBackendUri))
                .route(generateIdentifier("product",primaryBackendName),createRewriteRoute("product",generateInboundPath(HttpRequestTypes.POST.getName(),"product"),primaryBackendUri))
                .route(generateIdentifier("product",primaryBackendName),createRewriteRoute("product",generateInboundPath(HttpRequestTypes.DELETE.getName(),"product"),primaryBackendUri))
                .route(generateIdentifier("employee",primaryBackendName),createRewriteRoute("employee",generateInboundPath(HttpRequestTypes.POST.getName(),"employee"),primaryBackendUri))
                .route(generateIdentifier("employee",primaryBackendName),createRewriteRoute("employee",generateInboundPath(HttpRequestTypes.DELETE.getName(),"employee"),primaryBackendUri))
                .build();
    }

    private Function<PredicateSpec, Buildable<Route>> createRewriteRoute(
            String resource,String inbound,String backendUri) {

        String backend = streamURIPrefix + resource;

        return r -> r
                .path(inbound, inbound + "/**")
                .filters(f -> f.rewritePath(inbound + "(?<segment>/?.*)",
                        backend + "${segment}"))
                .uri(backendUri);
    }

    private String generateIdentifier(String resource,String backendName) {
        return String.format("%s-get-%s", backendName, resource);
    }

    private String generateInboundPath(String httpRequestType,String resource) {
        return String.format("/%s-%s", httpRequestType, resource);
    }

}
