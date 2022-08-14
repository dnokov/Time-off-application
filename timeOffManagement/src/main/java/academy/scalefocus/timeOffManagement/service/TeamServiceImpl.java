package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.dto.TeamCreationDTO;
import academy.scalefocus.timeOffManagement.dto.TeamResponseDTO;
import academy.scalefocus.timeOffManagement.dto.UserResponseDTO;
import academy.scalefocus.timeOffManagement.dto.mappers.TeamMapper;
import academy.scalefocus.timeOffManagement.dto.mappers.UserMapper;
import academy.scalefocus.timeOffManagement.exception.TeamHasNoMembersException;
import academy.scalefocus.timeOffManagement.exception.TeamNotFoundException;
import academy.scalefocus.timeOffManagement.exception.UserNotFoundException;
import academy.scalefocus.timeOffManagement.model.Team;
import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.repository.TeamRepository;
import academy.scalefocus.timeOffManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("teamService")
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    private final TeamMapper teamMapper = TeamMapper.INSTANCE;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public TeamResponseDTO findById(Long id) {
        Team team = findTeamById(id);
        return teamMapper.teamToTeamResponseDto(team);
    }

    @Override
    public TeamResponseDTO findByTitle(String title) {
        Team team = teamRepository.findByTitle(title)
                .orElseThrow(() -> new TeamNotFoundException(title));

        return teamMapper.teamToTeamResponseDto(team);
    }

    @Override
    public List<TeamResponseDTO> findAllTeams() {

        return teamRepository.findAll().stream()
                .map(teamMapper::teamToTeamResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Set<UserResponseDTO> findAllMembers(Long teamId) {
        Team team = findTeamById(teamId);

        return team.getMembers().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toSet());
    }

    @Override
    public TeamResponseDTO save(TeamCreationDTO teamDTO) {

        Boolean ifExists = teamRepository.existsByTitle(teamDTO.getTitle());

        if (ifExists) {
            throw new IllegalArgumentException("Title is already taken.");
        }

        Team team = teamMapper.teamDtoToTeam(teamDTO);
        teamRepository.save(team);
        return teamMapper.teamToTeamResponseDto(team);
    }

    @Override
    public TeamResponseDTO update(Long id, TeamCreationDTO teamDTO) {

        Team existingTeam = findTeamById(id);

        if (!teamDTO.getTitle().equals(existingTeam.getTitle())) {

            Boolean ifExists = teamRepository.existsByTitle(teamDTO.getTitle());
            if (ifExists) {
                throw new IllegalArgumentException("Title is already taken.");
            }

            existingTeam.setTitle(teamDTO.getTitle());
        }

        if (!teamDTO.getDescription().equals(existingTeam.getDescription())) {
            existingTeam.setDescription(teamDTO.getDescription());
        }

        teamRepository.save(existingTeam);

        return teamMapper.teamToTeamResponseDto(existingTeam);
    }

    @Override
    public void delete(Long id) {
        findTeamById(id);

        teamRepository.deleteById(id);
    }

    @Override
    public void addMember(Long teamId, Long userId) {
        Team team = findTeamById(teamId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        team.addMember(user);
        teamRepository.save(team);
    }

    @Override
    public void removeMember(Long teamId, Long userId) {
        Team team = findTeamById(teamId);
        User user = findUserByTeam(team, userId);

        team.getMembers().remove(user);
        teamRepository.save(team);
    }

    @Override
    public void setTeamLeader(Long teamId, Long userId) {
        Team team = findTeamById(teamId);
        User user = findUserByTeam(team, userId);

        team.setTeamLeader(user.getId());
        teamRepository.save(team);
    }

    private User findUserByTeam(Team team, Long userId) {

        if (team.getMembers().isEmpty()) {
            throw new TeamHasNoMembersException(team.getId());
        }

        return team.getMembers().stream()
                .filter(member -> member.getId().equals(userId))
                .findFirst().orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Team findTeamById(Long id) {
        return teamRepository
                .findById(id).orElseThrow(() -> new TeamNotFoundException(id));
    }
}

