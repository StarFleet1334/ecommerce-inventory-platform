package com.example.inventoryservice.skeleton;

import com.example.inventoryservice.entity.messages.StockMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Stock Controller", description = "Delete & Post Operations related to Stocks")
@RequestMapping(value = "/api/v1/stock")
public interface StockControllerInterface {

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Stock",description = "Deletes a stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No stock with given ID exists")
    })
    ResponseEntity<String> deleteStockById(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Add Stock",description = "Add new stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postStock(@RequestBody StockMessage stockMessage,
                                     @RequestParam(required = false, defaultValue = "false") boolean initialLoad);
}
