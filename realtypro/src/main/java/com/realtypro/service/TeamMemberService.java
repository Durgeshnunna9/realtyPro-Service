package com.realtypro.service;

import com.realtypro.schema.TeamMember;
import com.realtypro.repository.TeamMemberRepository;
import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.ManagerRepository;
import com.realtypro.schema.Agent;
import com.realtypro.schema.Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    // ✅ Create new team member (with validation)
    public TeamMember createTeamMember(TeamMember teamMember) throws Exception {
        Optional<Agent> agentOpt = agentRepository.findById(teamMember.getAgent().getAgentId());
        Optional<Manager> managerOpt = managerRepository.findById(teamMember.getManager().getManagerId());

        if (agentOpt.isEmpty()) {
            throw new Exception("Invalid Agent ID: " + teamMember.getAgent().getAgentId());
        }

        if (managerOpt.isEmpty()) {
            throw new Exception("Invalid Manager ID: " + teamMember.getManager().getManagerId());
        }

        teamMember.setAgent(agentOpt.get());
        teamMember.setManager(managerOpt.get());

        return teamMemberRepository.save(teamMember);
    }

    // ✅ Get all team members
    public List<TeamMember> getAllTeamMembers() {
        return teamMemberRepository.findAll();
    }

    // ✅ Get team member by ID
    public Optional<TeamMember> getTeamMemberById(Long id) {
        return teamMemberRepository.findById(id);
    }

    // ✅ Get team members by Manager ID
    public List<TeamMember> getTeamMembersByManagerId(Long managerId) {
        return teamMemberRepository.findByManager_ManagerId(managerId);
    }

    // ✅ Get team members by Agent ID
    public List<TeamMember> getTeamMembersByAgentId(Long agentId) {
        return teamMemberRepository.findByAgent_AgentId(agentId);
    }

    // ✅ Delete team member
    public void deleteTeamMember(Long id) throws Exception {
        if (!teamMemberRepository.existsById(id)) {
            throw new Exception("Team Member not found with ID: " + id);
        }
        teamMemberRepository.deleteById(id);
    }
}
