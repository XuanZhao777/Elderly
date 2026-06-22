package com.elderly.care.repository;

import com.elderly.care.entity.Moment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MomentRepository extends JpaRepository<Moment, String> {
    
    @Query("SELECT m FROM Moment m WHERE m.userId = :userId ORDER BY m.createdAt DESC")
    Page<Moment> findByUser(@Param("userId") String userId, Pageable pageable);
}
