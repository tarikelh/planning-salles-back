package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.services.CourseServiceImpl;
import fr.dawan.calendarproject.services.InterventionServiceImpl;
import fr.dawan.calendarproject.services.LocationServiceImpl;
import fr.dawan.calendarproject.services.UserServiceImpl;

@Mapper(componentModel = "spring", uses = { InterventionServiceImpl.class,
		LocationServiceImpl.class , CourseServiceImpl.class, UserServiceImpl.class})
public interface InterventionMapper {
	
	@Mappings({
		@Mapping(target = "courseId", source = "course.id"),
		@Mapping(target = "locationId", source = "location.id"),
		@Mapping(target = "userId", source = "user.id"),
		@Mapping(target = "masterInterventionId", source = "masterIntervention.id"),
	})
	InterventionDto interventionToInterventionDto(Intervention intervention);
	
	@Mappings({
		@Mapping(target = "course.id", source = "courseId"),
		@Mapping(target = "location.id", source = "locationId"),
		@Mapping(target = "user.id", source = "userId"),
		@Mapping(target = "masterIntervention.id", source = "masterInterventionId"),
		@Mapping(target = "enumType", source = "type")
	})
	Intervention interventionDtoToIntervention(InterventionDto intervention);
	
	@Mappings({
		@Mapping(target = "id", source = "interventionId"),
		@Mapping(target = "course.id", source = "courseId"),
		@Mapping(target = "location.id", source = "locationId"),
		@Mapping(target = "user.id", source = "userId"),
		@Mapping(target = "masterIntervention.id", source = "masterInterventionId"),
		@Mapping(target = "enumType", source = "type"),
		@Mapping(target = "version", ignore = true)
	})
	Intervention interventionMementoDtoToIntervention(InterventionMementoDto iMemDto);
	
	@Mappings({
		@Mapping(target = "interventionId", source = "id"),
		@Mapping(target = "courseId", source = "course.id"),
		@Mapping(target = "locationId", source = "location.id"),
		@Mapping(target = "userId", source = "user.id"),
		@Mapping(target = "masterInterventionId", source = "masterIntervention.id"),
		@Mapping(target = "locationCity", source = "location.city"),
		@Mapping(target = "courseTitle", source = "course.title"),
		@Mapping(target = "userFullName", source = "user.fullname"),
		@Mapping(target = "userEmail", source = "user.email"),
		@Mapping(target = "type", source = "type")
	})
	InterventionMementoDto interventionToInterventionMementoDto(Intervention intervention);

//	@Mapping(source="course.id", target="courseId")
//	@Mapping(source="location.id", target="locationId")
//	@Mapping(source="masterIntervention.id", target="masterInterventionId")
//	@Mapping(source="user.id", target="userId")
//	InterventionDto interventionToInterventionDto(Intervention interv);
//	
//	@Mapping(source="courseId", target="course")
//	@Mapping(source="locationId", target="location")
//	@Mapping(source="masterInterventionId", target="masterIntervention")
//	@Mapping(source="userId", target="user")
//	@Mapping(source = "type", target = "enumType")
//	Intervention interventionDtoToIntervention(InterventionDto intervDto);
//	
//	@Mapping(source="course.id", target="courseId")
//	@Mapping(source="location.id", target="locationId")
//	@Mapping(source="masterIntervention.id", target="masterInterventionId")
//	@Mapping(source="user.id", target="userId")
//	@Mapping(source="id", target ="interventionId")
//	InterventionMementoDto InterventionMementoDtoToIntervention(Intervention interv);
}
