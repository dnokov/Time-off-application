package com.learning.timeOffManagement.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.learning.timeOffManagement.dto.TimeOffCreationDTO;
import com.learning.timeOffManagement.dto.TimeOffResponseDTO;
import com.learning.timeOffManagement.dto.mappers.TimeOffMapper;
import com.learning.timeOffManagement.model.TimeOffStatus;
import com.learning.timeOffManagement.model.TimeOffType;
import com.learning.timeOffManagement.security.UserPrincipal;
import com.learning.timeOffManagement.service.TimeOffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
public class TimeOffControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private UserPrincipal userPrincipal;

    @MockBean
    private TimeOffService timeOffService;

    @Autowired
    private TimeOffMapper timeOffMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private TimeOffCreationDTO timeOffCreationDTO;

    private TimeOffResponseDTO timeOffResponseDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        timeOffCreationDTO = new TimeOffCreationDTO(TimeOffType.PAID.toString(),
            "vacation", LocalDate.now(), LocalDate.now());
        timeOffResponseDTO = new TimeOffResponseDTO(5L, TimeOffType.PAID.toString(),
            "vacation", TimeOffStatus.CREATED.toString(),
            LocalDate.now(), LocalDate.now(),
            "Joro", new ArrayList<>());

        userPrincipal = new UserPrincipal(1L, "admin", true);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

    }

    @SneakyThrows
    @org.junit.jupiter.api.Test
    public void shouldAddTimeOff() {

        when(timeOffService.addTimeOff(ArgumentMatchers.any(UserPrincipal.class),
            ArgumentMatchers.any(TimeOffCreationDTO.class)))
            .thenReturn(timeOffResponseDTO);

        mockMvc.perform(post("/timeOffs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(timeOffCreationDTO))
            .principal(userPrincipal))
            .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    public void shouldGetTimeOff() {
        when(timeOffService.getTimeOff(ArgumentMatchers.any(UserPrincipal.class),
            ArgumentMatchers.any(Long.class)))
            .thenReturn(timeOffResponseDTO);

        mockMvc.perform(get("/timeOffs/1")
            .contentType(MediaType.ALL)
            .principal(userPrincipal))
            .andExpect(status().is2xxSuccessful());
    }
}