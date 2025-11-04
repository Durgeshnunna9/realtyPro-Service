package com.realtypro.repository;

import com.realtypro.schema.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByManagerUserId(Long managerId);

    List<TeamMember> findByAgentUserId(Long agentId);
}
