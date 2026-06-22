package com.elderly.care.service;

import com.elderly.care.dto.ElderlyDto;
import com.elderly.care.entity.ElderlyInfo;
import com.elderly.care.entity.FollowRelationship;
import com.elderly.care.repository.ElderlyInfoRepository;
import com.elderly.care.repository.FollowRelationshipRepository;
import com.elderly.care.repository.RiskAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ElderlyService {

    @Autowired
    private ElderlyInfoRepository elderlyRepository;

    @Autowired
    private FollowRelationshipRepository followRepository;

    @Autowired
    private RiskAssessmentRepository riskRepository;

    public Page<ElderlyInfo> getAllElderly(Pageable pageable) {
        return elderlyRepository.findAllActive(pageable);
    }

    public List<ElderlyInfo> getNearbyElderly(Double latitude, Double longitude, Double radiusKm) {
        return elderlyRepository.findNearby(latitude, longitude, radiusKm);
    }

    public Optional<ElderlyInfo> getElderlyById(String id) {
        return elderlyRepository.findById(id);
    }

    public Long countSupporters(String elderlyId) {
        return followRepository.countSupporters(elderlyId);
    }

    public ElderlyDto convertToDto(ElderlyInfo elderly, String currentUserId) {
        Long supportersCount = countSupporters(elderly.getId());
        Boolean isFollowed = false;
        if (currentUserId != null) {
            isFollowed = followRepository.findByVolunteerIdAndElderlyId(currentUserId, elderly.getId()).isPresent();
        }

        String riskLevel = "GREEN";
        Integer riskScore = 0;
        Optional<com.elderly.care.entity.RiskAssessment> assessment = riskRepository.findByElderlyId(elderly.getId());
        if (assessment.isPresent()) {
            riskLevel = assessment.get().getRiskLevel().name();
            riskScore = assessment.get().getRiskScore();
        }

        return ElderlyDto.builder()
                .id(elderly.getId())
                .name(elderly.getName())
                .age(elderly.getAge())
                .avatar(elderly.getAvatarUrl())
                .address(elderly.getAddress())
                .healthStatus(elderly.getHealthStatus())
                .disabilityLevel(elderly.getDisabilityLevel())
                .daysWithoutCompanion(elderly.getDaysWithoutCompanion())
                .supportersCount(supportersCount)
                .riskLevel(riskLevel)
                .riskScore(riskScore)
                .tags(elderly.getTags())
                .isFollowed(isFollowed)
                .build();
    }

    public Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Radius of the earth in km
        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
