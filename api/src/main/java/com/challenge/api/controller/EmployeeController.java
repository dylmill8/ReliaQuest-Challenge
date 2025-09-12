package com.challenge.api.controller;

import com.challenge.api.dto.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.service.EmployeeService;
import com.challenge.api.service.InMemoryEmployeeService.DuplicateEmployeeException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST API for employees.
 *
 * GET /api/v1/employee - list all employees
 * GET /api/v1/employee/{uuid} - fetch one employee by UUID
 * POST /api/v1/employee - create new employee
 */
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /**
     * @return list of employees
     */
    @GetMapping(produces = "application/json")
    public List<Employee> getAllEmployees() {
        return service.findAll();
    }

    /**
     * Fetch a single employee by UUID.
     *
     * @implNote Need not be concerned with an actual persistence layer.
     * @param uuid Employee UUID
     * @return Requested Employee if exists
     * @throws ResponseStatusException 404 if not found
     */
    @GetMapping(path = "/{uuid}", produces = "application/json")
    public Employee getEmployeeByUuid(@PathVariable UUID uuid) {
        return service.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    /**
     * Create a new employee.
     *
     * @implNote Service generates UUID, derives fullName, enforces email uniqueness and sanity checks.
     * @param requestBody CreateEmployeeRequest from client
     * @return Created Employee
     * @throws ResponseStatusException 400 on validation errors, 409 on duplicate email
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest requestBody) {
        try {
            Employee created = service.create(requestBody);
            return ResponseEntity.created(URI.create("/api/v1/employee/" + created.getUuid()))
                    .body(created);
        } catch (DuplicateEmployeeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
