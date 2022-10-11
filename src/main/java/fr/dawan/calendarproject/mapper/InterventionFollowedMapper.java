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

//	@Mapping(target = "userDto.locationId", source = "user.location.id")
//	@Mapping(target = "interventionDto.courseIdDg2", source = "intervention.course.idDg2")
//	@Mapping(target = "interventionDto.locationId", source = "intervention.location.id")
//	@Mapping(target = "interventionDto.locationIdDg2", source = "intervention.location.idDg2")
//	@Mapping(target = "interventionDto.masterInterventionId", source = "intervention.masterIntervention.id")
	@Mapping(target = "userDto", source = "user")
	@Mapping(target = "interventionDto", source = "intervention")
	InterventionFollowedDto interventionFollowedToInterventionFollowedDto(InterventionFollowed interventionFollowed);

//	"interventionsFollowed, masterInterventionIdTemp, optionSlug, user". Mapping from property "InterventionDto intervention" to "Intervention 
//	 intervention".
//	- Unmapped target properties: "enumCompany, enumType, interventionsFollowed, location, skills". Mapping from property "UserDto user" to "User user".

//	@Mapping(target = "user.enumType", source = "userDto.type")
//	@Mapping(target = "user.enumCompany", source = "userDto.company")
//	@Mapping(target = "user.location.id", source = "userDto.locationId")
//	@Mapping(target = "user.skills", ignore = true)
//	
//	@Mapping(target = "intervention.course.id", source = "interventionDto.courseId")
//	@Mapping(target = "intervention.location.id", source = "interventionDto.locationId")
//	@Mapping(target = "intervention.masterIntervention.id", source = "interventionDto.masterInterventionId")
//	
//	@Mapping(target = "interventioninterventionsFollowed", source = "interventionDto.courseId")
//	@Mapping(target = "intervention.masterInterventionIdTemp", source = "interventionDto.locationId")
//	@Mapping(target = "intervention.optionSlug",  ignore = true)
//	@Mapping(target = "intervention.user.id", source = "interventionDto.userId")

	@Mapping(target = "user", source = "userDto")
	@Mapping(target = "intervention", source = "interventionDto")
	InterventionFollowed interventionFollowedDtoToInterventionFollowed(InterventionFollowedDto interventionFollowedDto);

	List<InterventionFollowedDto> listInterventionFollowedToListInterventionFollowedDto(
			List<InterventionFollowed> interventionFollowed);

//	@Mapping(target = "intervention.id", source = "interventionId")
//	@Mapping(target = "student.id", source = "studentId")
	List<InterventionFollowed> listInterventionFollowedDtoToListInterventionFollowed(
			List<InterventionFollowedDto> interventionFollowedDto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user.idDg2", source = "personId")
	@Mapping(target = "intervention.idDg2", source = "interventionId")
	@Mapping(target = "version", ignore = true)
	InterventionFollowed interventionFollowedDG2DtoToInterventionFollowed(
			InterventionFollowedDG2Dto interventionFollowedDG2Dto);
}
