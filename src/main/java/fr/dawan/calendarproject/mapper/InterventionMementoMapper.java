package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.entities.Intervention;

@Mapper(componentModel = "spring")
public interface InterventionMementoMapper {

	@Mapping(target = "locationIdDg2", source = "location.idDg2")
	@Mapping(target = "courseIdDg2", source = "course.idDg2")
	@Mapping(target = "interventionId", source = "id")
	@Mapping(target = "courseId", source = "course.id")
	@Mapping(target = "locationId", source = "location.id")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "masterInterventionId", source = "masterIntervention.id")
	@Mapping(target = "locationCity", source = "location.city")
	@Mapping(target = "courseTitle", source = "course.title")
	@Mapping(target = "userFullName", source = "user.fullname")
	@Mapping(target = "type", source = "type")
	InterventionMementoDto interventionToInterventionMementoDto(Intervention intervention);

	@Mapping(target = "id", source = "interventionId")
	@Mapping(target = "course.id", source = "courseId")
	@Mapping(target = "location.id", source = "locationId")
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "masterIntervention.id", source = "masterInterventionId")
	@Mapping(target = "type", source = "type")
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "interventionsFollowed", ignore = true)
	Intervention interventionMementoDtoToIntervention(InterventionMementoDto iMemDto);
}
