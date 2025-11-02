package com.realtypro.repository;

import com.realtypro.schema.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByAgentAgentId(Long agentId);
    long countByAgentAgentId(Long agentId);
//    List<Property> findTop5ByAgentAgentIdOrderByCreatedAtDesc(Long agentId);
    List<Property> findByAgentAgentIdOrderByPropertyIdDesc(Long agentId);
}
