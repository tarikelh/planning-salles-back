package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.UserMapper;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.SkillRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.HashTools;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;

	@MockBean
	private LocationRepository locationRepository;

	@MockBean
	private SkillRepository skillRepository;

	@MockBean
	private UserMapper userMapper;

	private List<User> uList = new ArrayList<User>();
	private List<AdvancedUserDto> uDtoList = new ArrayList<AdvancedUserDto>();

	@BeforeEach
	void setUp() throws Exception {
		Location loc = Mockito.mock(Location.class);
		
		uList.add(new User(1, "Daniel", "Balavoine", loc,
				"dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0));
		
		uList.add(new User(2, "Michel", "Polnareff", loc,
				"mpolnareff@dawan.fr", "testPasswordPolnareff", null,
				UserType.COMMERCIAL, UserCompany.JEHANN, "", 0));
		
		uList.add(new User(3, "Charles", "Aznavour", loc,
				"caznavour@dawan.fr", "testPasswordAznav", null,
				UserType.FORMATEUR, UserCompany.JEHANN, "", 0));
		
		uDtoList.add(new AdvancedUserDto(1, "Daniel", "Balavoine", 0,
				"dbalavoine@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null));
		
		uDtoList.add(new AdvancedUserDto(2, "Michel", "Polnareff", 0,
				"mpolnareff@dawan.fr", "testPasswordPolnareff",
				"COMMERCIAL", "JEHANN", "", 0, null));
		
		uDtoList.add(new AdvancedUserDto(3, "Charles", "Aznavour", 0,
				"caznavour@dawan.fr", "testPasswordAznav",
				"FORMATEUR", "JEHANN", "", 0, null));
	}

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();
	}
	
	@Test
	void shouldGetAllUsersAndReturnDto() {
		when(userRepository.findAll()).thenReturn(uList);
		when(userMapper.userToAdvancedUserDto(any(User.class)))
			.thenReturn(uDtoList.get(0), uDtoList.get(1), uDtoList.get(2));
		
		List<AdvancedUserDto> result = userService.getAllUsers();
		
		assertThat(result).isNotNull();
		assertEquals(uList.size(), result.size());
		assertEquals(uDtoList.size(), result.size());
		assertEquals(uDtoList, result);
	}

	@Test
	void shouldGetUsersAndReturnPaginatedDtos() {
		Page<User> p1 = new PageImpl<User>(uList.subList(0, 2));

		when(userRepository.findAllByFirstNameContainingOrLastNameContainingOrEmailContaining(
				any(String.class), any(String.class), any(String.class), any(Pageable.class)))
			.thenReturn(p1);
		when(userMapper.userToAdvancedUserDto(any(User.class)))
			.thenReturn(uDtoList.get(0), uDtoList.get(1));
		
		List<AdvancedUserDto> result = userService.getAllUsers(0, 2, "");
		
		assertThat(result).isNotNull();
		assertEquals(uList.subList(0, 2).size(), result.size());
	}
	
	@Test
	void shouldGetAllUsersWithPageAndSizeLessThanZero() {
		Page<User> unpagedSkills = new PageImpl<User>(uList);

		when(userRepository.findAllByFirstNameContainingOrLastNameContainingOrEmailContaining(
				any(String.class), any(String.class), any(String.class), any(Pageable.class)))
			.thenReturn(unpagedSkills);
		when(userMapper.userToAdvancedUserDto(any(User.class)))
			.thenReturn(uDtoList.get(0), uDtoList.get(1));
		
		List<AdvancedUserDto> result = userService.getAllUsers(0, 2, "");
		
		assertThat(result).isNotNull();
		assertEquals(uList.size(), result.size());
	}

	@Test
	void shouldReturnCountOfUserssWithGivenKeyword() {
		when(userRepository.countByFirstNameContainingOrLastNameContainingOrEmailContaining(
				any(String.class), any(String.class), any(String.class)))
			.thenReturn((long)uList.size());
		
		CountDto result = userService.count("");
		
		assertThat(result).isNotNull();
		assertEquals(uList.size(), result.getNb());
	}

	@Test
	void shouldGetAllUsersByType() {
		when(userRepository.findAllByType(any(UserType.class))).thenReturn(uList.subList(0, 1));
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(uDtoList.get(0));
		
		List<AdvancedUserDto> result = userService.getAllUsersByType("ADMINISTRATIF");

		assertThat(result).isNotNull();
	}

	@Test
	void shouldReturnNullWhenGivenTypeIsWrong() {
		List<AdvancedUserDto> result = userService.getAllUsersByType("BADUSERTYPE");

		assertThat(result).isNull();
	}
	
	@Test
	void shouldGetUserById() {
		when(userRepository.findById(any(long.class))).thenReturn(Optional.of(uList.get(1)));
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(uDtoList.get(1));
		
		AdvancedUserDto result = userService.getById(2);
		
		assertThat(result).isNotNull();
		assertEquals(uDtoList.get(1), result);
	}

	@Test
	void shouldReturnNullWhenSkillIdIsUnknown() {
		when(userRepository.findById(any(long.class))).thenReturn(Optional.empty());

		AdvancedUserDto result = userService.getById(2222);

		assertThat(result).isNull();
	}

	@Test
	void shouldSaveNewUser() {
		MockedStatic<HashTools> hashTools = Mockito.mockStatic(HashTools.class);
		Location mockedLoc = Mockito.mock(Location.class);
		Set<Skill> sList = new HashSet<Skill>();
		Skill s1 = new Skill(1, "DevOps", null, 0);
		sList.add(s1);
		Skill s2 = new Skill(2, "POO", null, 0);
		sList.add(s2);
		Skill s3 = new Skill(3, "SQL", null, 0);
		sList.add(s3);
		
		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);
		skillIds.add(2L);
		skillIds.add(3L);
		
		AdvancedUserDto toCreate = new AdvancedUserDto(0, "Michel", "Delpech", 0,
				"mdelpech@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);
		AdvancedUserDto expected = new AdvancedUserDto(3, "Michel", "Delpech", 0,
				"mdelpech@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);
		User repoReturn = new User(3, "Michel", "Delpech",
				mockedLoc,
				"mdelpech@dawan.fr", "testPassword", sList,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0);
		
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(userMapper.advancedUserDtoToUser(any(AdvancedUserDto.class))).thenReturn(repoReturn);
		when(skillRepository.getOne(any(Long.class))).thenReturn(s1, s2, s3);
		when(locationRepository.getOne(any(Long.class))).thenReturn(mockedLoc);
		hashTools.when(() -> HashTools.hashSHA512(any(String.class))).thenReturn("hashedPassword");
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(repoReturn);
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(expected);
		
		AdvancedUserDto result = userService.saveOrUpdate(toCreate);
		
		assertThat(result).isNotNull();
		assertEquals(expected, result);
		assertEquals(sList.size(), result.getSkillsId().size());
		
		if(!hashTools.isClosed())
			hashTools.close();
	}

	@Test
	void ShouldReturnNullWhenUpdateUserWithWrongId() {
		AdvancedUserDto toUpdate = new AdvancedUserDto(222, "Michel", "Delpech", 0,
				"mdelpech@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);

		when(skillRepository.findById(any(long.class))).thenReturn(Optional.empty());

		AdvancedUserDto result = userService.saveOrUpdate(toUpdate);

		assertThat(result).isNull();
	}

	@Test
	void ShouldHashWhenPasswordHasChanged() {
		MockedStatic<HashTools> hashTools = Mockito.mockStatic(HashTools.class);
		Location mockedLoc = Mockito.mock(Location.class);
		AdvancedUserDto newPwdDto = new AdvancedUserDto(1, "Daniel", "Balavoine", 0,
				"dbalavoine@dawan.fr", "newStrongPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);
		User newPwd = new User(1, "Daniel", "Balavoine", mockedLoc,
				"dbalavoine@dawan.fr", "newStrongPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0);
		User oldPwd = new User(1, "Daniel", "Balavoine", mockedLoc,
				"dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0);
		User newHashed = new User(1, "Daniel", "Balavoine", mockedLoc,
				"dbalavoine@dawan.fr", "hashedNewStrongPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0);
		AdvancedUserDto expected = new AdvancedUserDto(1, "Daniel", "Balavoine", 0,
				"dbalavoine@dawan.fr", "hashedNewStrongPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);
		
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(oldPwd));
		when(userMapper.advancedUserDtoToUser(any(AdvancedUserDto.class))).thenReturn(newPwd);
		when(locationRepository.getOne(any(Long.class))).thenReturn(mockedLoc);
		when(userRepository.getOne(any(Long.class))).thenReturn(oldPwd);
		hashTools.when(() -> HashTools.hashSHA512(any(String.class))).thenReturn("hashedNewStrongPassword");
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(newHashed);
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(expected);
		
		AdvancedUserDto result = userService.saveOrUpdate(newPwdDto);
		
		assertThat(result).isNotNull();
		assertEquals(expected, result);
		assertEquals("hashedNewStrongPassword", result.getPassword());
		
		if(!hashTools.isClosed())
			hashTools.close();
	}
	
	@Test
	void shouldFindUserByEmail() {
		when(userRepository.findByEmail(any(String.class))).thenReturn(uList.get(0));
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(uDtoList.get(0));
		
		AdvancedUserDto result = userService.findByEmail("dbalavoine@dawan.fr");
		
		assertThat(result).isNotNull();
		assertEquals(uDtoList.get(0), result);
	}
	
	@Test
	void shouldReturnNullWhenFindUserWithWrongEmail() {
		when(userRepository.findByEmail(any(String.class))).thenReturn(null);
		
		AdvancedUserDto result = userService.findByEmail("wrongEmail@dawan.fr");
		
		assertThat(result).isNull();
	}

	@Test
	void shouldThrowWhenUserHasDuplicateEmail() {
		AdvancedUserDto alreadyExistingEmail = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12,
				"dbalavoine@dawan.fr", "newStrongPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);
		
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(uList.get(0));
		
		assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(alreadyExistingEmail);
		});
	}
	
	@Test
	void shouldThrowWhenUserHasBadEmail() {
		AdvancedUserDto badEmail = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12,
				"dbalavoinedawan.fr", "newStrongPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);
		
		when(locationRepository.findById(any(Long.class)))
				.thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		
		assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badEmail);
		});
	}
	
	@Test
	void shouldThrowWhenUserHasBadLocationId() {
		AdvancedUserDto badLocId = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12,
				"dbalavoine@dawan.fr", "newStrongPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);
		
		when(locationRepository.findById(any(Long.class)))
				.thenReturn(Optional.empty());
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		
		assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badLocId);
		});
	}
	
	@Test
	void shouldThrowWhenUserHasBadSkill() {
		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);
		
		AdvancedUserDto badSkill = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12,
				"dbalavoine@dawan.fr", "newStrongPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);
		
		when(locationRepository.findById(any(Long.class)))
			.thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		
		assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badSkill);
		});
	}
	
	@Test
	void shouldThrowWhenUserHasTooShortPassword() {
		Skill s1 = new Skill(1, "DevOps", null, 0);
		
		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);
		
		AdvancedUserDto shortPwd = new AdvancedUserDto(0, "Daniel", "Balavoine", 12,
				"dbalavoine@dawan.fr", "short",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);
		
		when(locationRepository.findById(any(Long.class)))
			.thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));
		
		assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(shortPwd);
		});
	}
	
	@Test
	void shouldThrowWhenUserHasBad() {
		Skill s1 = new Skill(1, "DevOps", null, 0);
		
		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);
		
		AdvancedUserDto badCompany = new AdvancedUserDto(0, "Daniel", "Balavoine", 12,
				"dbalavoine@dawan.fr", "newStrongPassword",
				"ADMINISTRATIF", "BADCOMPANY", "", 0, skillIds);
		
		when(locationRepository.findById(any(Long.class)))
			.thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));
		
		assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badCompany);
		});
	}
	
	@Test
	void shouldThrowWhenUserHasBadType() {
		Skill s1 = new Skill(1, "DevOps", null, 0);
		
		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);
		
		AdvancedUserDto badType = new AdvancedUserDto(0, "Daniel", "Balavoine", 12,
				"dbalavoine@dawan.fr", "newStrongPassword",
				"BADTYPE", "DAWAN", "", 0, skillIds);
		
		when(locationRepository.findById(any(Long.class)))
			.thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));
		
		assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badType);
		});
	}
	
	@Test
	void shouldReturnTrueWhenUserIsCorrect() {
		Skill s1 = new Skill(1, "DevOps", null, 0);
		
		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);
		
		AdvancedUserDto goodUser = new AdvancedUserDto(0, "Daniel", "Balavoine", 12,
				"dbalavoine@dawan.fr", "newStrongPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);
		
		when(locationRepository.findById(any(Long.class)))
			.thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));
		
		boolean result = userService.checkIntegrity(goodUser);
		
		assertThat(result).isTrue();
	}

}
