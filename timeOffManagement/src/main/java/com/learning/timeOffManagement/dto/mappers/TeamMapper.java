package com.learning.timeOffManagement.dto.mappers;

import com.learning.timeOffManagement.dto.TeamCreationDTO;
import com.learning.timeOffManagement.dto.TeamResponseDTO;
import com.learning.timeOffManagement.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    Team teamDtoToTeam(TeamCreationDTO teamCreationDTO);

    TeamResponseDTO teamToTeamResponseDto(Team team);
}
