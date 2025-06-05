package com.example.inventoryservice.skeleton;


import com.example.inventoryservice.entity.common.InventoryItem;
import com.example.inventoryservice.entity.response.InventoryItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventory Controller", description = "CRUD Operations related to inventory management")
@RequestMapping(value = "/api/inventory")
public interface InventoryControllerInterface {

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Inventory", description = "Deletes an inventory item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No item with given ID exists")
    })
    ResponseEntity<String> deleteInventoryItem(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Post Inventory", description = "Add new inventory item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<InventoryItemResponse> postInventoryItem(@RequestBody InventoryItem inventoryItem);

}
