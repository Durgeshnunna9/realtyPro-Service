package com.realtypro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.realtypro.schema.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long>{
    long countByAgentAgentIdAndStatusNot(Long agentId, String status);
}
