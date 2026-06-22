package com.elderly.care.repository;

import com.elderly.care.entity.Points;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointsRepository extends JpaRepository<Points, String> {
    
    Optional<Points> findByUserId(String userId);
    
    @Query("SELECT p FROM Points p ORDER BY p.totalPoints DESC")
    Page<Points> getLeaderboard(Pageable pageable);
}
