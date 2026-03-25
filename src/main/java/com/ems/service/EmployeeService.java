package com.ems.service;

import com.ems.dto.EmployeeStatsDTO;
import com.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee updated);
    void deleteEmployeeById(Long id);
    List<Employee> findByDept(String dept);
    Optional<Employee> findByEmail(String email);

    // ============== PAGINATION METHODS ==============

    Page<Employee> getAllEmployeesWithPagination(Pageable pageable);
    Page<Employee> getEmployeesByDeptWithPagination(String dept, Pageable pageable);
    Page<Employee> getEmployeesByDesignationWithPagination(String designation, Pageable pageable);
    Page<Employee> searchEmployeesByNameWithPagination(String name, Pageable pageable);
    Page<Employee> searchEmployeesByEmailWithPagination(String email, Pageable pageable);
    Page<Employee> getEmployeesBySupervisorWithPagination(String supervisor, Pageable pageable);
    Page<Employee> getEmployeesBySalaryRangeWithPagination(Double minSalary, Double maxSalary, Pageable pageable);
    Page<Employee> getEmployeesBySalaryGreaterThanWithPagination(Double salary, Pageable pageable);
    Page<Employee> getEmployeesBySalaryLessThanWithPagination(Double salary, Pageable pageable);
    Page<Employee> getEmployeesByDeptAndDesignationWithPagination(String dept, String designation, Pageable pageable);
    Page<Employee> getAllEmployeesSortedBySalaryDescendingWithPagination(Pageable pageable);
    Page<Employee> getAllEmployeesSortedByNameAscendingWithPagination(Pageable pageable);
    Page<Employee> searchEmployeesByMultipleCriteriaWithPagination(String dept, String designation, String status, Double minSalary, Double maxSalary, String name, String email, Pageable pageable);

    // ============== UTILITY METHODS ==============

    Long countEmployeesByDept(String dept);
    Long countEmployeesByDesignation(String designation);
    boolean employeeExistsByEmail(String email);

    EmployeeStatsDTO getEmployeeStats();
}
