# API Implementation

Overview
- Endpoints
  - GET /api/v1/employee — returns all employees.
  - GET /api/v1/employee/{uuid} — returns one employee or 404 if not found.
  - POST /api/v1/employee — creates a new employee, returns 201 Created & Location header.
- Security
  - Very small API key filter protecting all /api/** routes.
  - Header: `X-ERU-ApiKey`, value configured via `apiKey` in application.yml.
  - On valid key, the filter marks the request authenticated so /api/** passes auth rules.
- Design
  - The service creates `SimpleEmployee` from the `Employee` interface to limit client's access to information.
  - UUID is owned by the service, so the client never sends it.
  - `fullName` is derived from first + last.
  - New employees don't have a termination date on create, so they are initialized to null.

POST Request Implementation
- Required: firstName, lastName, email, jobTitle.
- Age must be positive and reasonable
- Salary >= 0 if present.
- contractHireDate must be now or in the past (defaults to now if omitted).
- Email must be unique.

Storage Implementation:
- Implemented an in-memory service to test the code for getting newly created employees (without data persistence).

Files worth a look
- Controller: com.challenge.api.controller.EmployeeController
- Service: com.challenge.api.service.EmployeeService, com.challenge.api.service.InMemoryEmployeeService
- Model: com.challenge.api.model.Employee, com.challenge.api.model.SimpleEmployee
- DTO: com.challenge.api.dto.CreateEmployeeRequest
- Security: com.challenge.api.security.SecurityConfig, com.challenge.api.security.ApiKeyAuthFilter
- Tests: api/src/test/java/com/challenge/api/controller/EmployeeControllerIT.java

Example create (Git Bash):

curl -i -X POST http://localhost:8080/api/v1/employee \
    -H 'Content-Type: application/json' \
    -H 'X-ERU-ApiKey: super-secret-api-key' \
    -d '{"firstName":"Ada","lastName":"Lovelace","email":"ada@example.com","jobTitle":"Engineer"}'