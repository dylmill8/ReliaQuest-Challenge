package com.challenge.api.dto;

import java.time.Instant;

/**
 * Input DTO for POST /employee.
 *
 * @implNote Client-supplied fields only. No uuid/fullName/terminationDate here; the service derives or rejects those.
 */
public class CreateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;
    private Integer salary;
    private Integer age;
    private Instant contractHireDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String v) {
        this.firstName = v;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String v) {
        this.lastName = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        this.email = v;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String v) {
        this.jobTitle = v;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer v) {
        this.salary = v;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer v) {
        this.age = v;
    }

    public Instant getContractHireDate() {
        return contractHireDate;
    }

    public void setContractHireDate(Instant v) {
        this.contractHireDate = v;
    }
}
