package com.elderly.care.service;

import com.elderly.care.dto.CheckInDto;
import com.elderly.care.entity.CheckIn;
import com.elderly.care.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class CheckInService {

    @Autowired
    private CheckInRepository checkInRepository;

    public CheckIn createCheckIn(String volunteerId, String elderlyId) {
        CheckIn checkIn = CheckIn.builder()
                .volunteerId(volunteerId)
                .elderlyId(elderlyId)
                .checkInTime(LocalDateTime.now())
                .pointsEarned(1)
                .build();
        return checkInRepository.save(checkIn);
    }

    public CheckIn completeCheckIn(String checkInId, Integer durationMinutes, String feedback) {
        Optional<CheckIn> checkIn = checkInRepository.findById(checkInId);
        if (checkIn.isPresent()) {
            CheckIn existing = checkIn.get();
            existing.setCheckOutTime(LocalDateTime.now());
            existing.setDurationMinutes(durationMinutes);
            existing.setFeedback(feedback);
            return checkInRepository.save(existing);
        }
        return null;
    }

    public Page<CheckIn> getVolunteerCheckInHistory(String volunteerId, Pageable pageable) {
        return checkInRepository.findByVolunteer(volunteerId, pageable);
    }

    public Integer getTotalVolunteerHours(String volunteerId) {
        Integer totalMinutes = checkInRepository.getTotalVolunteerHours(volunteerId);
        return totalMinutes != null ? totalMinutes / 60 : 0;
    }

    public CheckInDto convertToDto(CheckIn checkIn) {
        return CheckInDto.builder()
                .id(checkIn.getId())
                .volunteerId(checkIn.getVolunteerId())
                .elderlyId(checkIn.getElderlyId())
                .durationMinutes(checkIn.getDurationMinutes())
                .feedback(checkIn.getFeedback())
                .pointsEarned(checkIn.getPointsEarned())
                .createdAt(checkIn.getCreatedAt())
                .build();
    }
}
