package academy.scalefocus.timeOffManagement.controller;

import academy.scalefocus.timeOffManagement.dto.TeamCreationDTO;
import academy.scalefocus.timeOffManagement.dto.TeamResponseDTO;
import academy.scalefocus.timeOffManagement.service.TeamServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(username = "admin", authorities = {"ADMIN"})
public class TeamControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TeamServiceImpl teamService;
    private TeamCreationDTO teamCreationDTO;
    private TeamResponseDTO teamResponseDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        teamCreationDTO = new TeamCreationDTO("Test team", "This is a test team");
        teamResponseDTO = new TeamResponseDTO(1L, "Test team", "This is a test team");
    }

    @Test
    public void shouldReturnAllTeams() throws Exception {
        Mockito.when(teamService.findAllTeams())
                .thenReturn(List.of(new TeamResponseDTO(), new TeamResponseDTO(), new TeamResponseDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get("/teams")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldReturnTeamWithValidData() throws Exception {
        Mockito.when(teamService.findById(1L))
                .thenReturn(teamResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(teamResponseDTO,
                        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TeamResponseDTO.class)));
    }

    @Test
    public void shouldCreateTeamSuccessfullyAndReturnStatusCreated() throws Exception {
        Mockito.when(teamService.save(ArgumentMatchers.any(TeamCreationDTO.class)))
                .thenReturn(teamResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamCreationDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    public void shouldAddMemberAndReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamId}/{userId}",1,1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldUpdateTeamAndReturnUpdatedTitle() throws Exception {
        teamResponseDTO.setTitle("Updated title");
        Mockito.when(teamService.update(ArgumentMatchers.any(Long.class),
                ArgumentMatchers.any(TeamCreationDTO.class)))
                .thenReturn(teamResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamCreationDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals("Updated title",
                        objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TeamResponseDTO.class).getTitle()));
    }

    @Test
    public void shouldSetTeamLeaderWithValidDtaAndReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/teams/1/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldDeleteTeamAndReturnStatusOk() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldDeleteTeamMemberAndReturnStatusOk() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/1/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}