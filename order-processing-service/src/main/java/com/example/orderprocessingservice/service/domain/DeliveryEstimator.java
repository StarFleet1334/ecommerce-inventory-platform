package com.example.orderprocessingservice.service.domain;

import com.example.orderprocessingservice.dto.model.order.RouteCalculationResponse;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.service.RouteCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class DeliveryEstimator {

    private final RouteCalculationService routeSvc;

    public RouteCalculationResponse bestRoute(BigDecimal custLat, BigDecimal custLon,
                                              Collection<WareHouse> sources) {

        return sources.stream()
                .map(wh -> routeSvc.calculateRoute(custLat, custLon,
                        wh.getLatitude(), wh.getLongitude()))
                .min(Comparator.comparing(RouteCalculationResponse::getDistanceKm))
                .orElse(null);
    }

    public OffsetDateTime expectedArrival(RouteCalculationResponse route) {
        return OffsetDateTime.now().plus(route != null
                ? Duration.ofSeconds((long) route.getDurationSeconds())
                : Duration.ofHours(24));
    }

}
