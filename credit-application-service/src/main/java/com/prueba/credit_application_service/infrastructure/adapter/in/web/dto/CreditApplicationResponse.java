package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

public class CreditApplicationResponse {
    private Long id;
    private Long affiliateId;
    private String affiliateName;
    private Double requestedAmount;
    private Integer termMonths;
    private Double interestRate; // NUEVO
    private String status;
    private String applicationDate;
    private String evaluationDate;
    private String analystComments;
    private RiskEvaluationResponse riskEvaluation;
    
    public CreditApplicationResponse() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getAffiliateId() { return affiliateId; }
    public void setAffiliateId(Long affiliateId) { this.affiliateId = affiliateId; }
    
    public String getAffiliateName() { return affiliateName; }
    public void setAffiliateName(String affiliateName) { this.affiliateName = affiliateName; }
    
    public Double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }
    
    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }
    
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getApplicationDate() { return applicationDate; }
    public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }
    
    public String getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(String evaluationDate) { this.evaluationDate = evaluationDate; }
    
    public String getAnalystComments() { return analystComments; }
    public void setAnalystComments(String analystComments) { this.analystComments = analystComments; }
    
    public RiskEvaluationResponse getRiskEvaluation() { return riskEvaluation; }
    public void setRiskEvaluation(RiskEvaluationResponse riskEvaluation) { this.riskEvaluation = riskEvaluation; }
}