package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

/**
 * AffiliateResponse - INFRASTRUCTURE LAYER DTO
 * Used for HTTP output, NOT part of domain
 */
public class AffiliateResponse {
    private Long id;
    private String document;
    private String fullName;
    private String email;
    private String phone;
    private Double monthlySalary;
    private String status;
    private String affiliationDate;
    
    // Constructors
    public AffiliateResponse() {}
    
    public AffiliateResponse(Long id, String document, String fullName, String email, 
                            String phone, Double monthlySalary, String status, 
                            String affiliationDate) {
        this.id = id;
        this.document = document;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.monthlySalary = monthlySalary;
        this.status = status;
        this.affiliationDate = affiliationDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getAffiliationDate() {
        return affiliationDate;
    }
    
    public void setAffiliationDate(String affiliationDate) {
        this.affiliationDate = affiliationDate;
    }
}
