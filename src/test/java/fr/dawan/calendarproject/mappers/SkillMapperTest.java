package fr.dawan.calendarproject.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.SkillMapper;
import fr.dawan.calendarproject.mapper.UserMapper;
import fr.dawan.calendarproject.repositories.SkillRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class SkillMapperTest {

	@Autowired
	private SkillMapper skillMapper;

	@MockBean
	private SkillRepository skillRepository;

	@MockBean
	private UserMapper userMapper;

	private Skill skill = new Skill();
	private Skill skill2 = new Skill();
	private SkillDto advSkillDto = new SkillDto();
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
		location = new Location(1, "paris", "#32656", "FR", false, 1);

		LocalDate date = LocalDate.now(); 
		
		user = new User(1L, 1L, 1L, "firstname", "lastname", location, "areda@dawan.fr", "mdpdelux", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "./image/img.png",date,null, 0);

		user2 = new User(2L, 2L, 2L, "firstname", "lastname", location, "areda2@dawan.fr", "mdpdelux2", null,
				UserType.FORMATEUR, UserCompany.JEHANN, "./image/img.png",date, null, 0 );

		usersId.add(user.getId());
		usersId.add(user2.getId());

		users.add(user);
		users.add(user2);

		advSkillDto = new SkillDto(2, "POO", null, 0);

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
	void should_map_skillToSkillDto() {
		// mocking
		when(userMapper.setUsersToListLong(any())).thenReturn(usersId);

		// mapping
		SkillDto mappedAdvancedSkillDto = skillMapper.skillToSkillDto(skill);

		// assert
		assertEquals(mappedAdvancedSkillDto.getId(), skill.getId());
		assertEquals(mappedAdvancedSkillDto.getTitle(), skill.getTitle());
		assertEquals(mappedAdvancedSkillDto.getUsersId(), usersId);
		assertEquals(mappedAdvancedSkillDto.getVersion(), skill.getVersion());
	}

	@Test
	void should_map_SkillDtoToSkill() {
		// mocking
		when(userMapper.listLongToSetUsers(any())).thenReturn(users);

		// mapping
		Skill mappedSkill = skillMapper.skillDtoToSkill(advSkillDto);

		// assert
		assertEquals(mappedSkill.getId(), advSkillDto.getId());
		assertEquals(mappedSkill.getTitle(), advSkillDto.getTitle());
		assertEquals(mappedSkill.getVersion(), advSkillDto.getVersion());
	}

}
