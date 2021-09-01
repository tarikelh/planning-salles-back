package fr.dawan.calendarproject.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;

@Mapper
public interface DtoMapper {

	@Mapping(source = "location.id", target = "locationId")
	default AdvancedUserDto UserToAdvancedUserDto(User user) {
		List<Long> skills = new ArrayList<>();
		for (Skill skill : user.getSkills()) {
			skills.add(skill.getId());
		}

		AdvancedUserDto advUser = new AdvancedUserDto(user.getId(), user.getFirstName(), user.getLastName(),
				user.getLocation().getId(), user.getEmail(), user.getPassword(), user.getType().toString(),
				user.getCompany().toString(), user.getImagePath(), user.getVersion(), skills);

		return advUser;
	}

	default AdvancedSkillDto AdvancedSkillToSkillDto(Skill skill) {
		List<Long> users = new ArrayList<>();
		for (User user : skill.getUsers()) {
			users.add(user.getId());
		}

		AdvancedSkillDto advSkill = new AdvancedSkillDto(skill.getId(), skill.getTitle(), skill.getVersion(), users);

		return advSkill;
	}

	@Mapping(source = ".", target = ".")
	LocationDto LocationToLocationDto(Location location);

	@Mapping(source = ".", target = ".")
	CourseDto CourseToCourseDto(Course course);

	@Mapping(source = ".", target = ".")
	SkillDto SkillToSkillDto(Skill skill);

}
