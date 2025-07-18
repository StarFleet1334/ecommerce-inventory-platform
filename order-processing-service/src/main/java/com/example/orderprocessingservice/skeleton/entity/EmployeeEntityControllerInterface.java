package com.example.orderprocessingservice.skeleton.entity;

import com.example.orderprocessingservice.dto.model.personnel.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Employee Entity Controller", description = "Operations related to employee entity")
@RequestMapping(value = "/api/v1/employee")
public interface EmployeeEntityControllerInterface {

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve employee", description = "Retrieves employee entity by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No employee with given ID exists"),
            @ApiResponse(responseCode = "400", description = "Invalid employee ID")
    })
    ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id);

    @GetMapping
    @Operation(summary = "Retrieve all employee", description = "Retrieves all employee entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "No employee found")
    })
    ResponseEntity<List<Employee>> getAllEmployee();

}
