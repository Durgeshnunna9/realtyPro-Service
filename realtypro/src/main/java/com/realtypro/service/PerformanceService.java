package com.realtypro.service;

import com.realtypro.dto.PerformanceDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceService {

    public List<PerformanceDTO> getAgentPerformance(Long agentId) {
        // TODO: Replace with actual DB queries (e.g. salesRepo.findByAgentId(agentId))
        // For now, returning mock data for graph testing

        List<PerformanceDTO> performance = new ArrayList<>();
        performance.add(new PerformanceDTO("Jan", 4, 120000, 6000));
        performance.add(new PerformanceDTO("Feb", 6, 180000, 9000));
        performance.add(new PerformanceDTO("Mar", 5, 150000, 7500));
        performance.add(new PerformanceDTO("Apr", 8, 240000, 12000));
        performance.add(new PerformanceDTO("May", 10, 300000, 15000));

        return performance;
    }
}
