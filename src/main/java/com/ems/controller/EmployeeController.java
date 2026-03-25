package com.ems.controller;

import com.ems.dto.EmployeeStatsDTO;
import com.ems.model.Employee;
import com.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employees", description = "Employee management endpoints with CRUD operations, pagination, and advanced search")
@SecurityRequirement(name = "Bearer Authentication")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ============== BASIC CRUD ENDPOINTS ==============

    @Operation(summary = "Create new employee", description = "Create a new employee record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee data")
    })
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Get employee by ID", description = "Retrieve a specific employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update employee", description = "Update an existing employee record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Invalid employee data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id,
            @RequestBody Employee employee) {
        Employee updated = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete employee", description = "Delete an employee record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

    // ============== PAGINATION ENDPOINTS ==============

    /**
     * Get all employees with pagination
     * Example: /api/employees/paginated?page=0&size=10&sort=name,asc
     */
    @Operation(summary = "Get all employees with pagination", description = "Retrieve all employees with pagination support")
    @ApiResponse(responseCode = "200", description = "Employees retrieved with pagination")
    @GetMapping("/paginated/all")
    public ResponseEntity<Page<Employee>> getAllEmployeesWithPagination(Pageable pageable) {
        Page<Employee> employees = employeeService.getAllEmployeesWithPagination(pageable);
        return ResponseEntity.ok(employees);
    }



    /**
     * Advanced search with multiple criteria and pagination
     * Example: /api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=50000&maxSalary=80000&page=0&size=10
     */
    // add status filter to the search criteria

    @Operation(summary = "Advanced employee search", description = "Search employees using multiple criteria (department, designation, salary range) with pagination")
    @ApiResponse(responseCode = "200", description = "Search results with pagination")
    @GetMapping("/paginated/search/advanced")
    public ResponseEntity<Page<Employee>> searchEmployeesByMultipleCriteriaWithPagination(
            @Parameter(description = "Department filter (optional)") @RequestParam(required = false) String dept,
            @Parameter(description = "Designation filter (optional)") @RequestParam(required = false) String designation,
            @Parameter(description = "active/inactive (optional)") @RequestParam(required = false) String status,
            @Parameter(description = "Minimum salary filter (optional)") @RequestParam(required = false) Double minSalary,
            @Parameter(description = "Maximum salary filter (optional)") @RequestParam(required = false) Double maxSalary,
            @Parameter(description = "Name filter (optional, partial match)") @RequestParam(required = false) String name,
            @Parameter(description = "Email filter (optional, partial match)") @RequestParam(required = false) String email,
            Pageable pageable) {
        Page<Employee> employees = employeeService.searchEmployeesByMultipleCriteriaWithPagination(dept, designation, status, minSalary, maxSalary, name, email, pageable);
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get employee statistics", description = "Retrieve statistics about employees (total, active, inactive, departments)")
    @ApiResponse(responseCode = "200", description = "Employee statistics retrieved successfully")
    @GetMapping("/stats")
    public ResponseEntity<EmployeeStatsDTO> getEmployeeStats() {
        EmployeeStatsDTO stats = employeeService.getEmployeeStats();
        return ResponseEntity.ok(stats);
    }
}
