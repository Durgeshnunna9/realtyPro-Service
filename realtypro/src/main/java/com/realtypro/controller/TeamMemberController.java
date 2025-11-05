package com.realtypro.controller;

import com.realtypro.schema.TeamMember;
import com.realtypro.schema.User;
import com.realtypro.utilities.Role;
import com.realtypro.repository.TeamMemberRepository;
import com.realtypro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/team_members")
public class TeamMemberController {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Create new team member
    @PostMapping("/insert")
    public ResponseEntity<?> createTeamMember(@RequestBody TeamMember teamMember) {
        try {
            // ✅ Validate Manager
            Optional<User> managerOpt = userRepository.findById(teamMember.getManager().getUserId());
            if (managerOpt.isEmpty() || managerOpt.get().getRole() != Role.MANAGER) {
                return ResponseEntity.badRequest().body("Invalid Manager ID or Role");
            }

            // ✅ Validate Agent
            Optional<User> agentOpt = userRepository.findById(teamMember.getAgent().getUserId());
            if (agentOpt.isEmpty() || agentOpt.get().getRole() != Role.AGENT) {
                return ResponseEntity.badRequest().body("Invalid Agent ID or Role");
            }

            // ✅ Save the valid team member
            TeamMember savedTeamMember = teamMemberRepository.save(teamMember);
            return ResponseEntity.ok(savedTeamMember);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating team member: " + e.getMessage());
        }
    }

    // ✅ Get all team members
    @GetMapping("/all")
    public ResponseEntity<List<TeamMember>> getAllTeamMembers() {
        List<TeamMember> teamMembers = teamMemberRepository.findAll();
        if (teamMembers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(teamMembers);
    }

    // ✅ Get team member by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamMember> getTeamMemberById(@PathVariable Long id) {
        Optional<TeamMember> teamMember = teamMemberRepository.findById(id);
        return teamMember.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Get team members by Manager ID (now filtered through User with role MANAGER)
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<?> getTeamMembersByManager(@PathVariable Long managerId) {
        Optional<User> managerOpt = userRepository.findById(managerId);
        if (managerOpt.isEmpty() || managerOpt.get().getRole() != Role.MANAGER) {
            return ResponseEntity.badRequest().body("Invalid Manager ID or Role");
        }

        List<TeamMember> teamMembers = teamMemberRepository.findByManagerUserId(managerId);
        if (teamMembers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(teamMembers);
    }

    // ✅ Get team members by Agent ID (now filtered through User with role AGENT)
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<?> getTeamMembersByAgent(@PathVariable Long agentId) {
        Optional<User> agentOpt = userRepository.findById(agentId);
        if (agentOpt.isEmpty() || agentOpt.get().getRole() != Role.AGENT) {
            return ResponseEntity.badRequest().body("Invalid Agent ID or Role");
        }

        List<TeamMember> teamMembers = teamMemberRepository.findByAgentUserId(agentId);
        if (teamMembers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(teamMembers);
    }

    // ✅ Delete team member
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable Long id) {
        if (teamMemberRepository.existsById(id)) {
            teamMemberRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
