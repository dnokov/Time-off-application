package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.dto.TimeOffCreationDTO;
import academy.scalefocus.timeOffManagement.dto.TimeOffResponseDTO;
import academy.scalefocus.timeOffManagement.dto.TimeOffUpdateDTO;
import academy.scalefocus.timeOffManagement.dto.mappers.TimeOffMapper;
import academy.scalefocus.timeOffManagement.exception.TimeOffAlreadySubmittedException;
import academy.scalefocus.timeOffManagement.exception.TimeOffDateException;
import academy.scalefocus.timeOffManagement.exception.TimeOffLimitExceededException;
import academy.scalefocus.timeOffManagement.exception.TimeOffNotFoundException;
import academy.scalefocus.timeOffManagement.model.TimeOff;
import academy.scalefocus.timeOffManagement.model.TimeOffStatus;
import academy.scalefocus.timeOffManagement.model.TimeOffType;
import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.repository.TimeOffRepository;
import academy.scalefocus.timeOffManagement.repository.UserRepository;
import academy.scalefocus.timeOffManagement.security.UserPrincipal;
import academy.scalefocus.timeOffManagement.utils.TimeOffUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TimeOffServiceImplTest {

    @Mock
    private TimeOffRepository timeOffRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimeOffMapper timeOffMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private TimeOffUtils timeOffUtils;

    @InjectMocks
    private TimeOffServiceImpl timeOffService;

    private TimeOff timeOff;
    private TimeOffCreationDTO timeOffCreationDTO;
    private TimeOffResponseDTO timeOffResponseDTO;
    private TimeOffUpdateDTO timeOffUpdateDTO;
    private User currentUser;
    private User approver;
    private UserPrincipal userPrincipal;
    private List<User> awaitingApproval = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userPrincipal = new UserPrincipal(1L, "user1", false);

        currentUser = new User("user1", "adminpass", "Ivan", "Ivanov",
                0L, true, "user1@gmail.bg");
        currentUser.setId(1L);

        approver = new User("user2", "adminpass", "Mariya", "Ivanova",
                0L, true, "user2@gmail.bg");
        approver.setId(2L);

        awaitingApproval.add(approver);

        timeOff = new TimeOff(1L, TimeOffType.PAID, "vacation", TimeOffStatus.CREATED, LocalDate.now(), LocalDate.now().plusDays(5),
                currentUser, awaitingApproval);

        timeOffCreationDTO = new TimeOffCreationDTO("PAID", "vacation", LocalDate.now(), LocalDate.now().plusDays(5));
        timeOffResponseDTO = new TimeOffResponseDTO(1L, "PAID", "vacation", "APPROVED", LocalDate.now(), LocalDate.now().plusDays(5),
                currentUser.getUsername(), List.of(approver.getUsername()));

        timeOffUpdateDTO = new TimeOffUpdateDTO(1L, "PAID", "vacation", LocalDate.now(), LocalDate.now().plusDays(5));

        currentUser.getRequests().add(timeOff);
        userRepository.save(currentUser);
        userRepository.save(approver);
    }

    @Test
    public void shouldFindById() {
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));
        given(timeOffMapper.timeOffToResponseDTO(timeOff)).willReturn(timeOffResponseDTO);

        assertNotNull(timeOffService.getTimeOff(userPrincipal, 1L));
    }

    @Test
    public void shouldThrowExceptionWhenTimeOffIsNotFoundByById() {
        currentUser.getRequests().remove(timeOff);
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));

        assertThrows(TimeOffNotFoundException.class, () -> timeOffService.getTimeOff(userPrincipal, 1L));
    }

    @Test
    public void shouldSaveTimeOff() {
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));
        given(timeOffUtils.compareDates(timeOff.getStartDate(), timeOff.getEndDate())).willReturn(true);
        given(timeOffUtils.checkIfUserIsAbleToRequest(currentUser, timeOffCreationDTO)).willReturn(false);
        given(timeOffMapper.creationDTOtoTimeOff(timeOffCreationDTO, currentUser)).willReturn(timeOff);

        timeOffService.addTimeOff(userPrincipal, timeOffCreationDTO);

        then(timeOffRepository).should().save(timeOff);
        then(userRepository).should().save(currentUser);
    }

    @Test
    public void shouldNotSaveWhenStartDateIsAfterEndDate() {
        LocalDate date = timeOff.getStartDate();
        timeOff.setStartDate(timeOff.getEndDate());
        timeOff.setEndDate(date);
        timeOffCreationDTO.setStartDate(timeOffCreationDTO.getEndDate());
        timeOffCreationDTO.setEndDate(date);
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));
        given(timeOffUtils.compareDates(timeOff.getStartDate(), timeOff.getEndDate())).willThrow(TimeOffDateException.class);

        assertThrows(TimeOffDateException.class, () -> timeOffService.addTimeOff(userPrincipal, timeOffCreationDTO));
    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotAbleToRequest() {
        currentUser.setPaidLeave(0);

        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));
        given(timeOffUtils.compareDates(timeOff.getStartDate(), timeOff.getEndDate())).willReturn(true);
        given(timeOffUtils.checkIfUserIsAbleToRequest(currentUser, timeOffCreationDTO)).willThrow(TimeOffLimitExceededException.class);

        assertThrows(TimeOffLimitExceededException.class, () -> timeOffService.addTimeOff(userPrincipal, timeOffCreationDTO));
    }

    @Test
    public void shouldApproveWhenTimeOffTypeIsSickLeave() {
        timeOffCreationDTO.setType("SICK_LEAVE");
        timeOff.setType(TimeOffType.SICK_LEAVE);

        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));
        given(timeOffUtils.compareDates(timeOff.getStartDate(), timeOff.getEndDate())).willReturn(true);
        given(timeOffUtils.checkIfUserIsAbleToRequest(currentUser, timeOffCreationDTO)).willReturn(false);
        given(timeOffMapper.creationDTOtoTimeOff(timeOffCreationDTO, currentUser)).willReturn(timeOff);

        timeOffService.addTimeOff(userPrincipal, timeOffCreationDTO);
        then(timeOffRepository).should().save(timeOff);
        then(userRepository).should().save(currentUser);

        assertEquals(TimeOffStatus.APPROVED, timeOff.getStatus());
        then(emailService).should().sendTimeOffRequestEmail(currentUser, approver, timeOff);
    }

    @Test
    public void shouldDeleteTimeOff() {
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));

        timeOffService.deleteTimeOffRequest(userPrincipal, 1L);
        then(timeOffRepository).should().delete(timeOff);
    }

    @Test
    public void shouldNotDeleteWhenTimeOffIsSubmitted() {
        timeOff.setStatus(TimeOffStatus.AWAITING);
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));

        assertThrows(TimeOffAlreadySubmittedException.class, () -> timeOffService.deleteTimeOffRequest(userPrincipal, 1L));
    }

    @Test
    public void shouldUpdateTimeOff() {
        timeOffUpdateDTO.setType("UNPAID");
        timeOff.setType(TimeOffType.UNPAID);

        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));
        given(timeOffMapper.timeOffToResponseDTO(timeOff)).willReturn(timeOffResponseDTO);

        timeOffService.updateTimeOff(userPrincipal, 1L, timeOffUpdateDTO);
        then(timeOffRepository).should().save(timeOff);
    }

    @Test
    public void shouldSendTimeOff() {
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));

        timeOffService.sendTimeOff(userPrincipal, 1L);
        then(timeOffRepository).should().save(timeOff);
        then(emailService).should().sendTimeOffRequestEmail(currentUser, approver, timeOff);
    }

    @Test
    public void shouldApproveWhenTeamLeadersAreOutOfOffice() {
        approver.setInOffice(false);
        given(userRepository.findById(1L)).willReturn(Optional.of(currentUser));

        timeOffService.sendTimeOff(userPrincipal, 1L);
        then(timeOffRepository).should().save(timeOff);

        assertEquals(TimeOffStatus.APPROVED, timeOff.getStatus());
    }

    @Test
    public void shouldApproveTimeOff() {
        userPrincipal = new UserPrincipal(2L, "user2", false);
        given(timeOffRepository.findById(1L)).willReturn(Optional.of(timeOff));

        timeOffService.approveTimeOffRequest(userPrincipal, 1L, true);
        then(timeOffRepository).should().save(timeOff);
        then(emailService).should().sendTimeOffRequestEmail(approver, currentUser, timeOff);

    }

    @Test
    public void shouldRejectTimeOff() {
        userPrincipal = new UserPrincipal(2L, "user2", false);
        given(timeOffRepository.findById(1L)).willReturn(Optional.of(timeOff));

        timeOffService.approveTimeOffRequest(userPrincipal, 1L, false);
        then(timeOffRepository).should().save(timeOff);
        then(emailService).should().sendTimeOffRequestEmail(approver, currentUser, timeOff);

    }

    @Test
    public void shouldThrowExceptionWhenTimeOffIsAlreadyRejected() {
        timeOff.setStatus(TimeOffStatus.REJECTED);
        userPrincipal = new UserPrincipal(2L, "user2", false);
        given(timeOffRepository.findById(1L)).willReturn(Optional.of(timeOff));

        assertThrows(TimeOffAlreadySubmittedException.class,
                () -> timeOffService.approveTimeOffRequest(userPrincipal, 1L, false));

    }
}