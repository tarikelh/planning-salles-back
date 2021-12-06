package fr.dawan.calendarproject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.InterventionDG2Dto;
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

	List<InterventionDto> listInterventionToListInterventionDto(List<Intervention> interventions);

	List<Intervention> listInterventionDtoToListIntervention(List<InterventionDto> interventionDtos);

	@Mapping(target = "course.id", source = "courseId")
	@Mapping(target = "location.id", source = "locationId")
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "masterIntervention.id", source = "masterInterventionId")
	@Mapping(target = "type", source = "type")
	@Mapping(target = "dateStart", source = "dateStart", dateFormat = "yyyy-MM-dd")
	@Mapping(target = "dateEnd", source = "dateEnd", dateFormat = "yyyy-MM-dd")
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "comment", ignore = true)
	@Mapping(target = "timeStart", ignore = true)
	@Mapping(target = "timeEnd", ignore = true)
	Intervention interventionDG2DtoToIntervention(InterventionDG2Dto iDG2);
}
