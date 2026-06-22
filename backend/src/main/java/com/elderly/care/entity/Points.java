package com.elderly.care.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Points {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "total_points")
    private Integer totalPoints = 0;

    @Column(name = "available_points")
    private Integer availablePoints = 0;

    @Column(name = "used_points")
    private Integer usedPoints = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private UserLevel level = UserLevel.BRONZE;

    @Column(name = "current_rank")
    private Integer currentRank;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public enum UserLevel {
        BRONZE, SILVER, GOLD, PLATINUM
    }
}
