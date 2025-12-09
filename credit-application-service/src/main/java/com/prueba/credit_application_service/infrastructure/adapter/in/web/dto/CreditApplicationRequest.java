package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * CreditApplicationRequest - INFRASTRUCTURE LAYER DTO
 */
public class CreditApplicationRequest {
    
    @NotNull(message = "Affiliate ID is required")
    private Long affiliateId;
    
    @NotNull(message = "Requested amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double requestedAmount;
    
    @NotNull(message = "Term in months is required")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer termMonths;
    
    // Constructors
    public CreditApplicationRequest() {}
    
    public CreditApplicationRequest(Long affiliateId, Double requestedAmount, 
                                   Integer termMonths) {
        this.affiliateId = affiliateId;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
    }
    
    // Getters and Setters
    public Long getAffiliateId() {
        return affiliateId;
    }
    
    public void setAffiliateId(Long affiliateId) {
        this.affiliateId = affiliateId;
    }
    
    public Double getRequestedAmount() {
        return requestedAmount;
    }
    
    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
    
    public Integer getTermMonths() {
        return termMonths;
    }
    
    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }
}
