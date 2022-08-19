package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.TimeOffCreationDTO;
import com.learning.timeOffManagement.dto.TimeOffResponseDTO;
import com.learning.timeOffManagement.dto.TimeOffUpdateDTO;
import com.learning.timeOffManagement.security.UserPrincipal;

public interface TimeOffService {
    TimeOffResponseDTO addTimeOff(UserPrincipal userPrincipal, TimeOffCreationDTO timeOffCreationDTO);

    TimeOffResponseDTO getTimeOff(UserPrincipal userPrincipal, Long timeOffId);

    TimeOffResponseDTO deleteTimeOffRequest(UserPrincipal userPrincipal, Long timeOffId);

    TimeOffResponseDTO updateTimeOff(UserPrincipal userPrincipal, Long timeOffId, TimeOffUpdateDTO timeOffUpdateDTO);

    void sendTimeOff(UserPrincipal userPrincipal, Long timeOffId);

    void approveTimeOffRequest(UserPrincipal userPrincipal, Long timeOffId, Boolean answer);
}
