package academy.scalefocus.timeOffManagement.dto.mappers;

import academy.scalefocus.timeOffManagement.dto.TeamCreationDTO;
import academy.scalefocus.timeOffManagement.dto.TeamResponseDTO;
import academy.scalefocus.timeOffManagement.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    Team teamDtoToTeam(TeamCreationDTO teamCreationDTO);

    TeamResponseDTO teamToTeamResponseDto(Team team);
}
