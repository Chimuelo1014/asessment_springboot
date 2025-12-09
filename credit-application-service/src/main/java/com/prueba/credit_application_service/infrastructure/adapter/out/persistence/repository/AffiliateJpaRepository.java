package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.repository;

import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliateJpaRepository extends JpaRepository<AffiliateEntity, Long> {
    
    Optional<AffiliateEntity> findByDocument(String document);
    
    boolean existsByDocument(String document);
    
    @Query("SELECT a FROM AffiliateEntity a WHERE a.status = 'ACTIVE'")
    List<AffiliateEntity> findAllActive();
}
