package com.learning.timeOffManagement.controller;

import com.learning.timeOffManagement.dto.TeamCreationDTO;
import com.learning.timeOffManagement.dto.TeamResponseDTO;
import com.learning.timeOffManagement.dto.UserResponseDTO;
import com.learning.timeOffManagement.dto.mappers.TeamMapper;
import com.learning.timeOffManagement.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/teams")
@PreAuthorize("hasAuthority('ADMIN')")
@Api(tags = "Teams")
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamMapper teamMapper;

    @GetMapping
    @ApiOperation("Get all the teams")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        List<TeamResponseDTO> allTeams = teamService.findAllTeams();
        return new ResponseEntity<>(allTeams, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a specific team by the ID")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id) {
        return new ResponseEntity<>(teamService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/members")
    @ApiOperation("Get all the members of a specific team")
    public ResponseEntity<Set<UserResponseDTO>> getTeamMembers(@PathVariable Long id) {
        return new ResponseEntity<>(teamService.findAllMembers(id), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("Create a new team")
    public ResponseEntity<?> createTeam(UriComponentsBuilder ucBuilder, @Valid @RequestBody TeamCreationDTO teamCreationDTO) {
        TeamResponseDTO savedTeam = teamService.save(teamCreationDTO);
        return ResponseEntity.created(ucBuilder.path("/teams/{teamId}")
                .buildAndExpand(savedTeam.getId()).toUri())
                .build();
    }

    @PostMapping("/{teamId}/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation("Add a new member to a specific team")
    public void addTeamMember(@PathVariable Long teamId,
                              @PathVariable Long userId) {
        teamService.addMember(teamId, userId);
    }

    @PutMapping("/{id}")
    @ApiOperation("Modify the information for a specific team")
    public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamCreationDTO teamCreationDTO) {
        return new ResponseEntity<>(teamService.update(id, teamCreationDTO), HttpStatus.OK);
    }

    @PatchMapping("/{teamId}/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation("Set a team leader for a specific team")
    public void setTeamLeader(@PathVariable Long teamId,
                              @PathVariable Long userId) {
        teamService.setTeamLeader(teamId, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation("Delete a team")
    public void deleteTeam(@PathVariable Long id) {
        teamService.delete(id);
    }

    @DeleteMapping("/{teamId}/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation("Remove a member from a team")
    public void deleteTeamMember(@PathVariable Long teamId,
                                 @PathVariable Long userId) {
        teamService.removeMember(teamId, userId);
    }
}
