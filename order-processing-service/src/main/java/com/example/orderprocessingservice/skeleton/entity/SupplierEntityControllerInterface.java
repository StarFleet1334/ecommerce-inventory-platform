package com.example.orderprocessingservice.skeleton.entity;

import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Tag(name = "Supplier Entity Controller", description = "Operations related to supplier entity")
@RequestMapping(value = "/api/v1/supplier")
public interface SupplierEntityControllerInterface {

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve supplier", description = "Retrieves supplier entity by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No supplier with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier ID")
    })
    ResponseEntity<?> getSupplierById(@PathVariable("id") String id);

    @GetMapping
    @Operation(summary = "Retrieve all supplier", description = "Retrieves all supplier entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "No supplier found")
    })
    ResponseEntity<List<Supplier>> getAllSupplier();

}
