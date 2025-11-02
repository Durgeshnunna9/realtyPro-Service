package com.realtypro.controller;

import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.SaleRepository;
import com.realtypro.repository.TaskRepository;
import com.realtypro.schema.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:8080") // or your frontend port
public class DashboardController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SaleRepository saleRepository;

    @GetMapping("/stats/{agentId}")
    public ResponseEntity<Map<String, Object>> getAgentStats(@PathVariable Long agentId) {
        Map<String, Object> stats = new HashMap<>();

        long propertyCount = propertyRepository.countByAgentAgentId(agentId);
        long pendingTasks = taskRepository.countByAgentAgentIdAndStatusNot(agentId, "completed");
        Double totalSales = saleRepository.sumSalesByAgent(agentId);

        stats.put("listings", propertyCount);
        stats.put("pendingTasks", pendingTasks);
        stats.put("totalSales", totalSales != null ? totalSales : 0.0);

        return ResponseEntity.ok(stats);
    }
//    @GetMapping("/agent/{agentId}/latest-properties")
//    public ResponseEntity<List<Property>> getLatestProperties(@PathVariable Long agentId) {
//        List<Property> properties = propertyRepository.findTop5ByAgentAgentIdOrderByCreatedAtDesc(agentId);
//        return ResponseEntity.ok(properties);
//    }

}
