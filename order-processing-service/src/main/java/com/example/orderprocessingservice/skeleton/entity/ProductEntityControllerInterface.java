package com.example.orderprocessingservice.skeleton.entity;

import com.example.orderprocessingservice.dto.model.asset.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Tag(name = "Product Entity Controller", description = "Operations related to product entity")
@RequestMapping(value = "/api/v1/product")
public interface ProductEntityControllerInterface {

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve product", description = "Retrieves product entity by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No product with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID")
    })
    ResponseEntity<?> getProductById(@PathVariable("id") String id);

    @GetMapping
    @Operation(summary = "Retrieve all product", description = "Retrieves all product entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "No product found")
    })
    ResponseEntity<List<Product>> getAllProduct();

}
