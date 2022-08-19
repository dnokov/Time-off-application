package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.TeamCreationDTO;
import com.learning.timeOffManagement.exception.TeamHasNoMembersException;
import com.learning.timeOffManagement.exception.TeamNotFoundException;
import com.learning.timeOffManagement.exception.UserNotFoundException;
import com.learning.timeOffManagement.model.Team;
import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.repository.TeamRepository;
import com.learning.timeOffManagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;
    private Team updatedTeam;
    private TeamCreationDTO teamDto;
    private TeamCreationDTO updatedTeamDto;
    private final Long teamId = 1L;

    private User user;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setTitle("Developers");
        team.setDescription("description");
        team.setId(1L);

        updatedTeam = new Team();
        updatedTeam.setId(1L);
        updatedTeam.setTitle("Managers");
        updatedTeam.setDescription("description");

        teamDto = new TeamCreationDTO("Developers", "description");
        updatedTeamDto = new TeamCreationDTO("Developers", "description");

        user = new User("admin", "adminpass", "Ivan", "Ivanov",
            0L, true, "admin@abv.bg");

        user.setId(1L);

    }

    @Test
    public void shouldFindTeamById() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        teamService.findById(teamId);

        then(teamRepository).should().findById(teamId);
    }

    @Test
    public void shouldThrowExceptionWhenTeamIsNotFoundById() {

        assertThrows(TeamNotFoundException.class, () -> teamService.findById(teamId));
    }

    @Test
    public void shouldFindTeamByTitle() {
        given(teamRepository.findByTitle(team.getTitle())).willReturn(Optional.of(team));

        teamService.findByTitle(teamDto.getTitle());

        then(teamRepository).should().findByTitle(team.getTitle());
    }

    @Test
    public void shouldThrowExceptionWhenTeamIsNotFoundByTitle() {

        assertThrows(TeamNotFoundException.class,
            () -> teamService.findByTitle(teamDto.getTitle()));
    }

    @Test
    public void shouldThrowExceptionWhenTitleIsTaken() {
        given(teamRepository.existsByTitle(team.getTitle())).willReturn(true);

        assertThrows(IllegalArgumentException.class, () -> teamService.save(teamDto));
    }

    @Test
    public void shouldSaveTeam() {
        given(teamRepository.existsByTitle(teamDto.getTitle())).willReturn(false);
        when(teamRepository.save(ArgumentMatchers.any(Team.class))).thenReturn(team);

        assertEquals(teamService.save(teamDto).getTitle(), team.getTitle());
    }

    @Test
    public void shouldUpdateTeam() {
        updatedTeamDto.setTitle("Managers");
        updatedTeamDto.setDescription("new description");

        given(teamRepository.findById(teamId)).willReturn(Optional.of(updatedTeam));

        teamService.update(teamId, updatedTeamDto);
        then(teamRepository).should().save(updatedTeam);
    }

    @Test
    public void shouldNotUpdateTeamWhenTeamIsNotFound() {

        assertThrows(TeamNotFoundException.class, () -> teamService.update(teamId, teamDto));
    }

    @Test
    public void shouldThrowExceptionWhenUpdateTeamTitleIsTaken() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(updatedTeam));
        given(teamRepository.existsByTitle(teamDto.getTitle())).willThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> teamService.update(teamId, teamDto));
    }

    @Test
    public void shouldDeleteTeam() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        teamService.delete(teamId);

        then(teamRepository).should().deleteById(teamId);
    }

    @Test
    public void shouldNotDeleteWhenTeamIsNotFound() {

        assertThrows(TeamNotFoundException.class, () -> teamService.delete(teamId));
    }

    @Test
    public void shouldAddMember() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        teamService.addMember(teamId, userId);

        then(teamRepository).should().save(team);
    }

    @Test
    public void shouldNotAddMemberWhenUserDoesNotExists() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        assertThrows(UserNotFoundException.class, () -> teamService.addMember(teamId, userId));
    }

    @Test
    public void shouldNotAddMemberWhenTeamDoesNotExists() {

        assertThrows(TeamNotFoundException.class, () -> teamService.addMember(teamId, userId));
    }

    @Test
    public void shouldRemoveMember() {
        team.addMember(user);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        teamService.removeMember(teamId, userId);

        then(teamRepository).should().save(team);
    }

    @Test
    public void shouldNotRemoveMemberWhenUserIsNotFound() {
        User testUser = new User();
        testUser.setId(2L);
        team.addMember(testUser);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        assertThrows(UserNotFoundException.class, () -> teamService.removeMember(teamId, userId));
    }

    @Test
    public void shouldNotRemoveMemberWhenTeamIsNotFound() {

        assertThrows(TeamNotFoundException.class, () -> teamService.removeMember(teamId, userId));
    }

    @Test
    public void shouldSetTeamLeader() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        team.getMembers().add(user);
        teamService.setTeamLeader(teamId, userId);

        then(teamRepository).should().save(team);
    }

    @Test
    public void shouldNotSetTeamLeaderWhenThereAreNoMembers() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        assertThrows(TeamHasNoMembersException.class,
            () -> teamService.setTeamLeader(teamId, userId));
    }

    @Test
    public void shouldFindAll() {
        given(teamRepository.findAll()).willReturn(List.of(team));

        teamService.findAllTeams();

        then(teamRepository).should().findAll();
    }

    @Test
    public void shouldFindAllMembers() {
        given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

        assertNotNull(teamService.findAllMembers(teamId));
    }

    @Test
    public void shouldNotFindAllMembersWhenTeamIsNotFound() {

        assertThrows(TeamNotFoundException.class, () -> teamService.findAllMembers(teamId));
    }
}