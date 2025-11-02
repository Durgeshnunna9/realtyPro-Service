package com.realtypro.service;

import com.realtypro.schema.Agent;
import com.realtypro.schema.User;
import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Create Agent
    public Agent createAgent(Agent agent) throws Exception {
        if (agent.getUser() == null) {
            throw new Exception("User reference is required");
        }

        Optional<User> userOpt = userRepository.findById(agent.getUser().getUserId());
        if (userOpt.isEmpty()) {
            throw new Exception("User not found with ID: " + agent.getUser().getUserId());
        }

        agent.setUser(userOpt.get());
        return agentRepository.save(agent);
    }

    // ✅ Get All Agents
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    // ✅ Get Agent by ID
    public Optional<Agent> getAgentById(Long id) {
        return agentRepository.findById(id);
    }

    // ✅ Update Agent
    public Agent updateAgent(Long id, Agent updatedAgent) throws Exception {
        Optional<Agent> existingOpt = agentRepository.findById(id);// explain this
        if (existingOpt.isEmpty()) {
            throw new Exception("Agent not found with ID: " + id);
        }

        Agent existing = existingOpt.get();
        existing.setRating(updatedAgent.getRating());

        if (updatedAgent.getUser() != null) {
            Optional<User> userOpt = userRepository.findById(updatedAgent.getUser().getUserId());
            if (userOpt.isPresent()) {
                existing.setUser(userOpt.get());
            }
        }

        return agentRepository.save(existing);
    }

    // ✅ Delete Agent
    public void deleteAgent(Long id) throws Exception {
        if (!agentRepository.existsById(id)) {
            throw new Exception("Agent not found with ID: " + id);
        }
        agentRepository.deleteById(id);
    }
}
