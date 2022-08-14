package academy.scalefocus.timeOffManagement.controller;

import academy.scalefocus.timeOffManagement.dto.UserCreationDTO;
import academy.scalefocus.timeOffManagement.dto.UserResponseDTO;
import academy.scalefocus.timeOffManagement.dto.mappers.UserMapper;
import academy.scalefocus.timeOffManagement.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
@Api(tags = "Users")
public class UsersController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping
    @ApiOperation("Create a new user")
    public ResponseEntity<?> createUser(UriComponentsBuilder ucBuilder, @Valid @RequestBody UserCreationDTO userCreationDTO) {
        UserResponseDTO savedUserDTO = userService.save(userCreationDTO);
        return ResponseEntity.created(ucBuilder.path("/users/{userId}").buildAndExpand(savedUserDTO.getId()).toUri()).build();
    }

    @GetMapping
    @ApiOperation("Get all the users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> allUsers = userService.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a user")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a specific user")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("Modify the information for a specific user")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreationDTO updatedUserDTO) {
        return new ResponseEntity<>(userService.update(id, updatedUserDTO), HttpStatus.OK);
    }

    @PostMapping("/{id}/unemploy")
    @ApiOperation("When a user quits, he should be marked as an unemployed")
    public ResponseEntity<UserResponseDTO> unemploy(@PathVariable Long id) {
        return new ResponseEntity<>(userService.unemploy(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/setadmin")
    @ApiOperation("Set a regular user to be an admin")
    public ResponseEntity<UserResponseDTO> setAdmin(@PathVariable Long id){
        return new ResponseEntity<>(userService.setAdminUser(id),HttpStatus.OK);
    }
}
