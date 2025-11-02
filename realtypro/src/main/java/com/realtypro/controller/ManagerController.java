package com.realtypro.controller;

import com.realtypro.repository.ManagerRepository;
import com.realtypro.repository.UserRepository;
import com.realtypro.schema.Manager;
import com.realtypro.schema.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/managers")
public class ManagerController {
    @Autowired
    private ManagerRepository managerRepository;
    
    @Autowired
    private UserRepository userRepository;

    // ➕ Create Manager
    @PostMapping("/create")
    public ResponseEntity<?> createManager(@RequestBody Manager manager) {
        try {
            // Check if user exists (FK reference)
            if (manager.getUser() != null) {
                Optional<User> userOpt = userRepository.findById(manager.getUser().getUserId());
                if (userOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("User not found with ID: " + manager.getUser().getUserId());
                }
                manager.setUser(userOpt.get());
            } else {
                return ResponseEntity.badRequest().body("User reference is required");
            }

            Manager savedManager = managerRepository.save(manager);
            return ResponseEntity.ok(savedManager);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving manager: " + e.getMessage());
        }
    }

    // 📋 Get all managers
    @GetMapping("/all")
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = managerRepository.findAll();
        if (managers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(managers);
    }

    // 🔍 Get manager by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getManagerById(@PathVariable Long id) {
        Optional<Manager> managerOpt = managerRepository.findById(id);
        return managerOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✏️ Update manager rating or linked user
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateManager(@PathVariable Long id, @RequestBody Manager updatedManager) {
        Optional<Manager> existingOpt = managerRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Manager existing = existingOpt.get();

        // Update linked user (if provided)
        if (updatedManager.getUser() != null) {
            Optional<User> userOpt = userRepository.findById(updatedManager.getUser().getUserId());
            userOpt.ifPresent(existing::setUser);
        }

        Manager saved = managerRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ❌ Delete manager
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable Long id) {
        if (!managerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        managerRepository.deleteById(id);
        return ResponseEntity.ok("Manager deleted successfully");
    }
}