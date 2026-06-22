package com.elderly.care.dto;

import com.elderly.care.entity.Points;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointsDto {
    private String userId;
    private Integer totalPoints;
    private Integer availablePoints;
    private Integer usedPoints;
    private Points.UserLevel level;
    private Integer currentRank;
    private Integer rankPercentage;
}
