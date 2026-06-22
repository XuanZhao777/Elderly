package com.elderly.care.algorithm;

import com.elderly.care.entity.ElderlyInfo;
import com.elderly.care.entity.RiskAssessment;
import com.elderly.care.repository.FollowRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 老人风险评估算法
 * 根据多个维度计算老人的风险等级
 */
@Component
public class RiskAssessmentAlgorithm {

    @Autowired
    private FollowRelationshipRepository followRepository;

    /**
     * 计算老人的综合风险分数
     */
    public RiskAssessment calculateRiskScore(ElderlyInfo elderly) {
        // 权重配置
        final double AGE_WEIGHT = 0.20;
        final double HEALTH_WEIGHT = 0.20;
        final double COMPANION_WEIGHT = 0.25;
        final double SUPPORTER_WEIGHT = 0.20;
        final double DISABILITY_WEIGHT = 0.15;

        // 计算各个因子
        BigDecimal ageFactor = calculateAgeFactor(elderly.getAge());
        BigDecimal healthFactor = calculateHealthFactor(elderly.getHealthStatus());
        BigDecimal companionFactor = calculateCompanionFactor(elderly.getDaysWithoutCompanion());
        BigDecimal supporterFactor = calculateSupporterFactor(elderly.getId());
        BigDecimal disabilityFactor = new BigDecimal(elderly.getDisabilityLevel());

        // 加权计算总风险分数
        Double riskScore = (ageFactor.doubleValue() * AGE_WEIGHT) +
                          (healthFactor.doubleValue() * HEALTH_WEIGHT) +
                          (companionFactor.doubleValue() * COMPANION_WEIGHT) +
                          (supporterFactor.doubleValue() * SUPPORTER_WEIGHT) +
                          (disabilityFactor.doubleValue() * DISABILITY_WEIGHT);

        riskScore = Math.min(100, Math.max(0, riskScore)); // 限制在0-100之间

        // 确定风险等级
        RiskAssessment.RiskLevel riskLevel = determineRiskLevel(riskScore);

        return RiskAssessment.builder()
                .elderlyId(elderly.getId())
                .riskScore(riskScore.intValue())
                .riskLevel(riskLevel)
                .ageFactor(ageFactor)
                .healthFactor(healthFactor)
                .companionFactor(companionFactor)
                .supporterFactor(supporterFactor)
                .disabilityFactor(disabilityFactor)
                .lastAssessedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 计算年龄因子（0-100）
     * 年龄越大，风险越高
     */
    private BigDecimal calculateAgeFactor(Integer age) {
        if (age < 60) return new BigDecimal(0);
        if (age < 70) return new BigDecimal(20);
        if (age < 80) return new BigDecimal(50);
        if (age < 90) return new BigDecimal(75);
        return new BigDecimal(100);
    }

    /**
     * 计算健康因子（0-100）
     */
    private BigDecimal calculateHealthFactor(ElderlyInfo.HealthStatus healthStatus) {
        switch (healthStatus) {
            case EXCELLENT:
                return new BigDecimal(0);
            case GOOD:
                return new BigDecimal(25);
            case FAIR:
                return new BigDecimal(60);
            case POOR:
                return new BigDecimal(100);
            default:
                return new BigDecimal(50);
        }
    }

    /**
     * 计算无人陪伴因子（0-100）
     * 无人陪伴天数越多，风险越高
     */
    private BigDecimal calculateCompanionFactor(Integer daysWithoutCompanion) {
        if (daysWithoutCompanion == null) daysWithoutCompanion = 0;
        
        if (daysWithoutCompanion < 3) return new BigDecimal(0);
        if (daysWithoutCompanion < 7) return new BigDecimal(30);
        if (daysWithoutCompanion < 14) return new BigDecimal(60);
        if (daysWithoutCompanion < 30) return new BigDecimal(80);
        return new BigDecimal(100);
    }

    /**
     * 计算支持者因子（0-100）
     * 支持者越多，风险越低
     */
    private BigDecimal calculateSupporterFactor(String elderlyId) {
        Long supportersCount = followRepository.countSupporters(elderlyId);
        
        if (supportersCount >= 5) return new BigDecimal(0);
        if (supportersCount >= 3) return new BigDecimal(25);
        if (supportersCount >= 1) return new BigDecimal(50);
        return new BigDecimal(100);
    }

    /**
     * 根据风险分数确定风险等级
     */
    private RiskAssessment.RiskLevel determineRiskLevel(Double riskScore) {
        if (riskScore < 40) return RiskAssessment.RiskLevel.GREEN;
        if (riskScore < 70) return RiskAssessment.RiskLevel.ORANGE;
        return RiskAssessment.RiskLevel.RED;
    }
}
