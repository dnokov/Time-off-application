package com.learning.timeOffManagement.dto.mappers;

import com.learning.timeOffManagement.dto.TimeOffCreationDTO;
import com.learning.timeOffManagement.dto.TimeOffResponseDTO;
import com.learning.timeOffManagement.dto.TimeOffUpdateDTO;
import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
imports = java.util.stream.Collectors.class)
public interface TimeOffMapper {
    TimeOffMapper INSTANCE = Mappers.getMapper(TimeOffMapper.class);

    @Mapping(expression = "java((user.getTeamLeaders()))",
            target = "awaitingApproval")
    @Mapping( target = "creator", source = "user." )
    TimeOff creationDTOtoTimeOff(TimeOffCreationDTO timeOffCreationDTO, User user);

    @Mapping( target = "creator", source = "user." )
    @Mapping( target = "id", source = "timeOffUpdateDTO.id" )
    @Mapping(expression = "java((user.getTeamLeaders()))",
            target = "awaitingApproval")
    TimeOff updateDTOtoTimeOff(TimeOffUpdateDTO timeOffUpdateDTO, User user);

    @Mapping( expression = "java((timeOff" +
            ".getAwaitingApproval()" +
            ".stream().map(u -> u.getFirstName() + \" \" + u.getLastName())" +
            ".collect(Collectors.toList())))"
            , target = "awaitingApproval")
    @Mapping( expression = "java((timeOff.getCreator().getFirstName() + \" \" + timeOff.getCreator().getLastName()))"
            , target = "creator")
    TimeOffResponseDTO timeOffToResponseDTO(TimeOff timeOff);
}
