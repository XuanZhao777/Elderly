package com.elderly.care.repository;

import com.elderly.care.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, String> {
    
    Optional<RiskAssessment> findByElderlyId(String elderlyId);
    
    @Query("SELECT r FROM RiskAssessment r WHERE r.riskLevel = :riskLevel ORDER BY r.riskScore DESC")
    List<RiskAssessment> findByRiskLevel(@Param("riskLevel") RiskAssessment.RiskLevel riskLevel);
}
