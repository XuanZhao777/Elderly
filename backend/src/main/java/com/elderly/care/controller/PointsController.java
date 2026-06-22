package com.elderly.care.controller;

import com.elderly.care.dto.ApiResponse;
import com.elderly.care.dto.PointsDto;
import com.elderly.care.entity.Points;
import com.elderly.care.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/points")
@CrossOrigin(origins = "*")
public class PointsController {

    @Autowired
    private PointsService pointsService;

    /**
     * 获取用户积分信息
     */
    @GetMapping("/me")
    public ApiResponse<PointsDto> getMyPoints(@RequestHeader String userId) {
        Points points = pointsService.getPointsByUserId(userId);
        
        if (points == null) {
            points = pointsService.createPointsRecord(userId);
        }
        
        PointsDto dto = pointsService.convertToDto(points, 1000); // 假设总用户数为1000
        return ApiResponse.success(dto);
    }

    /**
     * 获取排行榜
     */
    @GetMapping("/leaderboard")
    public ApiResponse<Page<PointsDto>> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Points> leaderboard = pointsService.getLeaderboard(pageable);
        
        Page<PointsDto> dtos = leaderboard.map(p -> pointsService.convertToDto(p, 1000));
        return ApiResponse.success(dtos);
    }
}
