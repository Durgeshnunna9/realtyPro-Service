package com.realtypro.controller;


import com.realtypro.mapper.UserMapper;
import com.realtypro.repository.UserRepository;
//import com.realtypro.schema.Agent;
import com.realtypro.schema.User;
import com.realtypro.utilities.Role;
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
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ LOGIN
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

//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "Invalid email or password"));
//        }

        // ✅ Optional: Return a DTO without password
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userRequest) {
        // 1️⃣ Check if user already exists
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email already registered"));
        }

        // 2️⃣ Encode password
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // 3️⃣ Validate role (default to AGENT if not provided)
        if (userRequest.getRole() == null) {
            userRequest.setRole(Role.AGENT);
        }

        // 4️⃣ Save user
        User savedUser = userRepository.save(userRequest);

        // 5️⃣ Return response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registration successful");
        response.put("userId", savedUser.getUserId());
        response.put("role", savedUser.getRole());

        return ResponseEntity.ok(response);
    }
}

