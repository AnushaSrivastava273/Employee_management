package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Custom JPQL Query 1
    @Query("SELECT e FROM Employee e WHERE e.department.name = :departmentName")
    List<Employee> findByDepartmentName(@Param("departmentName") String departmentName);

    // Custom Native SQL Query 2
    @Query(value = "SELECT * FROM employees WHERE status = :status", nativeQuery = true)
    List<Employee> findByStatusNative(@Param("status") String status);

    // Spring Data JPA generated query (Bonus)
    List<Employee> findByRole(String role);
}
