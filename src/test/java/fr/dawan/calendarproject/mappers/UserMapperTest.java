package fr.dawan.calendarproject.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.SkillMapper;
import fr.dawan.calendarproject.mapper.UserMapper;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private SkillMapper skillMapper;

	@MockBean
	private LocationRepository locationRepository;

	private User user = new User();
	private User user2 = new User();
	private List<Long> usersId = new ArrayList<Long>();
	private UserDto userDto = new UserDto();
	private AdvancedUserDto advUserDto = new AdvancedUserDto();
	private List<Long> skillsId = new ArrayList<Long>();
	private Set<Skill> skills = new HashSet<Skill>();
	private Set<User> usersSet = new HashSet<User>();
	private List<User> usersList = new ArrayList<User>();
	private Skill skill = new Skill();
	private Location location = new Location();
	private Location location2 = new Location();

	@BeforeEach
	void before() {
		skill = new Skill(2, "skill2", null, 1);
		skillsId.add(skill.getId());
		skills.add(skill);

		location = new Location(1, "paris", "#FFFFFF", 1);
		location2 = new Location(4, "Lille", "#FFFFF1", 1);

		advUserDto = new AdvancedUserDto(1, "advfirstname", "advlastname", 1, "advname@dawan.fr", "dsfghhrghzrazrfg",
				"ADMINISTRATIF", "DAWAN", "gdfsdfzaq.png", 1, null);

		userDto = new UserDto(2, "dtofustname", "dtolastname", 4, "dtoname@dawan.fr", "dfdghhereqq", "COMMERCIAL",
				"DAWAN", "zfzfzh.pngé", 2);

		user = new User(3, "firstname", "lastname", location, "name@dawan.fr", "dffghthghzrazrfg", skills,
				UserType.FORMATEUR, UserCompany.DAWAN, "gdfsdfzaq.png", 1);

		user2 = new User(5, "firstname5", "lastname5", location2, "name5@dawan.fr", "qsdijdszjd", skills,
				UserType.FORMATEUR, UserCompany.DAWAN, "sfdijofez.png", 1);

		usersSet.add(user);
		usersSet.add(user2);
		usersList.add(user);
		usersList.add(user2);
		usersId.add(user.getId());
		usersId.add(user2.getId());
	}

	@Test
	void should_map_userToAdvancedUserDto() {
		// mocking
		List<Long> list = new ArrayList<Long>();
		for (Skill skill : user.getSkills()) {
			list.add(skill.getId());
		}

		when(skillMapper.setSkillsToListLong(any())).thenReturn(list);

		// mapping
		AdvancedUserDto mappedAdvancedUserDto = userMapper.userToAdvancedUserDto(user);

		// assert
		assertEquals(mappedAdvancedUserDto.getId(), user.getId());
		assertEquals(mappedAdvancedUserDto.getFirstName(), user.getFirstName());
		assertEquals(mappedAdvancedUserDto.getLastName(), user.getLastName());
		assertEquals(mappedAdvancedUserDto.getFullName(), user.getFullname());
		assertEquals(mappedAdvancedUserDto.getLocationId(), user.getLocation().getId());
		assertEquals(mappedAdvancedUserDto.getEmail(), user.getEmail());
		assertEquals(mappedAdvancedUserDto.getPassword(), user.getPassword());
		assertEquals(mappedAdvancedUserDto.getSkillsId(), list);
		assertEquals(mappedAdvancedUserDto.getType(), user.getType().toString());
		assertEquals(mappedAdvancedUserDto.getCompany(), user.getCompany().toString());
		assertEquals(mappedAdvancedUserDto.getImagePath(), user.getImagePath());
		assertEquals(mappedAdvancedUserDto.getVersion(), user.getVersion());
	}

	@Test
	void should_map_advancedUserDtoToUser() {
		// mocking
		when(locationRepository.getOne(any(Long.class))).thenReturn(location);
		when(skillMapper.listLongToSetSkills(any())).thenReturn(skills);

		// mapping
		User mappedUser = userMapper.advancedUserDtoToUser(advUserDto);

		// assert
		assertEquals(mappedUser.getId(), advUserDto.getId());
		assertEquals(mappedUser.getFirstName(), advUserDto.getFirstName());
		assertEquals(mappedUser.getLastName(), advUserDto.getLastName());
		assertEquals(mappedUser.getFullname(), advUserDto.getFullName());
		assertEquals(mappedUser.getLocation().getId(), advUserDto.getLocationId());
		assertEquals(mappedUser.getEmail(), advUserDto.getEmail());
		assertEquals(mappedUser.getPassword(), advUserDto.getPassword());
		assertEquals(mappedUser.getType().toString(), advUserDto.getType());
		assertEquals(mappedUser.getCompany().toString(), advUserDto.getCompany());
		assertEquals(mappedUser.getImagePath(), advUserDto.getImagePath());
		assertEquals(mappedUser.getSkills(), skills);
		assertEquals(mappedUser.getVersion(), advUserDto.getVersion());
	}

	@Test
	void should_map_userDtoToUser() {
		// mocking
		when(locationRepository.getOne(any(Long.class))).thenReturn(location2);

		// mapping
		User mappedUser = userMapper.userDtoToUser(userDto);

		// assert
		assertEquals(mappedUser.getId(), userDto.getId());
		assertEquals(mappedUser.getFirstName(), userDto.getFirstName());
		assertEquals(mappedUser.getLastName(), userDto.getLastName());
		assertEquals(mappedUser.getFullname(), userDto.getFullName());
		assertEquals(mappedUser.getLocation().getId(), userDto.getLocationId());
		assertEquals(mappedUser.getEmail(), userDto.getEmail());
		assertEquals(mappedUser.getPassword(), userDto.getPassword());
		assertEquals(mappedUser.getType().toString(), userDto.getType());
		assertEquals(mappedUser.getCompany().toString(), userDto.getCompany());
		assertEquals(mappedUser.getImagePath(), userDto.getImagePath());
		assertEquals(mappedUser.getVersion(), userDto.getVersion());
	}

	@Test
	void should_map_userToUserDto() {
		// mapping
		UserDto mappedUserDto = userMapper.userToUserDto(user);

		// assert
		assertEquals(mappedUserDto.getId(), user.getId());
		assertEquals(mappedUserDto.getFirstName(), user.getFirstName());
		assertEquals(mappedUserDto.getLastName(), user.getLastName());
		assertEquals(mappedUserDto.getFullName(), user.getFullname());
		assertEquals(mappedUserDto.getLocationId(), user.getLocation().getId());
		assertEquals(mappedUserDto.getEmail(), user.getEmail());
		assertEquals(mappedUserDto.getPassword(), user.getPassword());
		assertEquals(mappedUserDto.getType(), user.getType().toString());
		assertEquals(mappedUserDto.getCompany(), user.getCompany().toString());
		assertEquals(mappedUserDto.getImagePath(), user.getImagePath());
		assertEquals(mappedUserDto.getVersion(), user.getVersion());
	}

	@Test
	void should_map_setUsersToListLong() {
		// mapping
		List<Long> mappedUserSkillsIds = userMapper.setUsersToListLong(usersSet);

		List<Long> list = new ArrayList<Long>();
		for (User user : usersSet) {
			list.add(user.getId());
		}

		// assert
		assertEquals(mappedUserSkillsIds, list);
	}

	@Test
	void should_map_listUserToSetUser() {
		// mapping
		Set<User> mappedUserSkillsIds = userMapper.listUserToSetUser(usersList);

		// assert
		assertEquals(mappedUserSkillsIds.size(), usersList.size());
		assertThat(mappedUserSkillsIds.contains(user));
		assertThat(mappedUserSkillsIds.contains(user2));
	}

	@Test
	void should_map_listLongToSetUsers() {
		// mocking
		when(userRepository.getOne((long) user.getId())).thenReturn(user);
		when(userRepository.getOne((long) user2.getId())).thenReturn(user2);

		// mapping
		Set<User> mappedUserSkillsIds = userMapper.listLongToSetUsers(usersId);

		// assert
		assertEquals(mappedUserSkillsIds.size(), usersList.size());
		assertThat(mappedUserSkillsIds.contains(user));
		assertThat(mappedUserSkillsIds.contains(user2));
	}

}
