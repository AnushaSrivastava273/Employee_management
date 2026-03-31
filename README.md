# Employee Management REST API

A fully-featured Java Spring Boot REST API for managing an employee database. It demonstrates the use of Test-Driven Development (TDD), object-oriented programming, RESTful architecture, and robust internal configuration structures using Spring Data JPA.

## Technologies Used
- **Java 17+**
- **Spring Boot 3.2+** 
- **Spring Data JPA** & **Hibernate**
- **PostgreSQL** (Production Database)
- **H2 Database** (Test-Isolated Database)
- **Maven** (Build Tool)
- **JUnit 5 & Mockito** (Testing Frameworks)

## Features
- **Full CRUD operations** for Employees.
- **Advanced Filtering**: Filter employees using queries parameterizing `department`, `role`, or `status`.
- **Validation**: Strict server-side payload validation using JSR Constraints (`@Valid`, `@NotBlank`, `@Email`) on DTOs prior to internal processing.
- **Global Error Handling**: Centralized exception handler (`@ControllerAdvice`) mapping validation errors and unhandled exceptions into robust, consistent JSON error responses.
- **Test-Driven Development**: High test coverage across unit tests (mocking persistence boundaries) and end-to-end integration tests using `@SpringBootTest` configured against an isolated H2 in-memory DB.
- **Custom Mapping Entities**: Data layering includes custom Native SQL and JPQL methods inside repository interfaces.

## Prerequisites
- **JDK 17** or newer installed.
- **PostgreSQL** installed and running on default port `5432`.

## Getting Started

### 1. Database Configuration
By default, the application is set up to connect to a PostgreSQL database named `employeedb`. 
Ensure your PostgreSQL server is active, and update the credentials inside `src/main/resources/application.properties` if they differ:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employeedb
spring.datasource.username=postgres
spring.datasource.password=password
```
*(Hibernate `ddl-auto=update` is enabled, which will automatically create the required schemas when you run the application.)*

### 2. Running the Application
You can use the included Maven wrapper to build and start the server:
#### Windows:
```shell
./mvnw.cmd clean spring-boot:run
```
#### macOS / Linux:
```shell
./mvnw clean spring-boot:run
```

The server will initialize on `http://localhost:8080`.

### 3. Running the Tests
Execution of JUnit 5 tests, ensuring isolation against an integrated H2 profile and decoupled Mockito boundaries:
```shell
./mvnw clean test
```

## API Endpoints

### Employees

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/employees` | Create a new employee |
| `GET` | `/api/employees/{id}` | Read a specific employee by ID |
| `GET` | `/api/employees` | Retrieve a list of all employees |
| `PUT` | `/api/employees/{id}` | Update an existing employee's payload |
| `DELETE` | `/api/employees/{id}` | Remove an employee from persistence |

**API Filtering (GET):**
You can also append query arguments via parameters.
- `/api/employees?department=Sales`
- `/api/employees?role=DEVELOPER`
- `/api/employees?status=ACTIVE`

### Departments (Utility Endpoint)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/departments` | Generate an initial logical department structure |

---

*This application was built utilizing best-practice system architecture patterns for robust REST configurations natively.*
