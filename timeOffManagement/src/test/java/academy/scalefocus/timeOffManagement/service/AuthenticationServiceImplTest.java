package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.dto.LoginRequestDTO;
import academy.scalefocus.timeOffManagement.exception.LoginException;
import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    private static LoginRequestDTO loginRequestDTO;
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6Miwic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNjI1MzI4OTkzfQ.uvH_3xw-xiSGjrXbrw9upG_9nDNRYoRBSUoYX2XDGs0";
    private static User user;

    @BeforeAll
    public static void setUpUsers(){
        loginRequestDTO = new LoginRequestDTO("admin", "adminpass");
        user = new User("admin","adminpass",
                "Admin","admin", null,true,"admin@admin.com");
        user.setId(1L);
    }
    @Test
    public void loginTestShouldThrowLoginExceptionWhenEmailIsInvalid(){
        loginRequestDTO.setEmail("wrongMail@mail.com");
        Mockito.when(userRepository.findByEmail("wrongMail@mail.com"))
                .thenThrow(new LoginException("Invalid Email"));
       Assertions.assertThrows(LoginException.class, () -> authenticationService.login(loginRequestDTO));
    }

    @Test
    public void loginTestShouldThrowLoginExceptionWhenPasswordIsInvalid(){
        loginRequestDTO.setPassword("wrongpass");
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        Assertions.assertThrows(LoginException.class, () -> authenticationService.login(loginRequestDTO));
    }

    @Test
    public void loginTestShouldRunWithoutExceptions(){
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(loginRequestDTO.getPassword(),user.getPassword()))
                .thenReturn(true);
        Mockito.when(tokenService.generateToken(user)).thenReturn(token);
        String parsedToken = authenticationService.login(loginRequestDTO);
        Assertions.assertEquals(token,parsedToken);
    }
}
