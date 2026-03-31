package com.example.employeemanagement.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EmployeeIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Department currentDepartment;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        Department department = new Department("Sales");
        currentDepartment = departmentRepository.save(department);
    }

    @Test
    void createEmployee_Success_Integration() {
        EmployeeDTO requestDTO = new EmployeeDTO(null, "Alice", "Smith", "alice.smith@example.com",
                currentDepartment.getId(), "MANAGER", "ACTIVE");
        ResponseEntity<EmployeeDTO> response = restTemplate.postForEntity("/api/employees", requestDTO,
                EmployeeDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alice", response.getBody().getFirstName());
        assertEquals(currentDepartment.getId(), response.getBody().getDepartmentId());

        // Check DB
        assertEquals(1, employeeRepository.findAll().size());
    }

    @Test
    void getEmployeesByDepartment_Success_Integration() {
        // Setup existing
        EmployeeDTO requestDTO1 = new EmployeeDTO(null, "Bob", "Jones", "bob@example.com", currentDepartment.getId(),
                "STAFF", "ACTIVE");
        restTemplate.postForEntity("/api/employees", requestDTO1, EmployeeDTO.class);

        // Find by department
        ResponseEntity<List<EmployeeDTO>> response = restTemplate.exchange(
                "/api/employees?department=Sales",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EmployeeDTO>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Bob", response.getBody().get(0).getFirstName());
    }

    @Test
    void createEmployee_ValidationError_Returns400() {
        // Missing fast name and invalid email
        EmployeeDTO invalidRequest = new EmployeeDTO(null, "", "Smith", "invalid-email", currentDepartment.getId(),
                "MANAGER", "ACTIVE");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/employees", invalidRequest, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Validation Failed"));
        assertTrue(response.getBody().contains("Email should be valid"));
        assertTrue(response.getBody().contains("First name is mandatory"));
    }
}
