package com.challenge.api.service;

import com.challenge.api.dto.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing Employee entities.
 */
public interface EmployeeService {

    /**
     * @return list of employees
     */
    List<Employee> findAll();

    /**
     * @param uuid Employee UUID
     * @return Requested Employee if exists
     */
    Optional<Employee> findByUuid(UUID uuid);

    /**
     * @implNote generated UUID, derives fullName, defaults termination date to null, forces unique email, and performs simple sanity checks.
     * @param request client-supplied fields allowed on creation
     * @return created Employee
     * @throws IllegalArgumentException for validation failures
     */
    Employee create(CreateEmployeeRequest request);
}
