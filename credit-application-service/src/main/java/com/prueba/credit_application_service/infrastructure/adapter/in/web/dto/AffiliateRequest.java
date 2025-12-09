package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.*;

/**
 * AffiliateRequest - INFRASTRUCTURE LAYER DTO
 * Used for HTTP input, NOT part of domain
 */
public class AffiliateRequest {
    
    @NotBlank(message = "Document is required")
    @Size(max = 20)
    private String document;
    
    @NotBlank(message = "Full name is required")
    @Size(max = 200)
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(max = 20)
    private String phone;
    
    @NotNull(message = "Monthly salary is required")
    @Min(value = 1, message = "Salary must be greater than 0")
    private Double monthlySalary;
    
    // Constructors
    public AffiliateRequest() {}
    
    public AffiliateRequest(String document, String fullName, String email, 
                           String phone, Double monthlySalary) {
        this.document = document;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.monthlySalary = monthlySalary;
    }
    
    // Getters and Setters
    public String getDocument() {
        return document;
    }
    
    public void setDocument(String document) {
        this.document = document;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Double getMonthlySalary() {
        return monthlySalary;
    }
    
    public void setMonthlySalary(Double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }
}
