package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;

@Mapper(componentModel = "spring")
public interface InterventionMapper {

	@Mapping(target = "courseId", source = "course.id")
	@Mapping(target = "locationId", source = "location.id")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "masterInterventionId", source = "masterIntervention.id")
	InterventionDto interventionToInterventionDto(Intervention intervention);

	@Mapping(target = "course.id", source = "courseId")
	@Mapping(target = "location.id", source = "locationId")
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "masterIntervention.id", source = "masterInterventionId")
	@Mapping(target = "type", source = "type")
	Intervention interventionDtoToIntervention(InterventionDto intervention);

}
