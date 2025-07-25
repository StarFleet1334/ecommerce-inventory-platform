package org.example.apigateway.model;

public record RedisSettings(String redisHost, int redisPort, String redisUser, String redisPassword) { }
