package com.realtypro.controller;

import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.SaleRepository;
import com.realtypro.repository.TaskRepository;
import com.realtypro.repository.UserRepository;
import com.realtypro.schema.Property;
import com.realtypro.schema.User;
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
    private UserRepository userRepository;

    @Autowired
    private SaleRepository saleRepository;

    @GetMapping("/stats/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // 1️⃣ Get user and their role
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long propertyCount = 0;
        long pendingTasks = 0;
        Double totalSales = 0.0;

        // 2️⃣ Use role to determine which queries to run
        switch (user.getRole()) {
            case AGENT:
                propertyCount = propertyRepository.countByAgentUserId(userId);
                pendingTasks = taskRepository.countByAgentUserIdAndStatusNot(userId, "COMPLETED");
                totalSales = saleRepository.sumSalesByAgent(userId);
                break;

            case MANAGER:
                propertyCount = propertyRepository.countByManagerUserId(userId);
                pendingTasks = taskRepository.countByManagerUserIdAndStatusNot(userId, "COMPLETED");
                totalSales = saleRepository.sumSalesByManager(userId);
                break;

            case ADMIN:
                // optionally: aggregate everything for admin
                propertyCount = propertyRepository.count();
                pendingTasks = taskRepository.count();
                totalSales = saleRepository.sumSalesByManager(userId); // or total system sales
                break;
        }

        stats.put("listings", propertyCount);
        stats.put("pendingTasks", pendingTasks);
        stats.put("totalSales", totalSales != null ? totalSales : 0.0);

        return ResponseEntity.ok(stats);
    }
}
