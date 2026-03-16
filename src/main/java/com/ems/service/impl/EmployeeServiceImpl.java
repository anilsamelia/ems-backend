package com.ems.service.impl;

import com.ems.dto.EmployeeStatsDTO;
import com.ems.exception.DuplicateEmailException;
import com.ems.exception.EmployeeNotFoundException;
import com.ems.exception.InvalidEmployeeDataException;
import com.ems.model.Employee;
import com.ems.repository.EmployeeRepository;
import com.ems.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;



    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        if (employee == null || employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Employee name is required");
        }
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Employee email is required");
        }

        // Check for duplicate email
        if (repository.existsByEmail(employee.getEmail())) {
            throw new DuplicateEmailException(employee.getEmail());
        }

        return repository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Long id, Employee updated) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        // Check if new email is already in use by another employee
        if (!existing.getEmail().equals(updated.getEmail()) &&
            repository.existsByEmail(updated.getEmail())) {
            throw new DuplicateEmailException(updated.getEmail());
        }

        // update fields
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setDept(updated.getDept());
        existing.setDesignation(updated.getDesignation());
        existing.setSalary(updated.getSalary());
        existing.setSupervisor(updated.getSupervisor());
        existing.setAddress(updated.getAddress());
        existing.setDoj(updated.getDoj());
        return repository.save(existing);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Employee> findByDept(String dept) {
        return repository.findByDept(dept);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // ============== PAGINATION METHODS ==============

    /**
     * Get all employees with pagination
     */

    public Page<Employee> getAllEmployeesWithPagination(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Get employees by department with pagination
     */
    public Page<Employee> getEmployeesByDeptWithPagination(String dept, Pageable pageable) {
        return repository.findByDept(dept, pageable);
    }

    /**
     * Get employees by designation with pagination
     */
    public Page<Employee> getEmployeesByDesignationWithPagination(String designation, Pageable pageable) {
        return repository.findByDesignation(designation, pageable);
    }

    /**
     * Search employees by name with pagination
     */
    public Page<Employee> searchEmployeesByNameWithPagination(String name, Pageable pageable) {
        return repository.searchByName(name, pageable);
    }

    /**
     * Search employees by email with pagination
     */
    public Page<Employee> searchEmployeesByEmailWithPagination(String email, Pageable pageable) {
        return repository.searchByEmail(email, pageable);
    }

    /**
     * Get employees by supervisor with pagination
     */
    public Page<Employee> getEmployeesBySupervisorWithPagination(String supervisor, Pageable pageable) {
        return repository.findBySupervisor(supervisor, pageable);
    }

    /**
     * Get employees by salary range with pagination
     */
    public Page<Employee> getEmployeesBySalaryRangeWithPagination(Double minSalary, Double maxSalary, Pageable pageable) {
        return repository.findBySalaryBetween(minSalary, maxSalary, pageable);
    }

    /**
     * Get employees with salary greater than specified amount with pagination
     */
    public Page<Employee> getEmployeesBySalaryGreaterThanWithPagination(Double salary, Pageable pageable) {
        return repository.findBySalaryGreaterThan(salary, pageable);
    }

    /**
     * Get employees with salary less than specified amount with pagination
     */
    public Page<Employee> getEmployeesBySalaryLessThanWithPagination(Double salary, Pageable pageable) {
        return repository.findBySalaryLessThan(salary, pageable);
    }

    /**
     * Get employees by department and designation with pagination
     */
    public Page<Employee> getEmployeesByDeptAndDesignationWithPagination(String dept, String designation, Pageable pageable) {
        return repository.findByDeptAndDesignation(dept, designation, pageable);
    }

    /**
     * Get all employees sorted by salary (descending) with pagination
     */
    public Page<Employee> getAllEmployeesSortedBySalaryDescendingWithPagination(Pageable pageable) {
        return repository.findAllBySalaryDescending(pageable);
    }

    /**
     * Get all employees sorted by name (ascending) with pagination
     */
    public Page<Employee> getAllEmployeesSortedByNameAscendingWithPagination(Pageable pageable) {
        return repository.findAllByNameAscending(pageable);
    }

    /**
     * Advanced search with multiple criteria and pagination
     */
    public Page<Employee> searchEmployeesByMultipleCriteriaWithPagination(String dept, String designation, String status, String doj, Double minSalary, Double maxSalary, Pageable pageable) {
        return repository.searchByMultipleCriteria(dept, designation,status, doj, minSalary, maxSalary, pageable);
    }



    // ============== UTILITY METHODS ==============

    /**
     * Count employees by department
     */
    public Long countEmployeesByDept(String dept) {
        return repository.countByDept(dept);
    }

    /**
     * Count employees by designation
     */
    public Long countEmployeesByDesignation(String designation) {
        return repository.countByDesignation(designation);
    }

    /**
     * Check if employee exists by email
     */
    public boolean employeeExistsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public EmployeeStatsDTO getEmployeeStats() {
        return repository.getEmployeeStats();
    }
}
