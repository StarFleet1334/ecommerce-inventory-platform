package com.example.orderprocessingservice.skeleton;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Order System Controller", description = "Operations related to customers orders")
@RequestMapping(value = "/api/v1/customer/order")
public interface OrderSystemControllerInterface {

    @PostMapping(value = "speedUp/{orderId}")
    @Operation(summary = "Speed up customer's order", description = "Speeds up processing of a customer order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully expedited"),
            @ApiResponse(responseCode = "404", description = "No order with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid order ID")
    })
    ResponseEntity<String> speedUpCustomersOrder(@PathVariable("orderId") int orderId);
}
