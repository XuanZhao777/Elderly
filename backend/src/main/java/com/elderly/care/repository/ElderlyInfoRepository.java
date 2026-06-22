package com.elderly.care.repository;

import com.elderly.care.entity.ElderlyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElderlyInfoRepository extends JpaRepository<ElderlyInfo, String> {
    
    @Query("SELECT e FROM ElderlyInfo e WHERE e.isActive = true ORDER BY e.createdAt DESC")
    Page<ElderlyInfo> findAllActive(Pageable pageable);
    
    @Query("SELECT e FROM ElderlyInfo e WHERE " +
           "SQRT(POWER(e.locationLatitude - :lat, 2) + POWER(e.locationLongitude - :lon, 2)) * 111 <= :radius " +
           "AND e.isActive = true ORDER BY e.daysWithoutCompanion DESC")
    List<ElderlyInfo> findNearby(@Param("lat") Double latitude, 
                                 @Param("lon") Double longitude, 
                                 @Param("radius") Double radiusKm);
    
    @Query("SELECT e FROM ElderlyInfo e WHERE e.age >= :minAge AND e.age <= :maxAge AND e.isActive = true")
    List<ElderlyInfo> findByAgeRange(@Param("minAge") Integer minAge, 
                                      @Param("maxAge") Integer maxAge);
}
