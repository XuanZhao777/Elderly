package com.elderly.care.repository;

import com.elderly.care.entity.FollowRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, String> {
    
    Optional<FollowRelationship> findByVolunteerIdAndElderlyId(String volunteerId, String elderlyId);
    
    @Query("SELECT f FROM FollowRelationship f WHERE f.volunteerId = :volunteerId AND f.isActive = true")
    List<FollowRelationship> findFollowingByVolunteer(@Param("volunteerId") String volunteerId);
    
    @Query("SELECT COUNT(f) FROM FollowRelationship f WHERE f.elderlyId = :elderlyId AND f.isActive = true")
    Long countSupporters(@Param("elderlyId") String elderlyId);
}
