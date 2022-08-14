package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.dto.TeamCreationDTO;
import academy.scalefocus.timeOffManagement.dto.TeamResponseDTO;
import academy.scalefocus.timeOffManagement.dto.UserResponseDTO;

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
