package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.UserCreationDTO;
import com.learning.timeOffManagement.dto.UserResponseDTO;
import com.learning.timeOffManagement.dto.mappers.UserMapper;
import com.learning.timeOffManagement.exception.FormerUserException;
import com.learning.timeOffManagement.exception.UserNotFoundException;
import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.repository.UserRepository;
import com.learning.timeOffManagement.utils.UserPrincipalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToUserDto(user);
    }

    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return userMapper.userToUserDto(user);
    }

    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.userToUserDto(user);
    }

    public List<UserResponseDTO> findAllUsers() {
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            userResponseDTOList.add(userMapper.userToUserDto(user));
        }

        return userResponseDTOList;
    }

    public UserResponseDTO save(UserCreationDTO userDTOtoSave) {

        if (userRepository.findByUsername(userDTOtoSave.getUsername()).isPresent()) {
            throw new IllegalArgumentException("This username already exists!");
        }

        if (userRepository.findByEmail(userDTOtoSave.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email is already taken!");
        }

        User tempUser = userMapper.userDtoToUser(userDTOtoSave);
        tempUser.setPassword(passwordEncoder.encode(userDTOtoSave.getPassword()));
        tempUser.setCreatorId(UserPrincipalUtils.getPrincipalFromContext().getId());

        userRepository.save(tempUser);

        return userMapper.userToUserDto(tempUser);
    }

    public UserResponseDTO update(Long id, UserCreationDTO userDTOtoUpdate) {

        User tempUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!userDTOtoUpdate.getUsername().equals(tempUser.getUsername())) {
            Boolean ifExists = userRepository.existsByUsername(userDTOtoUpdate.getUsername());
            if (ifExists) {
                throw new IllegalArgumentException("This username already exists!");
            }

            tempUser.setUsername(userDTOtoUpdate.getUsername());
        }

        if (!userDTOtoUpdate.getEmail().equals(tempUser.getEmail())) {
            Boolean ifExists = userRepository.findByEmail(userDTOtoUpdate.getEmail()).isPresent();
            if (ifExists) {
                throw new IllegalArgumentException("This email is already taken!");
            }

            tempUser.setEmail(userDTOtoUpdate.getEmail());
        }

        tempUser.setPassword(userDTOtoUpdate.getPassword());
        tempUser.setFirstName(userDTOtoUpdate.getFirstName());
        tempUser.setLastName(userDTOtoUpdate.getLastName());
        tempUser.setIsAdmin(userDTOtoUpdate.getIsAdmin());
        tempUser.setEditedBy(UserPrincipalUtils.getPrincipalFromContext().getId());
        tempUser.setDateOfLastChange(LocalDate.now());

        return userMapper.userToUserDto(userRepository.save(tempUser));
    }

    public void delete(Long id) {

        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public UserResponseDTO unemploy(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresentOrElse(u -> {
                    u.setUsername("former");
                    u.setPassword("former");
                    u.setFirstName("former");
                    u.setLastName("former");
                    u.setEmail("former@formeremployee.com");
                    u.setEmployed(false);
                    u.setInOffice(false);
                },
                () -> {
                    throw new UserNotFoundException(id);
                });
        userRepository.save(userOptional.get());
        return userMapper.userToUserDto(userOptional.get());
    }

    public UserResponseDTO setAdminUser(Long id) {
        User tempUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!tempUser.isEmployed()) {
            throw new FormerUserException(id);
        }

        tempUser.setIsAdmin(true);
        userRepository.save(tempUser);
        return userMapper.userToUserDto(tempUser);
    }
}
