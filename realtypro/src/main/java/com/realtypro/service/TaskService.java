package com.realtypro.service;

import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.ManagerRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.TaskRepository;
import com.realtypro.schema.Agent;
import com.realtypro.schema.Manager;
import com.realtypro.schema.Property;
import com.realtypro.schema.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ CREATE TASK
    public Task createTask(Task task) {
        Optional<Agent> agentOpt = agentRepository.findById(task.getAgent().getAgentId());
        if (agentOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Agent ID: " + task.getAgent().getAgentId());
        }

        Optional<Manager> managerOpt = managerRepository.findById(task.getManager().getManagerId());
        if (managerOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Manager ID: " + task.getManager().getManagerId());
        }

        Optional<Property> propertyOpt = propertyRepository.findById(task.getProperty().getPropertyId());
        if (propertyOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Property ID: " + task.getProperty().getPropertyId());
        }

        task.setAgent(agentOpt.get());
        task.setManager(managerOpt.get());
        task.setProperty(propertyOpt.get());

        return taskRepository.save(task);
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

            // Optionally update relationships
            if (updatedTask.getAgent() != null && updatedTask.getAgent().getAgentId() != null) {
                agentRepository.findById(updatedTask.getAgent().getAgentId())
                        .ifPresent(existingTask::setAgent);
            }

            if (updatedTask.getManager() != null && updatedTask.getManager().getManagerId() != null) {
                managerRepository.findById(updatedTask.getManager().getManagerId())
                        .ifPresent(existingTask::setManager);
            }

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
