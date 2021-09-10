package fr.dawan.calendarproject.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;

@Mapper
@Component
public interface DtoMapper {

	default List<Long> SetSkillsToListLong(Set<Skill> skills) {
		List<Long> skillsIds = new ArrayList<>();
		for (Skill skill : skills) {
			skillsIds.add(skill.getId());
		}
		return skillsIds;
	}

	@Mapping(source = "location.id", target = "locationId")
	default AdvancedUserDto UserToAdvancedUserDto(User user) {

		AdvancedUserDto advUser = new AdvancedUserDto(
//				user.getId(), user.getFirstName(), user.getLastName(),
//				user.getLocation().getId(), user.getEmail(), user.getPassword(), user.getType().toString(),
//				user.getCompany().toString(), user.getImagePath(), user.getVersion(),
//				this.SetSkillsToListLong(user.getSkills())
				);

		return advUser;
	}

	default AdvancedSkillDto SkillToAdvancedSkillDto(Skill skill) {
		List<Long> users = new ArrayList<>();
		for (User user : skill.getUsers()) {
			users.add(user.getId());
		}

		AdvancedSkillDto advSkill = new AdvancedSkillDto(skill.getId(), skill.getTitle(), skill.getVersion(), users);

		return advSkill;
	}

	LocationDto LocationToLocationDto(Location location);

	CourseDto CourseToCourseDto(Course course);

	SkillDto SkillToSkillDto(Skill skill);

	@Mappings({ @Mapping(target = "courseId", source = "course.id"),
			@Mapping(target = "locationId", source = "location.id"), @Mapping(target = "userId", source = "user.id"),
			@Mapping(target = "masterInterventionId", source = "masterIntervention.id"), })
	InterventionDto interventionToInterventionDto(Intervention intervention);

	@Mappings({ @Mapping(target = "course.id", source = "courseId"),
			@Mapping(target = "location.id", source = "locationId"), @Mapping(target = "user.id", source = "userId"),
			@Mapping(target = "masterIntervention.id", source = "masterInterventionId"),
			@Mapping(target = "enumType", source = "type") })
	Intervention interventionDtoToIntervention(InterventionDto intervention);

	@Mappings({ @Mapping(target = "id", source = "interventionId"), @Mapping(target = "course.id", source = "courseId"),
			@Mapping(target = "location.id", source = "locationId"), @Mapping(target = "user.id", source = "userId"),
			@Mapping(target = "masterIntervention.id", source = "masterInterventionId"),
			@Mapping(target = "enumType", source = "type"), @Mapping(target = "version", ignore = true) })
	Intervention interventionMementoDtoToIntervention(InterventionMementoDto iMemDto);

	@Mappings({ @Mapping(target = "interventionId", source = "id"), @Mapping(target = "courseId", source = "course.id"),
			@Mapping(target = "locationId", source = "location.id"), @Mapping(target = "userId", source = "user.id"),
			@Mapping(target = "masterInterventionId", source = "masterIntervention.id"),
			@Mapping(target = "locationCity", source = "location.city"),
			@Mapping(target = "courseTitle", source = "course.title"),
			@Mapping(target = "userFullName", source = "user.fullname"),
			@Mapping(target = "userEmail", source = "user.email"), @Mapping(target = "type", source = "type") })
	InterventionMementoDto interventionToInterventionMementoDto(Intervention intervention);
}
