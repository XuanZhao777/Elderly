package com.elderly.care.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInDto {
    private String id;
    private String volunteerId;
    private String elderlyId;
    private String elderlyName;
    private Integer durationMinutes;
    private String feedback;
    private List<String> photoUrls;
    private Integer pointsEarned;
    private LocalDateTime createdAt;
}
