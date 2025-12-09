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
@Table(name = "affiliates", indexes = {
        @Index(name = "idx_affiliate_document", columnList = "document", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String document;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "monthly_salary", nullable = false)
    private Double monthlySalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AffiliateStatus status;

    @Column(name = "affiliation_date", nullable = false)
    private LocalDate affiliationDate;

    @OneToMany(mappedBy = "affiliate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CreditApplicationEntity> creditApplications = new ArrayList<>();

    @Version
    private Long version;
}
