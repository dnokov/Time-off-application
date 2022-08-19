package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.TeamCreationDTO;
import com.learning.timeOffManagement.dto.TeamResponseDTO;
import com.learning.timeOffManagement.dto.UserResponseDTO;

import java.util.List;
import java.util.Set;

public interface TeamService {

    TeamResponseDTO findById(Long id);

    TeamResponseDTO findByTitle(String title);

    List<TeamResponseDTO> findAllTeams();

    Set<UserResponseDTO> findAllMembers(Long teamId);

    TeamResponseDTO save(TeamCreationDTO teamDTO);

    TeamResponseDTO update(Long id, TeamCreationDTO teamDTO);

    void delete(Long id);

    void addMember(Long teamId, Long userId);

    void removeMember(Long teamId, Long userId);

    void setTeamLeader(Long teamId, Long userId);
}
