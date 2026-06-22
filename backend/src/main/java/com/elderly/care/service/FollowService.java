package com.elderly.care.service;

import com.elderly.care.entity.FollowRelationship;
import com.elderly.care.repository.FollowRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    @Autowired
    private FollowRelationshipRepository followRepository;

    public FollowRelationship followElderly(String volunteerId, String elderlyId) {
        Optional<FollowRelationship> existing = followRepository.findByVolunteerIdAndElderlyId(volunteerId, elderlyId);
        
        if (existing.isPresent()) {
            FollowRelationship follow = existing.get();
            follow.setIsActive(true);
            return followRepository.save(follow);
        }

        FollowRelationship follow = FollowRelationship.builder()
                .volunteerId(volunteerId)
                .elderlyId(elderlyId)
                .isActive(true)
                .build();
        return followRepository.save(follow);
    }

    public void unfollowElderly(String volunteerId, String elderlyId) {
        Optional<FollowRelationship> follow = followRepository.findByVolunteerIdAndElderlyId(volunteerId, elderlyId);
        if (follow.isPresent()) {
            FollowRelationship relationship = follow.get();
            relationship.setIsActive(false);
            followRepository.save(relationship);
        }
    }

    public List<FollowRelationship> getFollowingElderly(String volunteerId) {
        return followRepository.findFollowingByVolunteer(volunteerId);
    }

    public Boolean isFollowing(String volunteerId, String elderlyId) {
        return followRepository.findByVolunteerIdAndElderlyId(volunteerId, elderlyId)
                .map(FollowRelationship::getIsActive)
                .orElse(false);
    }
}
