package com.realtypro.controller;

import com.realtypro.dto.PerformanceDTO;
import com.realtypro.dto.UserPerformanceResponse;
import com.realtypro.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/performance")
@CrossOrigin(origins = "http://localhost:8080")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    // 🟢 1. Core endpoint — your existing service logic
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPerformanceByUser(@PathVariable Long userId) {
        try {
            UserPerformanceResponse response = performanceService.getPerformanceByUser(userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong fetching performance data"));
        }
    }

    // 🟢 2. Optional — chart data (monthly performance)
    @GetMapping("/user/{userId}/monthly")
    public ResponseEntity<?> getMonthlyPerformance(@PathVariable Long userId) {
        try {
            // for now: mock data — can replace with DB aggregation later
            List<PerformanceDTO> monthlyStats = List.of(
                    new PerformanceDTO("January", 4, 50000, 2500),
                    new PerformanceDTO("February", 6, 65000, 3200),
                    new PerformanceDTO("March", 3, 40000, 2000)
            );
            return ResponseEntity.ok(monthlyStats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Couldn’t fetch monthly stats"));
        }
    }
}
