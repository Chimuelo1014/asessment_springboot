package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

/**
 * RiskEvaluationResponse - INFRASTRUCTURE LAYER DTO
 */
public class RiskEvaluationResponse {
    private Integer score;
    private String riskLevel;
    private String recommendation;
    private String message;
    
    // Constructors
    public RiskEvaluationResponse() {}
    
    public RiskEvaluationResponse(Integer score, String riskLevel, 
                                 String recommendation, String message) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.recommendation = recommendation;
        this.message = message;
    }
    
    // Getters and Setters
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public String getRiskLevel() {
        return riskLevel;
    }
    
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
    
    public String getRecommendation() {
        return recommendation;
    }
    
    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
