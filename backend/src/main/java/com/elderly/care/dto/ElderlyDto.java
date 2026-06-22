package com.elderly.care.dto;

import com.elderly.care.entity.ElderlyInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElderlyDto {
    private String id;
    private String name;
    private Integer age;
    private String avatar;
    private String address;
    private ElderlyInfo.HealthStatus healthStatus;
    private Integer disabilityLevel;
    private Integer daysWithoutCompanion;
    private Long supportersCount;
    private String riskLevel; // RED, ORANGE, GREEN
    private Integer riskScore;
    private Double distance;
    private Set<String> tags;
    private Boolean isFollowed;
}
