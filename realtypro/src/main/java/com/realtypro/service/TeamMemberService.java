package com.realtypro.service;

import com.realtypro.schema.TeamMember;
import com.realtypro.schema.User;
import com.realtypro.repository.TeamMemberRepository;
import com.realtypro.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Create new team member (with validation)
    public TeamMember createTeamMember(TeamMember teamMember) {
        if (teamMember.getUser() == null || teamMember.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User reference is required");
        }

        if (teamMember.getManager() == null || teamMember.getManager().getUserId() == null) {
            throw new IllegalArgumentException("Manager reference is required");
        }

        User user = userRepository.findById(teamMember.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + teamMember.getUser().getUserId()));

        User manager = userRepository.findById(teamMember.getManager().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with ID: " + teamMember.getManager().getUserId()));

        teamMember.setUser(user);
        teamMember.setManager(manager);

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
        return teamMemberRepository.findByManagerUserId(managerId);
    }

    // ✅ Get team members by User ID
    public List<TeamMember> getTeamMembersByUserId(Long userId) {
        return teamMemberRepository.findByAgentUserId(userId);
    }

    // ✅ Delete team member
    public void deleteTeamMember(Long id) {
        if (!teamMemberRepository.existsById(id)) {
            throw new IllegalArgumentException("Team Member not found with ID: " + id);
        }
        teamMemberRepository.deleteById(id);
    }
}
