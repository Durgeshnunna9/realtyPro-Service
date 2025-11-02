package com.realtypro.controller;

import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.ManagerRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.TaskRepository;
import com.realtypro.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try{
            // Validate agent
            Optional<Agent> agentOpt = agentRepository.findById(task.getAgent().getAgentId());
            if (agentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Agent ID");
            }

            // Validate manager
            Optional<Manager> managerOpt = managerRepository.findById(task.getManager().getManagerId());
            if (managerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Manager ID");
            }

            // Validate property
            Optional<Property> propertyOpt = propertyRepository.findById(task.getProperty().getPropertyId());
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Property ID");
            }

            // Set validated references
            task.setAgent(agentOpt.get());
            task.setManager(managerOpt.get());
            task.setProperty(propertyOpt.get());

            Task savedTask = taskRepository.save(task);
            return ResponseEntity.ok(savedTask);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving task: " + e.getMessage());
        }
    }

    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if(tasks.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tasks);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTaskName(updatedTask.getTaskName());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());

            // Optionally update relationships
            if (updatedTask.getAgent() != null) {
                agentRepository.findById(updatedTask.getAgent().getAgentId())
                        .ifPresent(existingTask::setAgent);
            }
            if (updatedTask.getManager() != null) {
                managerRepository.findById(updatedTask.getManager().getManagerId())
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

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok( "Task deleted sucessfully.");
    }
}
