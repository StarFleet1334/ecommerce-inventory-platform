package com.example.orderprocessingservice.skeleton;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Supply System Controller", description = "Operations related to supplies")
@RequestMapping(value = "/api/v1/employee/supply")
public interface SupplySystemControllerInterface {

    @PostMapping(value = "speedUp/{supplyId}")
    @Operation(summary = "Speed up employee's supply", description = "Speeds up processing of a employees supply by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "supply successfully expedited"),
            @ApiResponse(responseCode = "404", description = "No supply with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid supply ID")
    })
    ResponseEntity<String> speedUpEmployeesSupply(@PathVariable("supplyId") int supplyId);
}

