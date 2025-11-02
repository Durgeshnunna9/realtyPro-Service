package com.realtypro.controller;

import com.realtypro.dto.PerformanceDTO;
import com.realtypro.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
        import java.util.List;

@RestController
@RequestMapping("/api/performance")
@CrossOrigin(origins = "http://localhost:8080") // or your React app URL
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/agent/{agentId}")
    public List<PerformanceDTO> getPerformanceByAgent(@PathVariable Long agentId) {
        return performanceService.getAgentPerformance(agentId);
    }
}
