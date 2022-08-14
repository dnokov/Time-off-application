package academy.scalefocus.timeOffManagement.controller;

import academy.scalefocus.timeOffManagement.dto.LoginRequestDTO;
import academy.scalefocus.timeOffManagement.exception.LoginException;
import academy.scalefocus.timeOffManagement.service.AuthenticationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
public class LoginControllerTest {
    
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AuthenticationServiceImpl authenticationService;
    private static LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public  void setUpUsers() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        loginRequestDTO = new LoginRequestDTO("admin", "adminpass");
    }

    @Test
    public void loginUserTestShouldReturn200WithValidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void loginUserShouldReturnNotFoundCodeWithInvalidEmail() throws Exception {
        loginRequestDTO.setEmail("notAnAdmin@mail.com");

        Mockito.when(authenticationService.login(ArgumentMatchers.any(LoginRequestDTO.class)))
                .thenThrow(new LoginException("email"));
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResolvedException() instanceof
                        LoginException))
                .andExpect(mvcResult -> Assertions.assertEquals("Invalid email",
                        mvcResult.getResolvedException().getMessage()));
    }

    @Test
    public void loginUserShouldReturnNotFoundCodeWithInvalidPassword() throws Exception {
        loginRequestDTO.setPassword("wrongPassword");

        Mockito.when(authenticationService.login(ArgumentMatchers.any(LoginRequestDTO.class)))
                .thenThrow(new LoginException("password"));
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResolvedException() instanceof
                        LoginException))
                .andExpect(mvcResult -> Assertions.assertEquals("Invalid password",
                        mvcResult.getResolvedException().getMessage()));
    }
}
