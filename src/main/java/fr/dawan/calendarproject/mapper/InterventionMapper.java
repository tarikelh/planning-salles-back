package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.services.CourseService;
import fr.dawan.calendarproject.services.InterventionService;
import fr.dawan.calendarproject.services.LocationService;
import fr.dawan.calendarproject.services.UserService;

@Mapper(componentModel = "spring", uses = { InterventionService.class, LocationService.class, CourseService.class,
		UserService.class })
public interface InterventionMapper {

	@Mappings({ @Mapping(target = "courseId", source = "course.id"),
			@Mapping(target = "locationId", source = "location.id"), 
			@Mapping(target = "userId", source = "user.id"),
			@Mapping(target = "masterInterventionId", source = "masterIntervention.id"), })
	InterventionDto interventionToInterventionDto(Intervention intervention);

	@Mappings({ @Mapping(target = "course.id", source = "courseId"),
			@Mapping(target = "location.id", source = "locationId"), 
			@Mapping(target = "user.id", source = "userId"),
			@Mapping(target = "masterIntervention.id", source = "masterInterventionId"),
			@Mapping(target = "type", source = "type") })
	Intervention interventionDtoToIntervention(InterventionDto intervention);

}
