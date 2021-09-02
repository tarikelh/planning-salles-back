/**
 * 
 */
package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.DtoMapper;
import fr.dawan.calendarproject.mapper.DtoMapperImpl;

/**
 * @author Mokhtari Ahmed-Reda
 *
 */

class TestDtoMapper {
	private Skill skill1 = new Skill();
	private Skill skill2 = new Skill();
	private Skill skill3 = new Skill();
	private Course course = new Course();
	private Location location = new Location();
	private Set<Skill> skills = new HashSet<>();
	private Set<User> users = new HashSet<>();
	private User user = new User();
	private DtoMapper mapper = new DtoMapperImpl();

	@BeforeEach
	public void before() throws Exception {
		location = new Location(1, "Paris", "54967", 0);

		skill1 = new Skill(1, "sql", null, 0);
		skill2 = new Skill(2, "c#", null, 0);
		skill3 = new Skill(3, "java", null, 0);

		course = new Course(1, "Java Init", 0);

		skills.add(skill1);
		skills.add(skill2);
		skills.add(skill3);

		user = new User(1, "ahmed", "reda", location, "areda@dawan.fr", "mdpdelux", skills, UserType.ADMINISTRATIF,
				UserCompany.DAWAN, "./image/img.png", 0);

		users.add(user);

		skill1.setUsers(users);
	}

	@Test
	void shouldMap_SetOfSkills_To_ListOfLong() {
		// Prépa
		List<Long> skillsIds = new ArrayList<Long>();
		for (Skill skill : skills) {
			skillsIds.add(skill.getId());
		}

		// Exec
		List<Long> mappedSkills = mapper.SetSkillsToListLong(skills);

		// Test
		assertEquals(skillsIds, mappedSkills);
	}

	@Test
	void shouldMap_UserEntity_To_AdvancedUserDto() {
		// Prépa
		List<Long> skillsIds = new ArrayList<Long>();
		for (Skill skill : user.getSkills()) {
			skillsIds.add(skill.getId());
		}

		// Exec
		AdvancedUserDto mappedAdvUser = mapper.UserToAdvancedUserDto(user);

		// Test
		assertEquals(user.getCompany().toString(), mappedAdvUser.getCompany());
		assertEquals(user.getEmail(), mappedAdvUser.getEmail());
		assertEquals(user.getFirstName(), mappedAdvUser.getFirstName());
		assertEquals(user.getId(), mappedAdvUser.getId());
		assertEquals(user.getImagePath(), mappedAdvUser.getImagePath());
		assertEquals(user.getLastName(), mappedAdvUser.getLastName());
		assertEquals(user.getLocation().getId(), mappedAdvUser.getLocationId());
		assertEquals(user.getPassword(), mappedAdvUser.getPassword());
		assertEquals(user.getType().toString(), mappedAdvUser.getType());
		assertEquals(user.getVersion(), mappedAdvUser.getVersion());
		assertEquals(skillsIds, mappedAdvUser.getSkillsId());
	}

	@Test
	void shouldMap_SkillEntity_To_AdvancedSkillDto() {
		// Prépa
		List<Long> usersIds = new ArrayList<Long>();
		for (User users : skill1.getUsers()) {
			usersIds.add(users.getId());
		}

		// Exec
		AdvancedSkillDto mappedadvSkill = mapper.SkillToAdvancedSkillDto(skill1);

		// Test
		assertEquals(skill1.getId(), mappedadvSkill.getId());
		assertEquals(skill1.getTitle(), mappedadvSkill.getTitle());
		assertEquals(usersIds, mappedadvSkill.getUsersId());
		assertEquals(skill1.getVersion(), mappedadvSkill.getVersion());
	}

	@Test
	void shouldMap_LocationEntity_To_LocationDto() {
		// Exec
		LocationDto mappedLocation = mapper.LocationToLocationDto(location);

		// Test
		assertEquals(location.getId(), mappedLocation.getId());
		assertEquals(location.getCity(), mappedLocation.getCity());
		assertEquals(location.getColor(), mappedLocation.getColor());
		assertEquals(location.getVersion(), mappedLocation.getVersion());
	}

	@Test
	void shouldMap_CourseEntity_To_CourseDto() {
		// Exec
		CourseDto mappedCourse = mapper.CourseToCourseDto(course);

		// Test
		assertEquals(course.getId(), mappedCourse.getId());
		assertEquals(course.getTitle(), mappedCourse.getTitle());
		assertEquals(course.getVersion(), mappedCourse.getVersion());
	}

	@Test
	void shouldMap_SkillEntity_To_SkillDto() {
		// Exec
		SkillDto mappedadvSkill = mapper.SkillToSkillDto(skill1);

		// Test
		assertEquals(skill1.getId(), mappedadvSkill.getId());
		assertEquals(skill1.getTitle(), mappedadvSkill.getTitle());
		assertEquals(skill1.getVersion(), mappedadvSkill.getVersion());
	}
}
