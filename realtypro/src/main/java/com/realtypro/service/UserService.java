package com.realtypro.service;

import com.realtypro.schema.User;
import com.realtypro.repository.UserRepository;
import com.realtypro.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ CREATE single user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // ✅ CREATE multiple users
    public List<User> createUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    // ✅ READ all users (sorted by ID)
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "userId"));
    }

    public List<User> getAgents() {
        return userRepository.findByRole(Role.AGENT);
    }

    public List<User> getManagers() {
        return userRepository.findByRole(Role.MANAGER);
    }

    // ✅ READ single user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ✅ UPDATE user
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setRating(updatedUser.getRating());
            return userRepository.save(existingUser);
        });
    }

    // ✅ DELETE user
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
