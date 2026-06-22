package com.elderly.care.algorithm;

import com.elderly.care.entity.CheckIn;
import com.elderly.care.entity.ElderlyInfo;
import com.elderly.care.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据分析引擎
 * 提供老人多维度统计分析
 */
@Component
public class AnalyticsEngine {

    @Autowired
    private CheckInRepository checkInRepository;

    /**
     * 分析老人的访问频率
     */
    public Map<String, Object> analyzeVisitFrequency(String elderlyId) {
        Map<String, Object> analysis = new HashMap<>();
        
        // 获取最近30次访问
        Page<CheckIn> recentVisits = checkInRepository.findByElderly(elderlyId, PageRequest.of(0, 30));
        
        long totalVisits = recentVisits.getTotalElements();
        double avgVisitsPerWeek = totalVisits / 4.0; // 简化计算
        
        String trend = "STABLE";
        if (avgVisitsPerWeek > 3) {
            trend = "UP";
        } else if (avgVisitsPerWeek < 1) {
            trend = "DOWN";
        }
        
        analysis.put("totalVisits", totalVisits);
        analysis.put("averageVisitsPerWeek", Math.round(avgVisitsPerWeek * 100) / 100.0);
        analysis.put("trend", trend);
        
        return analysis;
    }

    /**
     * 分析老人的志愿者多样性
     */
    public Map<String, Object> analyzeVolunteerDiversity(String elderlyId) {
        Map<String, Object> analysis = new HashMap<>();
        
        Page<CheckIn> allVisits = checkInRepository.findByElderly(elderlyId, PageRequest.of(0, 1000));
        
        Set<String> uniqueVolunteers = new HashSet<>();
        for (CheckIn visit : allVisits) {
            uniqueVolunteers.add(visit.getVolunteerId());
        }
        
        analysis.put("uniqueVolunteers", uniqueVolunteers.size());
        analysis.put("description", generateDiversityDescription(uniqueVolunteers.size()));
        
        return analysis;
    }

    /**
     * 计算老人的关怀指数
     */
    public Integer calculateCareIndex(ElderlyInfo elderly) {
        int baseScore = 50;
        
        // 根据健康状况调整
        switch (elderly.getHealthStatus()) {
            case EXCELLENT:
                baseScore += 10;
                break;
            case GOOD:
                baseScore += 5;
                break;
            case POOR:
                baseScore -= 10;
                break;
        }
        
        // 根据无人陪伴天数调整
        if (elderly.getDaysWithoutCompanion() != null) {
            baseScore -= (elderly.getDaysWithoutCompanion() / 2);
        }
        
        // 根据残疾程度调整
        baseScore -= (elderly.getDisabilityLevel() / 5);
        
        return Math.max(0, Math.min(100, baseScore));
    }

    /**
     * 生成多样性描述
     */
    private String generateDiversityDescription(int volunteers) {
        if (volunteers >= 10) return "非常高 - 有多位志愿者关怀";
        if (volunteers >= 5) return "高 - 有多位稳定的志愿者";
        if (volunteers >= 2) return "中等 - 有少数志愿者";
        if (volunteers >= 1) return "低 - 只有一位志愿者";
        return "无 - 尚无志愿者关怀";
    }
}
