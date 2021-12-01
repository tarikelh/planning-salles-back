package fr.dawan.calendarproject.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.SkillMapper;

@SpringBootTest
@AutoConfigureMockMvc
class SkillMapperTest {

	@Autowired
	private SkillMapper skillMapper;

	private Skill skill = new Skill();
	private Skill skill2 = new Skill();
	private AdvancedSkillDto advSkillDto = new AdvancedSkillDto();
	private List<Long> usersId = new ArrayList<Long>();
	private Set<User> users = new HashSet<User>();
	private Set<Skill> skills = new HashSet<Skill>();
	private List<Long> skillsId = new ArrayList<Long>();
	private List<Skill> skillsList = new ArrayList<Skill>();
	private User user = new User();
	private User user2 = new User();
	private Location location = new Location();

	@BeforeEach
	void before() {
		location = new Location(1, "paris", "#32656", 1);

		user = new User(1, "firstname", "lastname", location, "areda@dawan.fr", "mdpdelux", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "./image/img.png", 0);

		user2 = new User(2, "firstname", "lastname", location, "areda2@dawan.fr", "mdpdelux2", null, UserType.FORMATEUR,
				UserCompany.JEHANN, "./image/img.png", 0);

		usersId.add(user.getId());
		usersId.add(user2.getId());

		users.add(user);
		users.add(user2);

		advSkillDto = new AdvancedSkillDto(2, "POO", 0, null);

		skill = new Skill(1, "DevOps", users, 0);
		skill2 = new Skill(2, "C#", users, 0);

		skillsList.add(skill);
		skillsList.add(skill2);

		skills.add(skill);
		skills.add(skill2);

		skillsId.add(skill.getId());
		skillsId.add(skill2.getId());
	}

	@Test
	void should_map_skillToAdvancedSkillDto() {
		// mapping
		AdvancedSkillDto mappedAdvancedSkillDto = skillMapper.skillToAdvancedSkillDto(skill);

		List<Long> list = new ArrayList<Long>();
		for (User user : skill.getUsers()) {
			list.add(user.getId());
		}

		// assert
		assertEquals(mappedAdvancedSkillDto.getId(), skill.getId());
		assertEquals(mappedAdvancedSkillDto.getTitle(), skill.getTitle());
		assertEquals(mappedAdvancedSkillDto.getUsersId(), list);
		assertEquals(mappedAdvancedSkillDto.getVersion(), skill.getVersion());
	}

	@Test
	void should_map_advancedSkillDtoToSkill() {
		// mapping
		Skill mappedSkill = skillMapper.advancedSkillDtoToSkill(advSkillDto);

		// assert
		assertEquals(mappedSkill.getId(), advSkillDto.getId());
		assertEquals(mappedSkill.getTitle(), advSkillDto.getTitle());
		assertEquals(mappedSkill.getVersion(), advSkillDto.getVersion());
	}

	@Test
	void should_map_listSkillsToSetSkills() {
		// mapping
		Set<Skill> mappedSkillSet = skillMapper.listSkillsToSetSkills(skillsList);

		// assert
		assertEquals(mappedSkillSet.size(), skillsList.size());
		assertThat(mappedSkillSet.contains(skill));
		assertThat(mappedSkillSet.contains(skill2));
	}

	@Test
	void should_map_setSkillsToListLong() {
		// mapping
		List<Long> mappedLongList = skillMapper.setSkillsToListLong(skills);

		List<Long> list = new ArrayList<Long>();

		for (Skill skill : skills) {
			list.add(skill.getId());
		}

		// assert
		assertEquals(mappedLongList.size(), skillsList.size());
		assertEquals(mappedLongList, list);
	}
}
