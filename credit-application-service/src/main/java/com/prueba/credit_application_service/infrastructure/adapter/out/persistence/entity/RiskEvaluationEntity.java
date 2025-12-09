package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity;

import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.domain.model.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "risk_evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_application_id", nullable = false, unique = true)
    private CreditApplicationEntity creditApplication;

    @Column(nullable = false)
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Column(length = 50)
    private String recommendation;

    @Column(name = "evaluation_message", columnDefinition = "TEXT")
    private String evaluationMessage;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDateTime evaluationDate;
}
