package com.example.inventoryservice.skeleton;

import com.example.inventoryservice.entity.messages.WareHouseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WareHouse Controller",description = "Delete & Post Operations related to WareHouse")
@RequestMapping(value = "/api/v1/warehouse")
public interface WareHouseControllerInterface {

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete WareHouse",description = "Deletes a warehouse by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No warehouse with given ID exists")
    })
    ResponseEntity<String> deleteWareHouseById(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Add WareHouse",description = "Add new warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Warehouse successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postWareHouse(@RequestBody WareHouseMessage wareHouseMessage);
}
