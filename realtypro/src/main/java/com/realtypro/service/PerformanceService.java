package com.realtypro.service;

import com.realtypro.dto.UserPerformanceResponse;
import com.realtypro.schema.User;
import com.realtypro.utilities.Role;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.TaskRepository;
import com.realtypro.repository.SaleRepository;
import com.realtypro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SaleRepository saleRepository;

    public UserPerformanceResponse getPerformanceByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Role role = user.getRole();
        UserPerformanceResponse response = new UserPerformanceResponse();

        response.setUserId(user.getUserId());
        response.setRole(role.name());
        response.setFullName(user.getFirstName() + " " + user.getLastName());

        // ✅ Role-based performance calculation
        if (role == Role.AGENT) {
            long propertyCount = propertyRepository.countByAgentUserId(userId);
            long pendingTasks = taskRepository.countByAgentUserIdAndStatusNot(userId, "completed");
            Double totalSales = saleRepository.sumSalesByAgent(userId);

            response.setListings(propertyCount);
            response.setPendingTasks(pendingTasks);
            response.setTotalSales(totalSales != null ? totalSales : 0.0);
        }
        else if (role == Role.MANAGER) {
            long managedProperties = propertyRepository.countByManagerUserId(userId);
            long pendingTasks = taskRepository.countByManagerUserIdAndStatusNot(userId, "completed");
            Double totalSales = saleRepository.sumSalesByManager(userId); // optional

            response.setManagedProperties(managedProperties);
            response.setPendingTasks(pendingTasks);
            response.setTotalSales(totalSales != null ? totalSales : 0.0);
        }
        else {
            throw new IllegalArgumentException("Unsupported role for performance view: " + role);
        }

        return response;
    }
}
