package com.elderly.care.controller;

import com.elderly.care.dto.ApiResponse;
import com.elderly.care.dto.CheckInDto;
import com.elderly.care.entity.CheckIn;
import com.elderly.care.service.CheckInService;
import com.elderly.care.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/checkin")
@CrossOrigin(origins = "*")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private PointsService pointsService;

    /**
     * 开始打卡
     */
    @PostMapping("/start/{elderlyId}")
    public ApiResponse<CheckInDto> startCheckIn(
            @PathVariable String elderlyId,
            @RequestHeader String userId) {
        CheckIn checkIn = checkInService.createCheckIn(userId, elderlyId);
        return ApiResponse.created(checkInService.convertToDto(checkIn));
    }

    /**
     * 完成打卡
     */
    @PostMapping("/{checkInId}/complete")
    public ApiResponse<?> completeCheckIn(
            @PathVariable String checkInId,
            @RequestParam Integer durationMinutes,
            @RequestParam String feedback,
            @RequestHeader String userId) {
        CheckIn checkIn = checkInService.completeCheckIn(checkInId, durationMinutes, feedback);
        
        if (checkIn == null) {
            return ApiResponse.error(404, "Check-in not found");
        }

        // 添加积分
        pointsService.addPoints(userId, checkIn.getPointsEarned());

        Map<String, Object> response = new HashMap<>();
        response.put("checkInId", checkIn.getId());
        response.put("pointsEarned", checkIn.getPointsEarned());
        response.put("message", "感谢您的陪伴！您获得了" + checkIn.getPointsEarned() + "亲情值");

        return ApiResponse.created(response);
    }

    /**
     * 获取打卡历史
     */
    @GetMapping("/history")
    public ApiResponse<Page<CheckInDto>> getCheckInHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader String userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CheckIn> checkIns = checkInService.getVolunteerCheckInHistory(userId, pageable);
        Page<CheckInDto> dtos = checkIns.map(checkInService::convertToDto);
        
        return ApiResponse.success(dtos);
    }

    /**
     * 获取总志愿时长
     */
    @GetMapping("/hours")
    public ApiResponse<?> getTotalVolunteerHours(@RequestHeader String userId) {
        Integer hours = checkInService.getTotalVolunteerHours(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("totalHours", hours);
        
        return ApiResponse.success(response);
    }
}
