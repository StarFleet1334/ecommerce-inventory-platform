package com.example.orderprocessingservice.dto.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteCalculationResponse {
    @JsonProperty("distanceKm")
    private double distanceKm;

    @JsonProperty("durationSeconds")
    private double durationSeconds;
}

