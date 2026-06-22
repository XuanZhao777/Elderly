package com.elderly.care.algorithm;

import com.elderly.care.entity.ElderlyInfo;
import com.elderly.care.repository.ElderlyInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐引擎
 * 根据标签、位置等多维度推荐相似老人
 */
@Component
public class RecommendationEngine {

    @Autowired
    private ElderlyInfoRepository elderlyRepository;

    /**
     * 推荐与指定老人相似的其他老人
     */
    public List<Map<String, Object>> recommendSimilarElderly(String elderlyId, int limit) {
        Optional<ElderlyInfo> targetElderly = elderlyRepository.findById(elderlyId);
        if (!targetElderly.isPresent()) {
            return new ArrayList<>();
        }

        ElderlyInfo target = targetElderly.get();
        List<ElderlyInfo> allElderly = elderlyRepository.findAll();

        return allElderly.stream()
                .filter(e -> !e.getId().equals(elderlyId) && e.getIsActive())
                .map(e -> {
                    double similarity = calculateSimilarity(target, e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("elderly", e);
                    result.put("similarity", similarity);
                    return result;
                })
                .sorted((a, b) -> Double.compare((Double) b.get("similarity"), (Double) a.get("similarity")))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 计算两个老人之间的相似度（0-1）
     */
    private double calculateSimilarity(ElderlyInfo elderly1, ElderlyInfo elderly2) {
        double tagSimilarity = calculateTagSimilarity(elderly1.getTags(), elderly2.getTags());
        double ageSimilarity = calculateAgeSimilarity(elderly1.getAge(), elderly2.getAge());
        double healthSimilarity = calculateHealthSimilarity(elderly1.getHealthStatus(), elderly2.getHealthStatus());
        double locationSimilarity = calculateLocationSimilarity(
                elderly1.getLocationLatitude(), elderly1.getLocationLongitude(),
                elderly2.getLocationLatitude(), elderly2.getLocationLongitude()
        );

        // 加权计算
        return (tagSimilarity * 0.4) +
               (ageSimilarity * 0.2) +
               (healthSimilarity * 0.2) +
               (locationSimilarity * 0.2);
    }

    /**
     * 计算标签相似度
     */
    private double calculateTagSimilarity(Set<String> tags1, Set<String> tags2) {
        if (tags1.isEmpty() && tags2.isEmpty()) return 1.0;
        if (tags1.isEmpty() || tags2.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(tags1);
        intersection.retainAll(tags2);
        Set<String> union = new HashSet<>(tags1);
        union.addAll(tags2);

        return (double) intersection.size() / union.size();
    }

    /**
     * 计算年龄相似度
     */
    private double calculateAgeSimilarity(Integer age1, Integer age2) {
        int ageDiff = Math.abs(age1 - age2);
        return Math.max(0, 1 - (ageDiff / 30.0));
    }

    /**
     * 计算健康状况相似度
     */
    private double calculateHealthSimilarity(ElderlyInfo.HealthStatus health1, ElderlyInfo.HealthStatus health2) {
        return health1.equals(health2) ? 1.0 : 0.5;
    }

    /**
     * 计算位置相似度
     */
    private double calculateLocationSimilarity(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // 地球半径（km）
        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = R * c;

        // 距离越近相似度越高，最多10km视为完全相似
        return Math.max(0, 1 - (distance / 10));
    }
}
