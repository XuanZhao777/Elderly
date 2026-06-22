package com.elderly.care.controller;

import com.elderly.care.dto.ApiResponse;
import com.elderly.care.dto.ElderlyDto;
import com.elderly.care.entity.ElderlyInfo;
import com.elderly.care.service.ElderlyService;
import com.elderly.care.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/elderly")
@CrossOrigin(origins = "*")
public class ElderlyController {

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private FollowService followService;

    /**
     * 获取老人列表（带分页）
     */
    @GetMapping
    public ApiResponse<Page<ElderlyDto>> getElderly(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(required = false) String userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ElderlyInfo> elderlyPage = elderlyService.getAllElderly(pageable);
        
        Page<ElderlyDto> dtoPage = elderlyPage.map(elderly -> 
                elderlyService.convertToDto(elderly, userId)
        );
        
        return ApiResponse.success(dtoPage);
    }

    /**
     * 获取附近的老人
     */
    @GetMapping("/nearby")
    public ApiResponse<List<ElderlyDto>> getNearbyElderly(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10") Double radius,
            @RequestHeader(required = false) String userId) {
        List<ElderlyInfo> nearby = elderlyService.getNearbyElderly(latitude, longitude, radius);
        List<ElderlyDto> dtos = nearby.stream()
                .map(e -> {
                    ElderlyDto dto = elderlyService.convertToDto(e, userId);
                    dto.setDistance(elderlyService.calculateDistance(latitude, longitude, 
                            e.getLocationLatitude(), e.getLocationLongitude()));
                    return dto;
                })
                .collect(Collectors.toList());
        
        return ApiResponse.success(dtos);
    }

    /**
     * 获取老人详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ElderlyDto> getElderly(
            @PathVariable String id,
            @RequestHeader(required = false) String userId) {
        Optional<ElderlyInfo> elderly = elderlyService.getElderlyById(id);
        if (elderly.isPresent()) {
            return ApiResponse.success(elderlyService.convertToDto(elderly.get(), userId));
        }
        return ApiResponse.error(404, "Elderly not found");
    }

    /**
     * 关注老人
     */
    @PostMapping("/{id}/follow")
    public ApiResponse<?> followElderly(
            @PathVariable String id,
            @RequestHeader String userId) {
        followService.followElderly(userId, id);
        return ApiResponse.created("Successfully followed");
    }

    /**
     * 取消关注老人
     */
    @DeleteMapping("/{id}/follow")
    public ApiResponse<?> unfollowElderly(
            @PathVariable String id,
            @RequestHeader String userId) {
        followService.unfollowElderly(userId, id);
        return ApiResponse.success("Successfully unfollowed");
    }
}
