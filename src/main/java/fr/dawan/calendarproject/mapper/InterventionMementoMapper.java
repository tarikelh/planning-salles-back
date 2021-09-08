package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.entities.Intervention;

@Mapper(componentModel = "spring")
public interface InterventionMementoMapper {

	@Mappings({ @Mapping(target = "interventionId", source = "id"), @Mapping(target = "courseId", source = "course.id"),
			@Mapping(target = "locationId", source = "location.id"), @Mapping(target = "userId", source = "user.id"),
			@Mapping(target = "masterInterventionId", source = "masterIntervention.id"),
			@Mapping(target = "locationCity", source = "location.city"),
			@Mapping(target = "courseTitle", source = "course.title"),
			@Mapping(target = "userFullName", source = "user.fullname"),
			@Mapping(target = "userEmail", source = "user.email"), @Mapping(target = "type", source = "type") })
	InterventionMementoDto interventionToInterventionMementoDto(Intervention intervention);

	@Mappings({ @Mapping(target = "id", source = "interventionId"), @Mapping(target = "course.id", source = "courseId"),
			@Mapping(target = "location.id", source = "locationId"), @Mapping(target = "user.id", source = "userId"),
			@Mapping(target = "masterIntervention.id", source = "masterInterventionId"),
			@Mapping(target = "enumType", source = "type"), @Mapping(target = "version", ignore = true) })
	Intervention interventionMementoDtoToIntervention(InterventionMementoDto iMemDto);

//	Long InterventionToLong(Intervention interv);
}
