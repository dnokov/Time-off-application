package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.UserCreationDTO;
import com.learning.timeOffManagement.dto.UserResponseDTO;
import com.learning.timeOffManagement.dto.mappers.UserMapper;
import com.learning.timeOffManagement.exception.UserNotFoundException;
import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.repository.UserRepository;
import com.learning.timeOffManagement.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserMapper userMapper = UserMapper.INSTANCE;
    private User adminUser;
    private UserCreationDTO adminDTO;
    private User user;
    private UserCreationDTO userDTO;
    private UserResponseDTO responseDTO;
    private Long adminUserId = 1L;
    private Long userId = 2L;

    private UserPrincipal userPrincipal;

    @BeforeEach
    public void setUp() {
        adminUser = new User("admin", "adminpass", "First name", "Last name", null, true, "admin@abv.bg");
        adminUser.setId(adminUserId);

        user = new User("testUser", "testPass", "Ivan", "Ivanov", adminUserId, false, "test@abv.bg");
        user.setId(userId);
        adminDTO = new UserCreationDTO("admin", "adminpass", "First name",
                "Last name", "admin@abv.bg", true);
        userDTO = new UserCreationDTO("testUser", "testPass", "Ivan",
                "Ivanov", "test@abv.bg", false);
        responseDTO = new UserResponseDTO(2L, "testUser", "Ivan",
                "Ivanov", LocalDate.now(), LocalDate.now(), 1L, false,
                20, 90, 40, "test@abv.bg", true, new ArrayList<>(), true);
        userPrincipal = new UserPrincipal(1L, "admin", true);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    }

    @Test
    public void findByIdShouldReturnUserDTO() {

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        assertEquals(userService.findById(userId).getUsername(), responseDTO.getUsername());
    }

    @Test
    public void shouldThrowUserNotFoundWhenNoIdIsPassed() {
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    public void shouldReturnUserResponseDTOWhenFindByUserName() {
        responseDTO.setUsername("admin");
        given(userRepository.findByUsername(adminDTO.getUsername())).willReturn(Optional.of(adminUser));
        assertEquals(responseDTO.getUsername(), userService.findByUsername("admin").getUsername());

    }

    @Test
    public void shouldThrowExceptionWhenFindByUserName() {

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername("Testing"));

    }

    @Test
    public void shouldReturnUserResponseDTOWhenFindByEmail() {

        given(userRepository.findByEmail(adminDTO.getEmail())).willReturn(Optional.of(adminUser));
        UserResponseDTO response = userService.findByEmail("admin@abv.bg");
        assertEquals(adminDTO.getEmail(), response.getEmail());

    }

    @Test
    public void shouldThrowExceptionWhenFindByEmail() {

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("Testing"));

    }

    @Test
    public void shouldReturnListOfUserResponseDTOWhenFindAll() {

        List<User> tempUsers = new ArrayList<>();
        tempUsers.add(adminUser);
        tempUsers.add(user);
        tempUsers.add(new User());
        tempUsers.add(new User());
        tempUsers.add(new User());
        tempUsers.add(new User());

        when(userRepository.findAll()).thenReturn(tempUsers);

        assertEquals(6, userService.findAllUsers().size());
        assertEquals(userService.findAllUsers().get(1).getClass(),UserResponseDTO.class);
    }

    @Test
    public void shouldSaveUserDTO() {
        userDTO.setPassword(new BCryptPasswordEncoder().encode("testPass"));
        given(userRepository.findByUsername(userDTO.getUsername())).willReturn(Optional.of(userMapper.userDtoToUser(userDTO)));

        assertEquals("testUser", userService.findByUsername("testUser").getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("testPass", userDTO.getPassword()));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUserNameTaken() {

        UserCreationDTO testUser = new UserCreationDTO();
        testUser.setUsername("admin");
        testUser.setPassword("awdadawd");
        testUser.setFirstName("test name first");
        testUser.setLastName("test name last");
        testUser.setIsAdmin(false);
        testUser.setEmail("test1@abv.bg");

        given(userRepository.findByUsername(testUser.getUsername())).willReturn(
                Optional.of(userMapper.userDtoToUser(testUser)));

        assertThrows(IllegalArgumentException.class, () -> userService.save(testUser));
    }

    @Test
    public void shouldUpdateUserDTO () {
        User testUser = new User();
        testUser.setId(5L);
        testUser.setUsername("test Username");
        testUser.setPassword("adawdawdda");
        testUser.setFirstName("test name first");
        testUser.setLastName("test name last");
        testUser.setCreatorId(adminUserId);
        testUser.setEditedBy(adminUserId);
        testUser.setIsAdmin(false);
        testUser.setEmail("test@abv.bg");
        testUser.setDateOfCreation(LocalDate.now());
        testUser.setDateOfLastChange(LocalDate.now());

        UserCreationDTO test = new UserCreationDTO();
        test.setUsername("testUser");
        test.setPassword("adawdawdda");
        test.setFirstName("Ivan");
        test.setLastName("Ivanov");
        test.setEmail("test@abv.bg");
        test.setIsAdmin(false);

        given(userRepository.findById(5L)).willReturn(Optional.of(testUser));

        userService.update(testUser.getId(),test);
        then(userRepository).should().save(testUser);
    }

    @Test
    public void deleteShouldDeletedUser() throws UserNotFoundException {
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.delete(userId);

        then(userRepository).should().deleteById(userId);

    }

    @Test
    public void deleteShouldThrowExceptionWhenInvalidId() {
        assertThrows(UserNotFoundException.class, () -> userService.delete(25L));
    }

    @Test
    public void shouldSetUserAsAdmin() {
        User expectedUser = user;
        expectedUser.setIsAdmin(true);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserResponseDTO actualUser = userService.setAdminUser(user.getId());

        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    public void shouldUnemployUserWhenValidIdIsPassed(){
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
       UserResponseDTO response = userService.unemploy(userId);
        then(userRepository).should().save(user);

        assertEquals("former",response.getUsername());
    }
}
