package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.dto.LoginRequestDTO;

public interface AuthenticationService {
    String login(LoginRequestDTO loginRequestDTO);
}
