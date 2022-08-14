package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.dto.LoginRequestDTO;
import academy.scalefocus.timeOffManagement.exception.LoginException;
import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {
        User user;
        if (loginRequestDTO.getEmail().equals("admin")) {
            user = userRepository.findByUsername("admin")
                    .orElseThrow(() -> new LoginException("user"));
        } else {
            user = userRepository.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new LoginException("email"));
        }
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new LoginException("password");
        }
        return tokenService.generateToken(user);
    }
}
