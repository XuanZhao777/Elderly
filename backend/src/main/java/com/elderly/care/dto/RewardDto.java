package com.elderly.care.dto;

import com.elderly.care.entity.Reward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDto {
    private String id;
    private String name;
    private String description;
    private Integer requiredPoints;
    private Reward.RewardCategory category;
    private String imageUrl;
    private Integer stock;
    private Integer exchangeCount;
}
