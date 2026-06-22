package com.elderly.care.service;

import com.elderly.care.dto.PointsDto;
import com.elderly.care.entity.Points;
import com.elderly.care.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PointsService {

    @Autowired
    private PointsRepository pointsRepository;

    public Points createPointsRecord(String userId) {
        Points points = Points.builder()
                .userId(userId)
                .totalPoints(0)
                .availablePoints(0)
                .usedPoints(0)
                .level(Points.UserLevel.BRONZE)
                .lastUpdated(LocalDateTime.now())
                .build();
        return pointsRepository.save(points);
    }

    public Points addPoints(String userId, Integer pointsToAdd) {
        Optional<Points> pointsOpt = pointsRepository.findByUserId(userId);
        if (pointsOpt.isPresent()) {
            Points points = pointsOpt.get();
            points.setTotalPoints(points.getTotalPoints() + pointsToAdd);
            points.setAvailablePoints(points.getAvailablePoints() + pointsToAdd);
            points.setLevel(calculateLevel(points.getTotalPoints()));
            points.setLastUpdated(LocalDateTime.now());
            return pointsRepository.save(points);
        }
        return null;
    }

    public Points deductPoints(String userId, Integer pointsToDeduct) {
        Optional<Points> pointsOpt = pointsRepository.findByUserId(userId);
        if (pointsOpt.isPresent()) {
            Points points = pointsOpt.get();
            if (points.getAvailablePoints() >= pointsToDeduct) {
                points.setAvailablePoints(points.getAvailablePoints() - pointsToDeduct);
                points.setUsedPoints(points.getUsedPoints() + pointsToDeduct);
                points.setLastUpdated(LocalDateTime.now());
                return pointsRepository.save(points);
            }
        }
        return null;
    }

    public Points getPointsByUserId(String userId) {
        return pointsRepository.findByUserId(userId).orElse(null);
    }

    public Page<Points> getLeaderboard(Pageable pageable) {
        return pointsRepository.getLeaderboard(pageable);
    }

    private Points.UserLevel calculateLevel(Integer totalPoints) {
        if (totalPoints >= 1000) return Points.UserLevel.PLATINUM;
        if (totalPoints >= 500) return Points.UserLevel.GOLD;
        if (totalPoints >= 200) return Points.UserLevel.SILVER;
        return Points.UserLevel.BRONZE;
    }

    public PointsDto convertToDto(Points points, Integer totalUsers) {
        Integer rankPercentage = totalUsers > 0 ? (100 - (points.getCurrentRank() * 100 / totalUsers)) : 0;
        return PointsDto.builder()
                .userId(points.getUserId())
                .totalPoints(points.getTotalPoints())
                .availablePoints(points.getAvailablePoints())
                .usedPoints(points.getUsedPoints())
                .level(points.getLevel())
                .currentRank(points.getCurrentRank())
                .rankPercentage(rankPercentage)
                .build();
    }
}
