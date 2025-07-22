package org.example.apigateway.utils;

public enum HttpRequestTypes {

    POST("post"),
    GET("get"),
    DELETE("delete");

    private final String name;

    HttpRequestTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
