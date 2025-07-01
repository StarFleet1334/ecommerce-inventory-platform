package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.model.order.RouteCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RouteCalculationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteCalculationService.class);
    private final RestTemplate restTemplate;

    @Value("${route.calculation.service.url}")
    private String routeServiceUrl;

    public RouteCalculationResponse calculateRoute(BigDecimal originLat, BigDecimal originLon,
                                                   BigDecimal destLat, BigDecimal destLon) {
        String url = routeServiceUrl;

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("originLat", originLat)
                .queryParam("originLon", originLon)
                .queryParam("destLat", destLat)
                .queryParam("destLon", destLon)
                .build()
                .toUri();

        LOGGER.info("Making request to: {}", uri);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<RouteCalculationResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    RouteCalculationResponse.class
            );

            LOGGER.info("Received response status: {}", response.getStatusCode());
            LOGGER.info("Received response body: {}", response.getBody());

            return response.getBody();
        } catch (RestClientException e) {
            LOGGER.error("Error calling route calculation service: {} - {}", e.getClass().getName(), e.getMessage(), e);
            throw new RuntimeException("Failed to calculate route", e);
        }

    }

}
