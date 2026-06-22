package com.elderly.care.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskAssessment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "elderly_id", unique = true, nullable = false)
    private String elderlyId;

    @Column(name = "risk_score", nullable = false)
    private Integer riskScore; // 0-100

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel; // GREEN, ORANGE, RED

    @Column(name = "age_factor")
    private BigDecimal ageFactor;

    @Column(name = "health_factor")
    private BigDecimal healthFactor;

    @Column(name = "companion_factor")
    private BigDecimal companionFactor;

    @Column(name = "supporter_factor")
    private BigDecimal supporterFactor;

    @Column(name = "disability_factor")
    private BigDecimal disabilityFactor;

    @Column(name = "last_assessed_at")
    private LocalDateTime lastAssessedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastAssessedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastAssessedAt = LocalDateTime.now();
    }

    public enum RiskLevel {
        GREEN, ORANGE, RED
    }
}
