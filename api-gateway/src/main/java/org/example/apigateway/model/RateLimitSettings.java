package org.example.apigateway.model;

public record RateLimitSettings(int replenishRate, int burstCapacity, int duration) { }
