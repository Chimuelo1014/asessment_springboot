package com.prueba.credit_application_service.domain.model;

import com.prueba.creditapp.domain.model.enums.AffiliateStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Affiliate - DOMAIN MODEL (100% PURE JAVA)
 * NO Lombok, NO JPA, NO Spring annotations
 * Only business logic and domain rules
 */
public class Affiliate {
    private Long id;
    private String document;
    private String fullName;
    private String email;
    private String phone;
    private Double monthlySalary;
    private AffiliateStatus status;
    private LocalDate affiliationDate;
    private List<CreditApplication> creditApplications;
    
    // Default constructor
    public Affiliate() {
        this.creditApplications = new ArrayList<>();
    }
    
    // Full constructor
    public Affiliate(Long id, String document, String fullName, String email, 
                     String phone, Double monthlySalary, AffiliateStatus status, 
                     LocalDate affiliationDate) {
        this.id = id;
        this.document = document;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.monthlySalary = monthlySalary;
        this.status = status;
        this.affiliationDate = affiliationDate;
        this.creditApplications = new ArrayList<>();
    }
    
    // BUSINESS LOGIC METHODS (Domain behavior)
    
    public boolean isActive() {
        return AffiliateStatus.ACTIVE.equals(this.status);
    }
    
    public boolean hasMinimumSeniority(int months) {
        if (this.affiliationDate == null) {
            return false;
        }
        LocalDate minimumDate = LocalDate.now().minusMonths(months);
        return this.affiliationDate.isBefore(minimumDate) || 
               this.affiliationDate.isEqual(minimumDate);
    }
    
    public Double getMaxCreditAmount() {
        return this.monthlySalary != null ? this.monthlySalary * 5 : 0.0;
    }
    
    public boolean canRequestCredit(Double amount, int minimumSeniorityMonths) {
        return isActive() && 
               hasMinimumSeniority(minimumSeniorityMonths) &&
               amount <= getMaxCreditAmount();
    }
    
    public void addCreditApplication(CreditApplication application) {
        if (this.creditApplications == null) {
            this.creditApplications = new ArrayList<>();
        }
        this.creditApplications.add(application);
    }
    
    // GETTERS AND SETTERS
    
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
    
    public AffiliateStatus getStatus() {
        return status;
    }
    
    public void setStatus(AffiliateStatus status) {
        this.status = status;
    }
    
    public LocalDate getAffiliationDate() {
        return affiliationDate;
    }
    
    public void setAffiliationDate(LocalDate affiliationDate) {
        this.affiliationDate = affiliationDate;
    }
    
    public List<CreditApplication> getCreditApplications() {
        return creditApplications;
    }
    
    public void setCreditApplications(List<CreditApplication> creditApplications) {
        this.creditApplications = creditApplications;
    }
    
    // EQUALS, HASHCODE, TOSTRING
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Affiliate affiliate = (Affiliate) o;
        return Objects.equals(id, affiliate.id) && 
               Objects.equals(document, affiliate.document);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, document);
    }
    
    @Override
    public String toString() {
        return "Affiliate{" +
                "id=" + id +
                ", document='" + document + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status=" + status +
                '}';
    }
}
