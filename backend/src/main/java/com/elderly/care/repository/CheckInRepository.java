package com.elderly.care.repository;

import com.elderly.care.entity.CheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, String> {
    
    @Query("SELECT c FROM CheckIn c WHERE c.volunteerId = :volunteerId ORDER BY c.createdAt DESC")
    Page<CheckIn> findByVolunteer(@Param("volunteerId") String volunteerId, Pageable pageable);
    
    @Query("SELECT c FROM CheckIn c WHERE c.elderlyId = :elderlyId ORDER BY c.createdAt DESC")
    Page<CheckIn> findByElderly(@Param("elderlyId") String elderlyId, Pageable pageable);
    
    @Query("SELECT SUM(c.durationMinutes) FROM CheckIn c WHERE c.volunteerId = :volunteerId")
    Integer getTotalVolunteerHours(@Param("volunteerId") String volunteerId);
}
