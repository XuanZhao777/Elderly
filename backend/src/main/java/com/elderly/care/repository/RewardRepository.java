package com.elderly.care.repository;

import com.elderly.care.entity.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, String> {
    
    @Query("SELECT r FROM Reward r WHERE r.isActive = true ORDER BY r.requiredPoints ASC")
    Page<Reward> findAllActive(Pageable pageable);
}
