package com.realtypro.controller;

import com.realtypro.repository.PropertyRepository;
import com.realtypro.schema.Agent;
import com.realtypro.schema.Property;
import com.realtypro.schema.User;
import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ➕ Create Agent
    @PostMapping("/create")
    public ResponseEntity<?> createAgent(@RequestBody Agent agent) {
        try {
            // Check if user exists (FK reference)
            if (agent.getUser() != null) {
                Optional<User> userOpt = userRepository.findById(agent.getUser().getUserId());
                if (userOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("User not found with ID: " + agent.getUser().getUserId());
                }
                agent.setUser(userOpt.get());
            } else {
                return ResponseEntity.badRequest().body("User reference is required");
            }

            Agent savedAgent = agentRepository.save(agent);
            return ResponseEntity.ok(savedAgent);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving agent: " + e.getMessage());
        }
    }

    // 📋 Get all agents
    @GetMapping("/all")
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = agentRepository.findAll();
        if (agents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agents);
    }

    // 🔍 Get agent by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAgentById(@PathVariable Long id) {
        Optional<Agent> agentOpt = agentRepository.findById(id);
        return agentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAgentByUserId(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        Optional<Agent> agentOpt = agentRepository.findByUser(userOpt.get());
        if (agentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Agent not found for this user"));
        }

        return ResponseEntity.ok(agentOpt.get());
    }

    @GetMapping("/{agentId}/latest-properties")
    public ResponseEntity<List<Property>> getLatestProperties(@PathVariable Long agentId) {
        List<Property> properties = propertyRepository.findByAgentAgentIdOrderByPropertyIdDesc(agentId);
        return ResponseEntity.ok(properties);
    }

    // ✏️ Update agent rating or linked user
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAgent(@PathVariable Long id, @RequestBody Agent updatedAgent) {
        Optional<Agent> existingOpt = agentRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Agent existing = existingOpt.get();

        // Update rating
        existing.setRating(updatedAgent.getRating());

        // Update linked user (if provided)
        if (updatedAgent.getUser() != null) {
            Optional<User> userOpt = userRepository.findById(updatedAgent.getUser().getUserId());
            userOpt.ifPresent(existing::setUser);
        }

        Agent saved = agentRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ❌ Delete agent
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAgent(@PathVariable Long id) {
        if (!agentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        agentRepository.deleteById(id);
        return ResponseEntity.ok("Agent deleted successfully");
    }
}
