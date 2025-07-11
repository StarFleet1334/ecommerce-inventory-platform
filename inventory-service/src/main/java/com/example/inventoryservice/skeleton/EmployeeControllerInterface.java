package com.example.inventoryservice.skeleton;

import com.example.inventoryservice.entity.messages.EmployeeMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Employee Controller",description = "Delete & Post Operations related to Employees")
@RequestMapping(value = "/api/v1/employee")
public interface EmployeeControllerInterface {

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Employee",description = "Deletes an employee by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted."),
            @ApiResponse(responseCode = "404", description = "No employee with given ID exists")
    })
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Add Employee",description = "Add new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee successfully added."),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields")
    })
    ResponseEntity<String> postEmployee(@RequestBody EmployeeMessage employeeMessage,
                                        @RequestParam(required = false, defaultValue = "false") boolean initialLoad);
}
