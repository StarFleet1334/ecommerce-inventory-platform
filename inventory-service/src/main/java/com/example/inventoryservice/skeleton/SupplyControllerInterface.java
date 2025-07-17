package com.example.inventoryservice.skeleton;

import com.example.inventoryservice.entity.messages.SupplyMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Supply Controller", description = "Operations related to Supplies")
@RequestMapping(value = "/api/v1/supply")
public interface SupplyControllerInterface {

    @PostMapping
    @Operation(summary = "Add Supply",description = "Add new supply")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supply successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postSupplier(@RequestBody SupplyMessage supplyMessage);

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Supply",description = "Deletes a supply by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No Supply with given ID exists")
    })
    ResponseEntity<String> deleteSupplyById(@PathVariable String id);

}
