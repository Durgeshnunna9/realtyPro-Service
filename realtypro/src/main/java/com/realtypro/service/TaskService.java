package com.realtypro.service;

import com.realtypro.schema.Task;
import com.realtypro.schema.User;
import com.realtypro.schema.Property;
import com.realtypro.repository.TaskRepository;
import com.realtypro.repository.UserRepository;
import com.realtypro.repository.PropertyRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ CREATE TASK
    public Task createTask(Task task) {
        // 🧩 Validate Agent (User with role AGENT)
        if (task.getAgent() == null || task.getAgent().getUserId() == null) {
            throw new IllegalArgumentException("Agent reference is required");
        }

        User agent = userRepository.findById(task.getAgent().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Agent not found with ID: " + task.getAgent().getUserId()));

        if (!"AGENT".equalsIgnoreCase(agent.getRole().name())) {
            throw new IllegalArgumentException("User is not assigned as AGENT");
        }

        // 🧩 Validate Manager (User with role MANAGER)
        if (task.getManager() == null || task.getManager().getUserId() == null) {
            throw new IllegalArgumentException("Manager reference is required");
        }

        User manager = userRepository.findById(task.getManager().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with ID: " + task.getManager().getUserId()));

        if (!"MANAGER".equalsIgnoreCase(manager.getRole().name())) {
            throw new IllegalArgumentException("User is not assigned as MANAGER");
        }

        // 🧩 Validate Property
        Property property = propertyRepository.findById(task.getProperty().getPropertyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Property ID: " + task.getProperty().getPropertyId()));

        // ✅ Assign validated references
        task.setAgent(agent);
        task.setManager(manager);
        task.setProperty(property);

        return taskRepository.save(task);
    }

    @Transactional
    public void updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setStatus(status);
        taskRepository.save(task);
    }

    // ✅ READ ALL TASKS
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // ✅ READ ONE TASK
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // ✅ UPDATE TASK
    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTaskName(updatedTask.getTaskName());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());

            // 🔄 Update Agent
            if (updatedTask.getAgent() != null && updatedTask.getAgent().getUserId() != null) {
                userRepository.findById(updatedTask.getAgent().getUserId())
                        .filter(u -> "AGENT".equalsIgnoreCase(u.getRole().name()))
                        .ifPresent(existingTask::setAgent);
            }

            // 🔄 Update Manager
            if (updatedTask.getManager() != null && updatedTask.getManager().getUserId() != null) {
                userRepository.findById(updatedTask.getManager().getUserId())
                        .filter(u -> "MANAGER".equalsIgnoreCase(u.getRole().name()))
                        .ifPresent(existingTask::setManager);
            }

            // 🔄 Update Property
            if (updatedTask.getProperty() != null && updatedTask.getProperty().getPropertyId() != null) {
                propertyRepository.findById(updatedTask.getProperty().getPropertyId())
                        .ifPresent(existingTask::setProperty);
            }

            return taskRepository.save(existingTask);
        }).orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
    }

    // ✅ DELETE TASK
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}
