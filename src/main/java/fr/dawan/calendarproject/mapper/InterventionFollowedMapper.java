package fr.dawan.calendarproject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.InterventionFollowedDG2Dto;
import fr.dawan.calendarproject.dto.InterventionFollowedDto;
import fr.dawan.calendarproject.entities.InterventionFollowed;
import fr.dawan.calendarproject.repositories.InterventionFollowedRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Mapper(componentModel = "spring", uses = { UserRepository.class, InterventionRepository.class,
		InterventionFollowedRepository.class, InterventionMapper.class, UserMapper.class })
public interface InterventionFollowedMapper {

	@Mapping(target = "userDto", source = "user")
	@Mapping(target = "advInterventionDto", source = "intervention")
	InterventionFollowedDto interventionFollowedToInterventionFollowedDto(InterventionFollowed interventionFollowed);

	@Mapping(target = "user", source = "userDto")
	@Mapping(target = "intervention", source = "advInterventionDto")
	InterventionFollowed interventionFollowedDtoToInterventionFollowed(InterventionFollowedDto interventionFollowedDto);

	List<InterventionFollowedDto> listInterventionFollowedToListInterventionFollowedDto(
			List<InterventionFollowed> interventionFollowed);


	List<InterventionFollowed> listInterventionFollowedDtoToListInterventionFollowed(
			List<InterventionFollowedDto> interventionFollowedDto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user.idDg2", source = "personId")
	@Mapping(target = "intervention.idDg2", source = "interventionId")
	@Mapping(target = "version", ignore = true)
	InterventionFollowed interventionFollowedDG2DtoToInterventionFollowed(
			InterventionFollowedDG2Dto interventionFollowedDG2Dto);
}
