package com.realtypro.repository;

import com.realtypro.schema.Agent;
import com.realtypro.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    // 🔍 Find agent linked to a specific user
    Optional<Agent> findByUser(User user);

    // ✅ Check if an agent already exists for a given user
    boolean existsByUser(User user);
}
