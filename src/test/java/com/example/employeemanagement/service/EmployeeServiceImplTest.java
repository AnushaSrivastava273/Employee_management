package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department("Engineering");
        department.setId(1L);

        employee = new Employee("John", "Doe", "john.doe@example.com", department, "DEVELOPER", "ACTIVE");
        employee.setId(1L);

        employeeDTO = new EmployeeDTO(1L, "John", "Doe", "john.doe@example.com", 1L, "DEVELOPER", "ACTIVE");
    }

    @Test
    void createEmployee_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO created = employeeService.createEmployee(employeeDTO);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_DepartmentNotFound_ThrowsException() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.createEmployee(employeeDTO));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO found = employeeService.getEmployeeById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("John", found.getFirstName());
    }

    @Test
    void getEmployeeById_NotFound_ThrowsException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void getAllEmployees_Success() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        assertNotNull(employees);
        assertEquals(1, employees.size());
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeDTO.setFirstName("Jane");
        employee.setFirstName("Jane");

        EmployeeDTO updated = employeeService.updateEmployee(1L, employeeDTO);

        assertNotNull(updated);
        assertEquals("Jane", updated.getFirstName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void getEmployeesByDepartment_Success() {
        when(employeeRepository.findByDepartmentName("Engineering")).thenReturn(Arrays.asList(employee));

        List<EmployeeDTO> result = employeeService.getEmployeesByDepartment("Engineering");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
