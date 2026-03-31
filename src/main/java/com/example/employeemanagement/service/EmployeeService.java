package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeDTO;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO getEmployeeById(Long id);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);

    List<EmployeeDTO> getEmployeesByDepartment(String departmentName);

    List<EmployeeDTO> getEmployeesByRole(String role);

    List<EmployeeDTO> getEmployeesByStatus(String status);
}
