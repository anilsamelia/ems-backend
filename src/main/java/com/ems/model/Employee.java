package com.ems.model;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Schema(description = "Employee entity representing a company employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Employee ID (auto-generated)", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "Employee full name (required, max 100 characters)", example = "Alice Johnson")
    private String name;

    @Column(name = "email", unique = true, length = 100)
    @Schema(description = "Employee email (unique, max 100 characters)", example = "alice.johnson@example.com")
    private String email;

    @Column(name = "dept", length = 50)
    @Schema(description = "Department name (max 50 characters)", example = "IT")
    private String dept;

    @Column(name = "designation", length = 100)
    @Schema(description = "Job designation/position (max 100 characters)", example = "Senior Developer")
    private String designation;

    @Column(name = "salary")
    @Schema(description = "Annual salary amount", example = "60000.0")
    private Double salary;

    @Column(name = "supervisor", length = 100)
    @Schema(description = "Supervisor name (max 100 characters)", example = "John Smith")
    private String supervisor;

    @Column(name = "address", length = 255)
    @Schema(description = "Employee address (max 255 characters)", example = "123 Main Street, New York, NY")
    private String address;

    @Column(name = "doj", length = 50)
    @Schema(description = "Date of Joining (max 50 characters)", example = "2023-01-15")
    private String doj;

    @Column(name = "status", length = 10)
    @Schema(description = "Active/inactive", example = "active")
    private String status;

}

