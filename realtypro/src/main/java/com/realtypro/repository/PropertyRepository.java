package com.realtypro.repository;

import com.realtypro.schema.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // 🧭 AGENT-based queries (User with role = AGENT)
    List<Property> findByAgentUserId(Long userId);
    long countByAgentUserId(Long userId);
    List<Property> findByAgentUserIdOrderByPropertyIdDesc(Long userId);

    // 🧭 MANAGER-based queries (User with role = MANAGER)
    long countByManagerUserId(Long userId);
    List<Property> findByManagerUserId(Long userId);
}


