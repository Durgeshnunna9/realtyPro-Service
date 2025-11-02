package com.realtypro.controller;

import com.realtypro.schema.TeamMember;
import com.realtypro.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/team_members")
public class TeamMemberController {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    // ✅ Create new team member
    @PostMapping("/insert")
    public ResponseEntity<TeamMember> createTeamMember(@RequestBody TeamMember teamMember) {
        try {
            TeamMember savedTeamMember = teamMemberRepository.save(teamMember);
            return ResponseEntity.ok(savedTeamMember);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
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
        return teamMember.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Get team members by Manager ID
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<TeamMember>> getTeamMembersByManager(@PathVariable Long managerId) {
        List<TeamMember> teamMembers = teamMemberRepository.findByManager_ManagerId(managerId);
        if (teamMembers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(teamMembers);
    }

    // ✅ Get team members by Agent ID
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<TeamMember>> getTeamMembersByAgent(@PathVariable Long agentId) {
        List<TeamMember> teamMembers = teamMemberRepository.findByAgent_AgentId(agentId);
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
