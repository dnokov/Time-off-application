package academy.scalefocus.timeOffManagement.controller;

import academy.scalefocus.timeOffManagement.dto.UserCreationDTO;
import academy.scalefocus.timeOffManagement.dto.UserResponseDTO;
import academy.scalefocus.timeOffManagement.dto.mappers.UserMapper;
import academy.scalefocus.timeOffManagement.exception.UserNotFoundException;
import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(username = "admin", authorities = {"ADMIN"})
public class UsersControllerTests {

    private MockMvc mockMvc;
    private User tempUser;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    private UserMapper userMapper = UserMapper.INSTANCE;

    private UserCreationDTO userCreationDTO;

    private UserResponseDTO responseDTO;

    @BeforeEach
    public void setUp() {
        tempUser = new User("testUser", "testPass", "Ivan", "Ivanov", 1L, false, "test@abv.bg");
        tempUser.setId(2L);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userCreationDTO = new UserCreationDTO("testUserName", "testPassword",
            "Pesho", "Peshov", "peshov@ggg.bg", false);
        responseDTO = new UserResponseDTO(2L, "testUserName", "Pesho",
                "Peshov", LocalDate.now(), LocalDate.now(), 1L, false,
                20, 90, 40, "peshov@ggg.bg", true, new ArrayList<>(), true);
    }

    @SneakyThrows
    @Test
    public void shouldCreateUserAndReturnStatusCreated() {
        Mockito.when(userService.save(ArgumentMatchers.any(UserCreationDTO.class)))
            .thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userCreationDTO)))
            .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    public void shouldGetAllUsers() {
        List<UserResponseDTO> users = Arrays.asList(new UserResponseDTO(),
                new UserResponseDTO(), new UserResponseDTO());

        Mockito.when(userService.findAllUsers())
            .thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @Test
    public void shouldDeleteUser() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @Test
    public void shouldGetUserById() {
        Mockito.when(userService.findById(ArgumentMatchers.any(Long.class)))
            .thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{1}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @Test
    public void shouldReturnNotFoundWhenGetUserById() {
        long id = 5L;

        Mockito.when(userService.findById(ArgumentMatchers.any(Long.class)))
            .thenThrow(new UserNotFoundException(id));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{1}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void shouldUpdateUser() {

        UserCreationDTO updatedUser = new UserCreationDTO();

        updatedUser.setUsername("editedUsername");
        updatedUser.setPassword("editedPassword");
        updatedUser.setFirstName("editedFirstName");
        updatedUser.setLastName("editedLasName");
        updatedUser.setEmail("edited@aaa.bg");
        updatedUser.setIsAdmin(false);

        responseDTO.setUsername("editedUsername");
        responseDTO.setFirstName("editedFirstName");
        responseDTO.setLastName("editedLasName");
        responseDTO.setEmail("edited@aaa.bg");
        responseDTO.setIsAdmin(false);

        Mockito.when(userService.update(1L,
            updatedUser)).thenReturn(responseDTO);

        mockMvc.perform(put("/users/{1}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void shouldSetUnemployed() {

        tempUser = new User();

        tempUser.setUsername("former");
        tempUser.setPassword("former");
        tempUser.setFirstName("former");
        tempUser.setLastName("former");
        tempUser.setEmail("former@formeremployee.com");
        tempUser.setEmployed(false);

        Mockito.when(userService.unemploy(1L)).thenReturn(userMapper.userToUserDto(tempUser));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/unemploy")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tempUser)))
            .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void shouldSetUserAsAdmin(){
        Mockito.when(userService.setAdminUser(ArgumentMatchers.any(Long.class)))
                .thenReturn(userMapper.userToUserDto(tempUser));

        tempUser.setIsAdmin(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/setadmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempUser)))
                .andExpect(status().isOk());
    }
}