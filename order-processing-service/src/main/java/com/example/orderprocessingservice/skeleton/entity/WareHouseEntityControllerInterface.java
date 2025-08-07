package com.example.orderprocessingservice.skeleton.entity;

import com.example.orderprocessingservice.dto.messages.WhTransferMessage;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WareHouse Entity Controller", description = "Operations related to warehouse entity")
@RequestMapping(value = "/api/v1/warehouse")
public interface WareHouseEntityControllerInterface {

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve warehouse", description = "Retrieves warehouse entity by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WareHouse successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No warehouse with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid warehouse ID")
    })
    ResponseEntity<?> getWareHouseById(@PathVariable("id") String id);

    @GetMapping
    @Operation(summary = "Retrieve all warehouse", description = "Retrieves all warehouse entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WareHouses successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "No warehouse found")
    })
    ResponseEntity<List<WareHouse>> getAllWareHouse();

    @PostMapping("/trasnfer")
    @Operation(summary = "Transfer product from one warehouse to another", description = "Transfers product from one warehouse to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully transferred"),
            @ApiResponse(responseCode = "404", description = "No warehouse with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid warehouse ID")
    })
    ResponseEntity<String> transferProduct(@RequestBody WhTransferMessage whTransferMessage);

}
