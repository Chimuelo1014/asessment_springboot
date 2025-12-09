package com.prueba.credit_application_service.domain.model;

import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * CreditApplication - DOMAIN MODEL (100% PURE JAVA)
 * UPDATED: Added interestRate field as per requirements
 */
public class CreditApplication {
    private Long id;
    private Affiliate affiliate;
    private Double requestedAmount;
    private Integer termMonths;
    private Double interestRate; // NEW: Tasa propuesta
    private CreditApplicationStatus status;
    private LocalDateTime applicationDate;
    private LocalDateTime evaluationDate;
    private String analystComments;
    private RiskEvaluation riskEvaluation;

    public CreditApplication() {
    }

    public CreditApplication(Long id, Affiliate affiliate, Double requestedAmount,
            Integer termMonths, Double interestRate, CreditApplicationStatus status,
            LocalDateTime applicationDate) {
        this.id = id;
        this.affiliate = affiliate;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.interestRate = interestRate;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    /**
     * Calculates the monthly payment including interest
     * Using simple interest formula: P * (1 + r*t) / t
     */
    public Double calculateMonthlyPayment() {
        if (requestedAmount == null || termMonths == null || termMonths == 0) {
            return 0.0;
        }
        
        if (interestRate == null || interestRate == 0.0) {
            return requestedAmount / termMonths;
        }
        
        double monthlyRate = interestRate / 12 / 100;
        double totalAmount = requestedAmount * (1 + (monthlyRate * termMonths));
        
        return totalAmount / termMonths;
    }

    public boolean isPaymentAffordable(Double monthlyIncome, Double maxDebtRatio) {
        if (monthlyIncome == null || monthlyIncome == 0) {
            return false;
        }
        Double monthlyPayment = calculateMonthlyPayment();
        Double debtRatio = monthlyPayment / monthlyIncome;
        return debtRatio <= maxDebtRatio;
    }

    public boolean canBeEvaluated() {
        return CreditApplicationStatus.PENDING.equals(this.status) ||
                CreditApplicationStatus.UNDER_REVIEW.equals(this.status);
    }

    public void approve(String comments) {
        this.status = CreditApplicationStatus.APPROVED;
        this.evaluationDate = LocalDateTime.now();
        this.analystComments = comments;
    }

    public void reject(String comments) {
        this.status = CreditApplicationStatus.REJECTED;
        this.evaluationDate = LocalDateTime.now();
        this.analystComments = comments;
    }

    public void markAsUnderReview() {
        this.status = CreditApplicationStatus.UNDER_REVIEW;
    }

    // GETTERS AND SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Affiliate getAffiliate() { return affiliate; }
    public void setAffiliate(Affiliate affiliate) { this.affiliate = affiliate; }
    
    public Double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }
    
    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }
    
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }
    
    public CreditApplicationStatus getStatus() { return status; }
    public void setStatus(CreditApplicationStatus status) { this.status = status; }
    
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }
    
    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDateTime evaluationDate) { this.evaluationDate = evaluationDate; }
    
    public String getAnalystComments() { return analystComments; }
    public void setAnalystComments(String analystComments) { this.analystComments = analystComments; }
    
    public RiskEvaluation getRiskEvaluation() { return riskEvaluation; }
    public void setRiskEvaluation(RiskEvaluation riskEvaluation) { this.riskEvaluation = riskEvaluation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditApplication that = (CreditApplication) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CreditApplication{" +
                "id=" + id +
                ", requestedAmount=" + requestedAmount +
                ", termMonths=" + termMonths +
                ", interestRate=" + interestRate +
                ", status=" + status +
                '}';
    }
}