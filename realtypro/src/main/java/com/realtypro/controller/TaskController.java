package com.realtypro.controller;

import com.realtypro.repository.TaskRepository;
import com.realtypro.repository.UserRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.schema.Task;
import com.realtypro.schema.Property;
import com.realtypro.schema.User;

import com.realtypro.service.TaskService;
import com.realtypro.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ CREATE TASK
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
            // ✅ Validate Agent (must exist + must have role = AGENT)
            User agent = userRepository.findById(task.getAgent().getUserId())
                    .filter(u -> u.getRole() == Role.AGENT)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Agent ID or Role"));

            // ✅ Validate Manager (must exist + must have role = MANAGER)
            User manager = userRepository.findById(task.getManager().getUserId())
                    .filter(u -> u.getRole() == Role.MANAGER)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Manager ID or Role"));

            // ✅ Validate Property
            Property property = propertyRepository.findById(task.getProperty().getPropertyId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Property ID"));

            // ✅ Assign verified relationships
            task.setAgent(agent);
            task.setManager(manager);
            task.setProperty(property);
            task.setAssignedDate(LocalDate.now());

            Task savedTask = taskRepository.save(task);
            return ResponseEntity.ok(savedTask);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving task: " + e.getMessage());
        }
    }

    // ✅ GET TASKS BY AGENT (filtered from user table)
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<?> getTasksByAgent(@PathVariable Long agentId) {
        Optional<User> agentOpt = userRepository.findById(agentId);
        if (agentOpt.isEmpty() || !(agentOpt.get().getRole() == Role.AGENT)) {
            return ResponseEntity.status(404).body(Map.of("error", "Agent not found"));
        }

        List<Task> tasks = taskRepository.findByAgent(agentOpt.get());
        if (tasks.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Sort by property ID descending (optional)
        tasks.sort((a, b) -> Long.compare(
                b.getProperty().getPropertyId(),
                a.getProperty().getPropertyId()
        ));

        return ResponseEntity.ok(tasks);
    }

    // ✅ GET ALL TASKS
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tasks);
    }

    // ✅ GET SINGLE TASK
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        taskService.updateTaskStatus(taskId, newStatus);
        return ResponseEntity.ok(Map.of("message", "Task status updated successfully"));
    }

    // ✅ UPDATE TASK
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTaskName(updatedTask.getTaskName());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setAssignedDate(updatedTask.getAssignedDate());

            // Optionally update relationships
            if (updatedTask.getAgent() != null) {
                userRepository.findById(updatedTask.getAgent().getUserId())
                        .filter(user -> user.getRole() == Role.AGENT)
                        .ifPresent(existingTask::setAgent);
            }
            if (updatedTask.getManager() != null) {
                userRepository.findById(updatedTask.getManager().getUserId())
                        .filter(user -> user.getRole() == Role.MANAGER)
                        .ifPresent(existingTask::setManager);
            }
            if (updatedTask.getProperty() != null) {
                propertyRepository.findById(updatedTask.getProperty().getPropertyId())
                        .ifPresent(existingTask::setProperty);
            }

            Task saved = taskRepository.save(existingTask);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE TASK
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted successfully.");
    }
}
