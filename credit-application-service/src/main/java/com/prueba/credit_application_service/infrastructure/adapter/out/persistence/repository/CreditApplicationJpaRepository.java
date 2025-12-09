package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.repository;

import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditApplicationJpaRepository extends JpaRepository<CreditApplicationEntity, Long> {

    @EntityGraph(attributePaths = {"affiliate", "riskEvaluation"})
    @Query("SELECT ca FROM CreditApplicationEntity ca WHERE ca.id = :id")
    Optional<CreditApplicationEntity> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT ca FROM CreditApplicationEntity ca JOIN FETCH ca.affiliate WHERE ca.affiliate.id = :affiliateId")
    List<CreditApplicationEntity> findByAffiliateId(@Param("affiliateId") Long affiliateId);

    List<CreditApplicationEntity> findByStatus(CreditApplicationStatus status);

    @Query("SELECT ca FROM CreditApplicationEntity ca JOIN FETCH ca.affiliate WHERE ca.status = :status")
    List<CreditApplicationEntity> findByStatusWithAffiliate(@Param("status") CreditApplicationStatus status);

    @EntityGraph(attributePaths = {"affiliate", "riskEvaluation"})
    @Query("SELECT ca FROM CreditApplicationEntity ca")
    List<CreditApplicationEntity> findAllWithDetails();
}
