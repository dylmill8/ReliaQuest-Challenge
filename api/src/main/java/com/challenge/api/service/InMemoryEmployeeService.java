package com.challenge.api.service;

import com.challenge.api.dto.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.model.SimpleEmployee;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * In-memory implementation of EmployeeService.
 *
 * @implNote no data persistence. Uses two thread-safe maps for storage in memory to easily run and test.
 */
@Service
public class InMemoryEmployeeService implements EmployeeService {

    private final Map<UUID, Employee> byId = new ConcurrentHashMap<>();
    private final Map<String, UUID> emailIndex = new ConcurrentHashMap<>();

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(byId.values());
    }

    @Override
    public Optional<Employee> findByUuid(UUID uuid) {
        if (uuid == null) return Optional.empty();
        return Optional.ofNullable(byId.get(uuid));
    }

    @Override
    public Employee create(CreateEmployeeRequest request) {
        validateCreate(request);

        String first = request.getFirstName().trim();
        String last = request.getLastName().trim();
        String emailNorm = normalizeEmail(request.getEmail());
        String jobTitle = request.getJobTitle().trim();

        if (emailIndex.containsKey(emailNorm)) {
            throw new DuplicateEmployeeException("Employee with email already exists");
        }

        Instant now = Instant.now();
        Instant hire = (request.getContractHireDate() == null) ? now : request.getContractHireDate();

        SimpleEmployee e = new SimpleEmployee();
        e.setUuid(UUID.randomUUID()); // service assigns UUID
        e.setFirstName(first);
        e.setLastName(last);
        e.setFullName((first + " " + last).trim()); // derive fullName
        e.setEmail(emailNorm);
        e.setJobTitle(jobTitle);
        e.setSalary(request.getSalary());
        e.setAge(request.getAge());
        e.setContractHireDate(hire);
        e.setContractTerminationDate(null); // null on create

        byId.put(e.getUuid(), e);
        emailIndex.put(emailNorm, e.getUuid());
        return e;
    }

    /**
     * Validate incoming creation request.
     *
     * @implNote checks: required strings present, email format, positive numbers, and realistic hire date.
     * @param request request payload
     */
    private void validateCreate(CreateEmployeeRequest request) {
        if (request == null) throw new IllegalArgumentException("Request body is required");
        if (isBlank(request.getFirstName())) throw new IllegalArgumentException("firstName is required");
        if (isBlank(request.getLastName())) throw new IllegalArgumentException("lastName is required");
        if (isBlank(request.getEmail())) throw new IllegalArgumentException("email is required");
        if (isBlank(request.getJobTitle())) throw new IllegalArgumentException("jobTitle is required");

        if (!request.getEmail().contains("@")) throw new IllegalArgumentException("email must contain @");

        if (request.getAge() != null && (request.getAge() <= 0 || request.getAge() > 120))
            throw new IllegalArgumentException("age must be between 1 and 120");

        if (request.getSalary() != null && request.getSalary() < 0)
            throw new IllegalArgumentException("salary must be >= 0");

        if (request.getContractHireDate() != null
                && request.getContractHireDate().isAfter(Instant.now()))
            throw new IllegalArgumentException("contractHireDate cannot be in the future");
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public static class DuplicateEmployeeException extends RuntimeException {
        public DuplicateEmployeeException(String msg) {
            super(msg);
        }
    }
}
