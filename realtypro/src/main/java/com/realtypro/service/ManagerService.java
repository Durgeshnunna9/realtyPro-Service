package com.realtypro.service;

import com.realtypro.schema.Manager;
import com.realtypro.schema.User;
import com.realtypro.repository.ManagerRepository;
import com.realtypro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;

    // ➕ CREATE new manager
    public Manager createManager(Manager manager) {
        if (manager.getUser() == null || manager.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User reference is required");
        }

        User user = userRepository.findById(manager.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with ID: " + manager.getUser().getUserId()));

        manager.setUser(user);
        return managerRepository.save(manager);
    }

    // 📋 GET all managers
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    // 🔍 GET manager by ID
    public Manager getManagerById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with ID: " + id));
    }

    // ✏️ UPDATE manager (linked user)
    public Manager updateManager(Long id, Manager updatedManager) {
        Manager existing = managerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with ID: " + id));

        if (updatedManager.getUser() != null && updatedManager.getUser().getUserId() != null) {
            User user = userRepository.findById(updatedManager.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "User not found with ID: " + updatedManager.getUser().getUserId()));
            existing.setUser(user);
        }

        return managerRepository.save(existing);
    }

    // ❌ DELETE manager
    public void deleteManager(Long id) {
        if (!managerRepository.existsById(id)) {
            throw new IllegalArgumentException("Manager not found with ID: " + id);
        }
        managerRepository.deleteById(id);
    }
}
