package com.prueba.credit_application_service.domain.model;

import com.prueba.credit_application_service.domain.model.enums.RiskLevel;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * RiskEvaluation - DOMAIN MODEL (100% PURE JAVA)
 */
public class RiskEvaluation {
    private Long id;
    private CreditApplication creditApplication;
    private Integer score;
    private RiskLevel riskLevel;
    private String recommendation;
    private String evaluationMessage;
    private LocalDateTime evaluationDate;

    // Default constructor
    public RiskEvaluation() {
    }

    // Full constructor
    public RiskEvaluation(Long id, Integer score, RiskLevel riskLevel,
            String recommendation, String evaluationMessage,
            LocalDateTime evaluationDate) {
        this.id = id;
        this.score = score;
        this.riskLevel = riskLevel;
        this.recommendation = recommendation;
        this.evaluationMessage = evaluationMessage;
        this.evaluationDate = evaluationDate;
    }

    // BUSINESS LOGIC METHODS

    public boolean isApprovalRecommended() {
        return "APPROVED".equalsIgnoreCase(this.recommendation);
    }

    public boolean isLowRisk() {
        return RiskLevel.LOW.equals(this.riskLevel);
    }

    public boolean requiresManualReview() {
        return "REVIEW".equalsIgnoreCase(this.recommendation) ||
                RiskLevel.MEDIUM.equals(this.riskLevel);
    }

    public boolean isHighRisk() {
        return RiskLevel.HIGH.equals(this.riskLevel);
    }

    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CreditApplication getCreditApplication() {
        return creditApplication;
    }

    public void setCreditApplication(CreditApplication creditApplication) {
        this.creditApplication = creditApplication;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getEvaluationMessage() {
        return evaluationMessage;
    }

    public void setEvaluationMessage(String evaluationMessage) {
        this.evaluationMessage = evaluationMessage;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RiskEvaluation that = (RiskEvaluation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RiskEvaluation{" +
                "id=" + id +
                ", score=" + score +
                ", riskLevel=" + riskLevel +
                ", recommendation='" + recommendation + '\'' +
                '}';
    }
}