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

@Mapper(componentModel = "spring", uses = { UserRepository.class, InterventionRepository.class, InterventionFollowedRepository.class })
public interface InterventionFollowedMapper {


	@Mapping(target = "interventionId", source = "intervention.id")
	@Mapping(target = "studentId", source = "student.id")
	InterventionFollowedDto interventionFollowedToInterventionFollowedDto(InterventionFollowed interventionFollowed);
	
	@Mapping(target = "intervention", ignore = true)
	@Mapping(target = "student", ignore = true)
	InterventionFollowed interventionFollowedDtoToInterventionFollowed(InterventionFollowedDto interventionFollowedDto);
	
	@Mapping(target = "interventionId", source = "intervention.id")
	@Mapping(target = "studentId", source = "student.id")
	List<InterventionFollowedDto> listInterventionFollowedToListInterventionFollowedDto(List<InterventionFollowed> interventionFollowed);
	
	@Mapping(target = "intervention.id", source = "interventionId")
	@Mapping(target = "student.id", source = "studentId")
	List<InterventionFollowed> listInterventionFollowedDtoToListInterventionFollowed(List<InterventionFollowedDto> interventionFollowedDto);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student.idDg2", source = "personId")
	@Mapping(target = "intervention.idDg2", source = "interventionId")
	@Mapping(target = "version", ignore = true)
	InterventionFollowed interventionFollowedDG2DtoToInterventionFollowed(InterventionFollowedDG2Dto interventionFollowedDG2Dto);
}
