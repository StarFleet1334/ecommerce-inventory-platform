package com.example.inventoryservice.skeleton;

import com.example.inventoryservice.entity.messages.SupplierMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Supplier Controller",description = "Delete & Post Operations related to Suppliers")
@RequestMapping(value = "/api/v1/supplier")
public interface SupplierControllerInterface {

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Supplier",description = "Deletes a supplier by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No supplier with given ID exists")
    })
    ResponseEntity<String> deleteSupplierById(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Add Supplier",description = "Add new supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postSupplier(@RequestBody SupplierMessage supplierMessage,
                                        @RequestParam(required = false, defaultValue = "false") boolean initialLoad);

}
