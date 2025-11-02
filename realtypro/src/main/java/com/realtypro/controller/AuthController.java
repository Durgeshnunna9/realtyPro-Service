package com.realtypro.controller;


import com.realtypro.dto.UserDTO;
import com.realtypro.mapper.UserMapper;
import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.UserRepository;
import com.realtypro.schema.Agent;
import com.realtypro.schema.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:8080") // or your frontend domain
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AgentRepository agentRepository;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        // 🔍 Find the agent linked to this user
        Optional<Agent> optionalAgent = agentRepository.findByUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("email", user.getEmail());
        response.put("first_name", user.getFirstName());
        response.put("last_name", user.getLastName());
        response.put("agentId", optionalAgent.map(Agent::getAgentId).orElse(null));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userRequest) {
        // 1️⃣ Check if user already exists
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email already registered"));
        }

        // 2️⃣ Save the new user
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(userRequest);

        // 3️⃣ Create and save the corresponding Agent record
        Agent agent = new Agent();
        agent.setUser(savedUser);
        agent.setRating(0); // default rating
        agentRepository.save(agent);

        // 4️⃣ Return the user (and agent info if needed)
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registration successful");
        response.put("userId", savedUser.getUserId());
        response.put("agentId", agent.getAgentId());

        return ResponseEntity.ok(response);
    }
}
