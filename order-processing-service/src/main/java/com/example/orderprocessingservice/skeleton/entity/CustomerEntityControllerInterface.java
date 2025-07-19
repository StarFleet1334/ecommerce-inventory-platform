package com.example.orderprocessingservice.skeleton.entity;

import com.example.orderprocessingservice.dto.model.customer.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Tag(name = "Customer Entity Controller", description = "Operations related to customer entity")
@RequestMapping(value = "/api/v1/customer")
public interface CustomerEntityControllerInterface {

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve customer", description = "Retrieves customer entity by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No customer with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID")
    })
    ResponseEntity<?> getCustomerById(@PathVariable("id") String id);

    @GetMapping
    @Operation(summary = "Retrieve all customers", description = "Retrieves all customer entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "No customers found")
    })
    ResponseEntity<List<Customer>> getAllCustomers();

}
