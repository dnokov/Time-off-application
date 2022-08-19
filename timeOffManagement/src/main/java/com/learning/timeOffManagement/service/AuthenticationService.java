package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.LoginRequestDTO;

public interface AuthenticationService {
    String login(LoginRequestDTO loginRequestDTO);
}
