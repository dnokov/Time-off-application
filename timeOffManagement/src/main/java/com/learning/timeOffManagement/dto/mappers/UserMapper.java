package com.learning.timeOffManagement.dto.mappers;

import com.learning.timeOffManagement.dto.UserCreationDTO;
import com.learning.timeOffManagement.dto.UserResponseDTO;
import com.learning.timeOffManagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        imports = java.util.stream.Collectors.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(expression = "java((user" +
            ".getTeams()" +
            ".stream().map(t -> \"Id: \" + t.getId() + \", \" + \"Title: \" + t.getTitle())" +
            ".collect(Collectors.toList())))"
            , target = "teamsInformation")
    UserResponseDTO userToUserDto(User user);

    @Mapping(source = "isAdmin", target = "isAdmin")
    User userDtoToUser(UserCreationDTO userCreationDTO);
}
