package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class AffiliateUpdateRequest {
    
    @Size(max = 200, message = "Full name cannot exceed 200 characters")
    private String fullName;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    private String phone;
    
    @Min(value = 1, message = "Salary must be greater than 0")
    private Double monthlySalary;
    
    public AffiliateUpdateRequest() {}
    
    public AffiliateUpdateRequest(String fullName, String email, String phone, Double monthlySalary) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.monthlySalary = monthlySalary;
    }
    
    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Double getMonthlySalary() { return monthlySalary; }
    public void setMonthlySalary(Double monthlySalary) { this.monthlySalary = monthlySalary; }
}