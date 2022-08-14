package academy.scalefocus.timeOffManagement.dto.mappers;

import academy.scalefocus.timeOffManagement.dto.UserCreationDTO;
import academy.scalefocus.timeOffManagement.dto.UserResponseDTO;
import academy.scalefocus.timeOffManagement.model.User;
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
