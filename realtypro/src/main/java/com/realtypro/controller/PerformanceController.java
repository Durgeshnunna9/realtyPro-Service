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
@CrossOrigin(origins = "http://localhost:8080") // adjust as needed
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    // Get performance by userId (role determines behavior)
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPerformanceByUser(@PathVariable Long userId) {
        try {
            // Delegate logic to the service
            UserPerformanceResponse response = performanceService.getPerformanceByUser(userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
}

