package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeStatsDTO {
    private Long totalEmployees;
    private Long activeEmployees;
    private Long inactiveEmployees;
    private Long totalDepartments;
}
