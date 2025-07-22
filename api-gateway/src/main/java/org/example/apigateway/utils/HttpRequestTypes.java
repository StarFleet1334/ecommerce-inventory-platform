package org.example.apigateway.utils;

public enum HttpRequestTypes {

    POST("POST"),
    GET("GET"),
    DELETE("DELETE");

    private final String name;

    HttpRequestTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
