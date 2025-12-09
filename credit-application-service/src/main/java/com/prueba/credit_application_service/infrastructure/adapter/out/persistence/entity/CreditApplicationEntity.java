package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity;

import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "credit_applications", indexes = {
        @Index(name = "idx_credit_app_affiliate", columnList = "affiliate_id"),
        @Index(name = "idx_credit_app_status", columnList = "status"),
        @Index(name = "idx_credit_app_date", columnList = "application_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id", nullable = false)
    private AffiliateEntity affiliate;

    @Column(name = "requested_amount", nullable = false)
    private Double requestedAmount;

    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @Column(name = "interest_rate", nullable = false) // NUEVO
    private Double interestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CreditApplicationStatus status;

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "evaluation_date")
    private LocalDateTime evaluationDate;

    @Column(name = "analyst_comments", columnDefinition = "TEXT")
    private String analystComments;

    @OneToOne(mappedBy = "creditApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RiskEvaluationEntity riskEvaluation;

    @Version
    private Long version;
}