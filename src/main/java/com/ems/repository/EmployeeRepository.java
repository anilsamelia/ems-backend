package com.ems.repository;

import com.ems.dto.EmployeeStatsDTO;
import com.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find by department
    List<Employee> findByDept(String dept);
    Optional<Employee> findByEmail(String email);

    // ============== PAGINATION METHODS ==============

    // Find all with pagination
    Page<Employee> findAll(Pageable pageable);

    // Find by department with pagination
    Page<Employee> findByDept(String dept, Pageable pageable);

    // Find by designation with pagination
    Page<Employee> findByDesignation(String designation, Pageable pageable);

    // Find by name (case-insensitive) with pagination
    Page<Employee> findByNameIgnoreCase(String name, Pageable pageable);

    // Find by supervisor with pagination
    Page<Employee> findBySupervisor(String supervisor, Pageable pageable);

    // Find by salary range with pagination
    Page<Employee> findBySalaryBetween(Double minSalary, Double maxSalary, Pageable pageable);

    // Find by department and designation with pagination
    Page<Employee> findByDeptAndDesignation(String dept, String designation, Pageable pageable);

    // Find employees with salary greater than with pagination
    Page<Employee> findBySalaryGreaterThan(Double salary, Pageable pageable);

    // Find employees with salary less than with pagination
    Page<Employee> findBySalaryLessThan(Double salary, Pageable pageable);

    // ============== CUSTOM QUERY METHODS WITH PAGINATION ==============

    // Search by name containing (partial match) with pagination
    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Employee> searchByName(@Param("name") String name, Pageable pageable);

    // Search by email containing with pagination
    @Query("SELECT e FROM Employee e WHERE LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Employee> searchByEmail(@Param("email") String email, Pageable pageable);

    // Find all employees sorted by salary (highest to lowest) with pagination
    @Query("SELECT e FROM Employee e ORDER BY e.salary DESC")
    Page<Employee> findAllBySalaryDescending(Pageable pageable);

    // Find all employees sorted by name with pagination
    @Query("SELECT e FROM Employee e ORDER BY e.name ASC")
    Page<Employee> findAllByNameAscending(Pageable pageable);


    // Search by multiple criteria with pagination
    @Query("SELECT e FROM Employee e WHERE " +
           "(:dept IS NULL OR e.dept = :dept) AND " +
           "(:designation IS NULL OR e.designation = :designation) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:doj IS NULL OR e.doj = :doj) AND " +
           "(:minSalary IS NULL OR e.salary >= :minSalary) AND " +
           "(:maxSalary IS NULL OR e.salary <= :maxSalary)")
    Page<Employee> searchByMultipleCriteria(
            @Param("dept") String dept,
            @Param("designation") String designation,
            @Param("status") String status,
            @Param("doj") String doj,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary,
            Pageable pageable);






    // ============== NON-PAGINATION METHODS ==============

    // Count employees by department
    Long countByDept(String dept);

    // Count employees by designation
    Long countByDesignation(String designation);

    // Check if employee exists by email
    boolean existsByEmail(String email);

    @Query("SELECT new com.ems.dto.EmployeeStatsDTO(" +
           "COUNT(e), " +
           "COUNT(CASE WHEN e.status = 'active' THEN 1 END), " +
           "COUNT(CASE WHEN e.status = 'inactive' THEN 1 END), " +
           "COUNT(DISTINCT e.dept)) " +
           "FROM Employee e")
    EmployeeStatsDTO getEmployeeStats();
}
