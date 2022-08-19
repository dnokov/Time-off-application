package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.TimeOffCreationDTO;
import com.learning.timeOffManagement.dto.TimeOffResponseDTO;
import com.learning.timeOffManagement.dto.TimeOffUpdateDTO;
import com.learning.timeOffManagement.dto.mappers.TimeOffMapper;
import com.learning.timeOffManagement.exception.*;
import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.TimeOffStatus;
import com.learning.timeOffManagement.model.TimeOffType;
import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.repository.TimeOffRepository;
import com.learning.timeOffManagement.repository.UserRepository;
import com.learning.timeOffManagement.security.UserPrincipal;
import com.learning.timeOffManagement.utils.TimeOffUtils;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeOffServiceImpl implements TimeOffService {

    private final TimeOffRepository timeOffRepository;
    private final UserRepository userRepository;
    private final TimeOffMapper timeOffMapper;
    private final EmailService emailService;
    private final TimeOffUtils timeOffUtils;

    @Autowired
    public TimeOffServiceImpl(TimeOffRepository timeOffRepository, UserRepository userRepository,
                              TimeOffMapper timeOffMapper,
                              EmailService emailService, TimeOffUtils timeOffUtils) {
        this.timeOffRepository = timeOffRepository;
        this.userRepository = userRepository;
        this.timeOffMapper = timeOffMapper;
        this.emailService = emailService;
        this.timeOffUtils = timeOffUtils;
    }

    @Override
    public TimeOffResponseDTO addTimeOff(UserPrincipal userPrincipal,
                                         TimeOffCreationDTO timeOffCreationDTO) {
        User currentUser = userRepository.findById(userPrincipal.getId()).get();

        if (timeOffCreationDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new TimeOffDateException(timeOffCreationDTO.getStartDate().toString());
        }

        if (!timeOffUtils
                .compareDates(timeOffCreationDTO.getStartDate(), timeOffCreationDTO.getEndDate())) {
            throw new TimeOffDateException(timeOffCreationDTO.getStartDate().toString(), timeOffCreationDTO.getEndDate().toString());
        }

        if (timeOffUtils.checkIfUserIsAbleToRequest(currentUser, timeOffCreationDTO)) {
            throw new TimeOffLimitExceededException(timeOffCreationDTO.getStartDate().toString(), timeOffCreationDTO.getEndDate().toString());
        }

        TimeOff timeOff = timeOffMapper.creationDTOtoTimeOff(timeOffCreationDTO, currentUser);

        if (timeOff.getType().equals(TimeOffType.SICK_LEAVE)) {
            timeOff.setStatus(TimeOffStatus.APPROVED);
            timeOff.getAwaitingApproval()
                    .forEach(approver -> emailService
                            .sendTimeOffRequestEmail(currentUser, approver, timeOff));
        }
        timeOffRepository.save(timeOff);
        userRepository.save(timeOffUtils.reduceDays(timeOff.getCreator(), timeOff));

        return timeOffMapper.timeOffToResponseDTO(timeOff);
    }

    @Override
    public TimeOffResponseDTO getTimeOff(UserPrincipal userPrincipal, Long timeOffId) {

        TimeOff timeOffOptional = checkTimeOffRequestCreatedByUser(userPrincipal, timeOffId);
        return timeOffMapper.timeOffToResponseDTO(timeOffOptional);
    }

    @Override
    public TimeOffResponseDTO deleteTimeOffRequest(UserPrincipal userPrincipal, Long timeOffId) {

        TimeOff deletionTarget = checkTimeOffRequestCreatedByUser(userPrincipal, timeOffId);
        if (!deletionTarget.getStatus().equals(TimeOffStatus.CREATED)) {

            throw new TimeOffAlreadySubmittedException(timeOffId);
        }
        this.timeOffRepository.delete(deletionTarget);
        return timeOffMapper.timeOffToResponseDTO(deletionTarget);
    }

    @Override
    public TimeOffResponseDTO updateTimeOff(UserPrincipal userPrincipal, Long timeOffId, TimeOffUpdateDTO timeOffUpdateDTO) {

        TimeOff updateTarget = checkTimeOffRequestCreatedByUser(userPrincipal, timeOffId);
        if (!updateTarget.getStatus().equals(TimeOffStatus.CREATED)) {
            throw new TimeOffAlreadySubmittedException(timeOffId);
        }
        updateTarget.setReason(timeOffUpdateDTO.getReason());
        updateTarget.setType(TimeOffType.valueOf(timeOffUpdateDTO.getType()));
        updateTarget.setStartDate(timeOffUpdateDTO.getStartDate());
        updateTarget.setEndDate(timeOffUpdateDTO.getEndDate());

        this.timeOffRepository.save(updateTarget);
        return timeOffMapper.timeOffToResponseDTO(updateTarget);
    }

    @Override
    public void sendTimeOff(UserPrincipal userPrincipal, Long timeOffId) {
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserNotFoundException(userPrincipal.getId()));

        TimeOff timeOff = currentUser.getRequests().stream()
                .filter(timeOffRequest -> timeOffRequest.getId().equals(timeOffId))
                .findFirst().orElseThrow(() -> new TimeOffNotFoundException(timeOffId));


        if (!timeOff.getStatus().equals(TimeOffStatus.CREATED)) {
            throw new TimeOffAlreadySubmittedException(timeOffId);
        }

        List<User> allTeamLeaders = timeOff.getAwaitingApproval();
        List<User> teamLeadersInOffice = timeOff.getAwaitingApproval().stream().filter(User::isInOffice).collect(Collectors.toList());

        if(teamLeadersInOffice.isEmpty()){
            timeOff.setStatus(TimeOffStatus.APPROVED);
            timeOffRepository.save(timeOff);
            allTeamLeaders.forEach(
                    teamLeader -> emailService.sendTimeOffRequestEmail(currentUser, teamLeader, timeOff)
            );
        }
        else {
            timeOff.setAwaitingApproval(teamLeadersInOffice);
            timeOff.setStatus(TimeOffStatus.AWAITING);
            timeOffRepository.save(timeOff);
            teamLeadersInOffice.forEach(
                    approver -> emailService.sendTimeOffRequestEmail(currentUser, approver, timeOff));
        }
    }

    @Override
    public void approveTimeOffRequest(UserPrincipal userPrincipal, Long timeOffId, Boolean answer) {
        TimeOff timeOff = timeOffRepository.findById(timeOffId)
                .orElseThrow(() -> new TimeOffNotFoundException(timeOffId));
        User currentUser = timeOff.getAwaitingApproval().stream()
                .filter(approver -> approver.getId().equals(userPrincipal.getId())).findFirst()
                .orElseThrow(() -> new ApproverNotFoundException(userPrincipal.getName()));

        if (timeOff.getStatus().equals(TimeOffStatus.REJECTED)) {
            throw new TimeOffAlreadySubmittedException(timeOffId);
        }

        if (!answer) {
            timeOff.setStatus(TimeOffStatus.REJECTED);
            timeOff.getAwaitingApproval().clear();
            timeOffRepository.save(timeOff);
            emailService.sendTimeOffRequestEmail(currentUser, timeOff.getCreator(), timeOff);

        } else if (timeOff.getAwaitingApproval().size() > 0) {
            timeOff.getAwaitingApproval().remove(currentUser);

            if (timeOff.getAwaitingApproval().size() == 0) {
                timeOff.setStatus(TimeOffStatus.APPROVED);
                timeOffRepository.save(timeOff);
                userRepository.save(timeOffUtils.reduceDays(timeOff.getCreator(), timeOff));
                emailService.sendTimeOffRequestEmail(currentUser, timeOff.getCreator(), timeOff);
            }
        }
    }

    private TimeOff checkTimeOffRequestCreatedByUser(UserPrincipal userPrincipal, Long timeOffId) {
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserNotFoundException(userPrincipal.getId()));
        return currentUser.getRequests().stream()
                .filter(timeOffRequest -> timeOffRequest.getId().equals(timeOffId))
                .findFirst().orElseThrow(() -> new TimeOffNotFoundException(timeOffId));
    }
}
