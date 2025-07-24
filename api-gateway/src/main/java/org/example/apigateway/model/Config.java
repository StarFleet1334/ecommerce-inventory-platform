package org.example.apigateway.model;

import java.time.Duration;

public record Config(int replenishRate, int burstCapacity, Duration interval) { }
