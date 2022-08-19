package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.dto.UserCreationDTO;
import com.learning.timeOffManagement.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO findById (Long id);

    UserResponseDTO findByUsername (String usernamer);

    UserResponseDTO findByEmail (String email);

    List<UserResponseDTO> findAllUsers ();

    UserResponseDTO save (UserCreationDTO userDTOtoSave);

    UserResponseDTO update (Long id, UserCreationDTO userDTOtoUpdate);

    void delete (Long id);

    UserResponseDTO setAdminUser (Long id);

    UserResponseDTO unemploy(Long id);
}
