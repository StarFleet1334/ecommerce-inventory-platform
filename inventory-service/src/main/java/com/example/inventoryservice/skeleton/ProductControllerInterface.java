package com.example.inventoryservice.skeleton;

import com.example.inventoryservice.entity.messages.ProductMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Controller",description = "Delete & Post Operations related to Products")
@RequestMapping(value = "/api/v1/product")
public interface ProductControllerInterface {

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Product",description = "Deletes a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No product with given ID exists")
    })
    ResponseEntity<String> deleteProductById(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Add Product",description = "Add new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postProduct(@RequestBody ProductMessage productMessage,
                                       @RequestParam(required = false, defaultValue = "false") boolean initialLoad);

}
