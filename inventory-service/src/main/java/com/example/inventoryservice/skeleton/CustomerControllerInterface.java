package com.example.inventoryservice.skeleton;

import com.example.inventoryservice.entity.messages.CustomerMessage;
import com.example.inventoryservice.entity.messages.CustomerOrderMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customer Controller", description = "Delete & Post Operations related to customers")
@RequestMapping(value = "/api/v1/customer")
public interface CustomerControllerInterface {

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Customer", description = "Deletes a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No customer with given ID exists")
    })
    ResponseEntity<String> deleteCustomerById(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Add Customer", description = "Add new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postCustomer(
            @RequestBody CustomerMessage customerMessage,
            @RequestParam(required = false, defaultValue = "false") boolean initialLoad
    );


    @PostMapping("/order")
    @Operation(summary = "Add Order",description = "Add Customer Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer Order successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postCustomerOrder(@RequestBody CustomerOrderMessage customerOrderMessage);
}