package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreditApplicationRequest {
    
    @NotNull(message = "Affiliate ID is required")
    private Long affiliateId;
    
    @NotNull(message = "Requested amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double requestedAmount;
    
    @NotNull(message = "Term in months is required")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer termMonths;
    
    @NotNull(message = "Interest rate is required") // NUEVO
    @DecimalMin(value = "0.0", message = "Interest rate must be positive")
    private Double interestRate;
    
    public CreditApplicationRequest() {}
    
    public CreditApplicationRequest(Long affiliateId, Double requestedAmount, 
                                   Integer termMonths, Double interestRate) {
        this.affiliateId = affiliateId;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.interestRate = interestRate;
    }
    
    // Getters and Setters
    public Long getAffiliateId() { return affiliateId; }
    public void setAffiliateId(Long affiliateId) { this.affiliateId = affiliateId; }
    
    public Double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }
    
    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }
    
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }
}