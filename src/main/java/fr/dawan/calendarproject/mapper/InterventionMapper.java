package fr.dawan.calendarproject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.InterventionDG2Dto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Mapper(componentModel = "spring", uses = { CourseRepository.class, LocationRepository.class, UserRepository.class,
		InterventionRepository.class })
public interface InterventionMapper {

	@Mapping(target = "courseId", source = "course.id")
	@Mapping(target = "locationId", source = "location.id")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "masterInterventionId", source = "masterIntervention.id")
	InterventionDto interventionToInterventionDto(Intervention intervention);

	@Mapping(target = "idDg2", ignore = true)
	@Mapping(target = "course", source = "courseId")
	@Mapping(target = "location", source = "locationId")
	@Mapping(target = "user", source = "userId")
	@Mapping(target = "masterIntervention", source = "masterInterventionId")
	@Mapping(target = "type", source = "type")
	Intervention interventionDtoToIntervention(InterventionDto intervention);

	List<InterventionDto> listInterventionToListInterventionDto(List<Intervention> interventions);

	List<Intervention> listInterventionDtoToListIntervention(List<InterventionDto> interventionDtos);

	@Mapping(target = "idDg2", source = "id")
	@Mapping(target = "type", source = "type")
	@Mapping(target = "dateStart", source = "dateStart", dateFormat = "yyyy-MM-dd")
	@Mapping(target = "dateEnd", source = "dateEnd", dateFormat = "yyyy-MM-dd")
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "location", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "masterIntervention", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "comment", ignore = true)
	@Mapping(target = "timeStart", ignore = true)
	@Mapping(target = "timeEnd", ignore = true)
	Intervention interventionDG2DtoToIntervention(InterventionDG2Dto iDG2);

	List<Intervention> listInterventionDG2DtotoListIntervention(List<InterventionDG2Dto> interventionDG2Dtos);
}
