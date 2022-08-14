package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.dto.TimeOffCreationDTO;
import academy.scalefocus.timeOffManagement.dto.TimeOffResponseDTO;
import academy.scalefocus.timeOffManagement.dto.TimeOffUpdateDTO;
import academy.scalefocus.timeOffManagement.security.UserPrincipal;

public interface TimeOffService {
    TimeOffResponseDTO addTimeOff(UserPrincipal userPrincipal, TimeOffCreationDTO timeOffCreationDTO);

    TimeOffResponseDTO getTimeOff(UserPrincipal userPrincipal, Long timeOffId);

    TimeOffResponseDTO deleteTimeOffRequest(UserPrincipal userPrincipal, Long timeOffId);

    TimeOffResponseDTO updateTimeOff(UserPrincipal userPrincipal, Long timeOffId, TimeOffUpdateDTO timeOffUpdateDTO);

    void sendTimeOff(UserPrincipal userPrincipal, Long timeOffId);

    void approveTimeOffRequest(UserPrincipal userPrincipal, Long timeOffId, Boolean answer);
}
