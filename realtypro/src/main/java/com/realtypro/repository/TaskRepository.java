package com.realtypro.repository;

import com.realtypro.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.realtypro.schema.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByAgentUserIdAndStatusNot(Long userId, String status);
    long countByManagerUserIdAndStatusNot(Long userId, String status);

    List<Task> findByAgent(User agent);
}

