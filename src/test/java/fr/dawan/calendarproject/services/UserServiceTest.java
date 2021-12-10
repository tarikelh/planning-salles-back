package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.dto.UserDG2Dto;
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
import fr.dawan.calendarproject.tools.JwtTokenUtil;

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
	
	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@MockBean
	private RestTemplate restTemplate;

	private List<User> uList = new ArrayList<User>();
	private List<AdvancedUserDto> uDtoList = new ArrayList<AdvancedUserDto>();
	private List<UserDG2Dto> usersDG2 = new ArrayList<UserDG2Dto>();
	private User userFromDG2 = new User();
	private AdvancedUserDto adUserDto;
	private ResetResponse resetResponse;
	private static String email = "dbalavoine@dawan.fr";

	@BeforeEach
	void setUp() throws Exception {
		adUserDto = new AdvancedUserDto(1, "Daniel", "Balavoine", 0,
				"dbalavoine@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);
		
		resetResponse = new ResetResponse("TokenTestResetPassword", "ResetPasswordTest");
		
		Location loc = Mockito.mock(Location.class);

		uList.add(new User(1, "Daniel", "Balavoine", loc, "dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0));

		uList.add(new User(2, "Michel", "Polnareff", loc, "mpolnareff@dawan.fr", "testPasswordPolnareff", null,
				UserType.COMMERCIAL, UserCompany.JEHANN, "", 0));

		uList.add(new User(3, "Charles", "Aznavour", loc, "caznavour@dawan.fr", "testPasswordAznav", null,
				UserType.FORMATEUR, UserCompany.JEHANN, "", 0));

		uDtoList.add(new AdvancedUserDto(1, "Daniel", "Balavoine", 0, "dbalavoine@dawan.fr", null, "ADMINISTRATIF",
				"DAWAN", "", 0, null));

		uDtoList.add(new AdvancedUserDto(2, "Michel", "Polnareff", 0, "mpolnareff@dawan.fr", "testPasswordPolnareff",
				"COMMERCIAL", "JEHANN", "", 0, null));

		uDtoList.add(new AdvancedUserDto(3, "Charles", "Aznavour", 0, "caznavour@dawan.fr", "testPasswordAznav",
				"FORMATEUR", "JEHANN", "", 0, null));

		userFromDG2 = new User(4, "Charles", "Aznavour", loc, "caznavour@dawan.fr", null, null, UserType.FORMATEUR,
				UserCompany.JEHANN, "", 0);

		usersDG2.add(new UserDG2Dto(4, "zcrijze", "ecqiuv", 1, "zdokpcopi@dawan.fr", null, "FORMATEUR", "JEHANN",
				"repoil.png", null, 0));
		usersDG2.add(new UserDG2Dto(9, "erzoiuevi", "azkczpo", 1, "zflkzlkzf@dawan.fr", null, "ADMINISTRATIF", "DAWAN",
				"vrdio.png", null, 0));
		usersDG2.add(new UserDG2Dto(5, "seqepo", "qiuhfokazd", 1, "csqhuzaoi@dawan.fr", null, "APPRENTI", "JEHANN",
				"vn.png", null, 0));
	}

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();
	}

	@Test
	void shouldGetAllUsersAndReturnDto() {
		when(userRepository.findAll()).thenReturn(uList);
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(uDtoList.get(0), uDtoList.get(1),
				uDtoList.get(2));

		List<AdvancedUserDto> result = userService.getAllUsers();

		assertThat(result).isNotNull();
		assertEquals(uList.size(), result.size());
		assertEquals(uDtoList.size(), result.size());
		assertEquals(uDtoList, result);
	}

	@Test
	void shouldGetUsersAndReturnPaginatedDtos() {
		Page<User> p1 = new PageImpl<User>(uList.subList(0, 2));

		when(userRepository.findAllByFirstNameContainingOrLastNameContainingOrEmailContaining(any(String.class),
				any(String.class), any(String.class), any(Pageable.class))).thenReturn(p1);
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(uDtoList.get(0), uDtoList.get(1));

		List<AdvancedUserDto> result = userService.getAllUsers(0, 2, "");

		assertThat(result).isNotNull();
		assertEquals(uList.subList(0, 2).size(), result.size());
	}

	@Test
	void shouldGetAllUsersWithPageAndSizeLessThanZero() {
		Page<User> unpagedSkills = new PageImpl<User>(uList);

		when(userRepository.findAllByFirstNameContainingOrLastNameContainingOrEmailContaining(any(String.class),
				any(String.class), any(String.class), any(Pageable.class))).thenReturn(unpagedSkills);
		when(userMapper.userToAdvancedUserDto(any(User.class))).thenReturn(uDtoList.get(0), uDtoList.get(1));

		List<AdvancedUserDto> result = userService.getAllUsers(0, 2, "");

		assertThat(result).isNotNull();
		assertEquals(uList.size(), result.size());
	}

	@Test
	void shouldReturnCountOfUserssWithGivenKeyword() {
		when(userRepository.countByFirstNameContainingOrLastNameContainingOrEmailContaining(any(String.class),
				any(String.class), any(String.class))).thenReturn((long) uList.size());

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
	void shouldReturnEmptyListWhenGivenTypeIsWrong() {
		List<AdvancedUserDto> result = userService.getAllUsersByType("BADUSERTYPE");

		assertThat(result).isEmpty();
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

		AdvancedUserDto toCreate = new AdvancedUserDto(0, "Michel", "Delpech", 0, "mdelpech@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);
		AdvancedUserDto expected = new AdvancedUserDto(3, "Michel", "Delpech", 0, "mdelpech@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);
		User repoReturn = new User(3, "Michel", "Delpech", mockedLoc, "mdelpech@dawan.fr", "testPassword", sList,
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

		if (!hashTools.isClosed())
			hashTools.close();
	}

	@Test
	void ShouldReturnNullWhenUpdateUserWithWrongId() {
		AdvancedUserDto toUpdate = new AdvancedUserDto(222, "Michel", "Delpech", 0, "mdelpech@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null);

		when(skillRepository.findById(any(long.class))).thenReturn(Optional.empty());

		AdvancedUserDto result = userService.saveOrUpdate(toUpdate);

		assertThat(result).isNull();
	}

	@Test
	void ShouldHashWhenPasswordHasChanged() {
		MockedStatic<HashTools> hashTools = Mockito.mockStatic(HashTools.class);
		Location mockedLoc = Mockito.mock(Location.class);
		AdvancedUserDto newPwdDto = new AdvancedUserDto(1, "Daniel", "Balavoine", 0, "dbalavoine@dawan.fr",
				"newStrongPassword", "ADMINISTRATIF", "DAWAN", "", 0, null);
		User newPwd = new User(1, "Daniel", "Balavoine", mockedLoc, "dbalavoine@dawan.fr", "newStrongPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0);
		User oldPwd = new User(1, "Daniel", "Balavoine", mockedLoc, "dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0);
		User newHashed = new User(1, "Daniel", "Balavoine", mockedLoc, "dbalavoine@dawan.fr", "hashedNewStrongPassword",
				null, UserType.ADMINISTRATIF, UserCompany.DAWAN, "", 0);
		AdvancedUserDto expected = new AdvancedUserDto(1, "Daniel", "Balavoine", 0, "dbalavoine@dawan.fr",
				"hashedNewStrongPassword", "ADMINISTRATIF", "DAWAN", "", 0, null);

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

		if (!hashTools.isClosed())
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
		AdvancedUserDto alreadyExistingEmail = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12, "dbalavoine@dawan.fr",
				"newStrongPassword", "ADMINISTRATIF", "DAWAN", "", 0, null);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(uList.get(0));

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(alreadyExistingEmail);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[0];

		assertEquals(1, resultException.getErrors().size());
		assertEquals("class fr.dawan.calendarproject.dto.AdvancedUserDto", result.getInstanceClass());
		assertEquals("/api/users", result.getPath());
		assertEquals("EmailNotUniq", result.getType());
		assertEquals("Email already used.", result.getMessage());
	}

	@Test
	void shouldThrowWhenUserHasBadEmail() {
		AdvancedUserDto badEmail = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12, "dbalavoinedawan.fr",
				"newStrongPassword", "ADMINISTRATIF", "DAWAN", "", 0, null);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badEmail);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[0];

		assertEquals(1, resultException.getErrors().size());
		assertEquals("class fr.dawan.calendarproject.dto.AdvancedUserDto", result.getInstanceClass());
		assertEquals("/api/users", result.getPath());
		assertEquals("InvalidEmail", result.getType());
		assertEquals("Email must be valid.", result.getMessage());
	}

	@Test
	void shouldThrowWhenUserHasBadLocationId() {
		AdvancedUserDto badLocId = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12, "dbalavoine@dawan.fr",
				"newStrongPassword", "ADMINISTRATIF", "DAWAN", "", 0, null);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badLocId);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[0];

		assertEquals(1, resultException.getErrors().size());
		assertEquals("class fr.dawan.calendarproject.dto.AdvancedUserDto", result.getInstanceClass());
		assertEquals("/api/users", result.getPath());
		assertEquals("LocationNotFound", result.getType());
		assertEquals("Location with id: " + badLocId.getLocationId() + " does not exist.", result.getMessage());
	}

	@Test
	void shouldThrowWhenUserHasBadSkill() {
		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);

		AdvancedUserDto badSkill = new AdvancedUserDto(0, "Daniel", "Balavoine2", 12, "dbalavoine@dawan.fr",
				"newStrongPassword", "ADMINISTRATIF", "DAWAN", "", 0, skillIds);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badSkill);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[0];

		assertEquals(1, resultException.getErrors().size());
		assertEquals("class fr.dawan.calendarproject.dto.AdvancedUserDto", result.getInstanceClass());
		assertEquals("/api/users", result.getPath());
		assertEquals("SkillNotFound", result.getType());
		assertEquals("Skill with id: " + skillIds.get(0) + " does not exist.", result.getMessage());
	}

	@Test
	void shouldThrowWhenUserHasTooShortPassword() {
		Skill s1 = new Skill(1, "DevOps", null, 0);

		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);

		AdvancedUserDto shortPwd = new AdvancedUserDto(0, "Daniel", "Balavoine", 12, "dbalavoine@dawan.fr", "short",
				"ADMINISTRATIF", "DAWAN", "", 0, skillIds);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(shortPwd);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[0];

		assertEquals(1, resultException.getErrors().size());
		assertEquals("class fr.dawan.calendarproject.dto.AdvancedUserDto", result.getInstanceClass());
		assertEquals("/api/users", result.getPath());
		assertEquals("PasswordTooShort", result.getType());
		assertEquals("Password must be at least 8 characters long", result.getMessage());
	}

	@Test
	void shouldThrowWhenUserHasBadCompany() {
		Skill s1 = new Skill(1, "DevOps", null, 0);

		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);

		AdvancedUserDto badCompany = new AdvancedUserDto(0, "Daniel", "Balavoine", 12, "dbalavoine@dawan.fr",
				"newStrongPassword", "ADMINISTRATIF", "BADCOMPANY", "", 0, skillIds);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badCompany);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[0];

		assertEquals(1, resultException.getErrors().size());
		assertEquals("class fr.dawan.calendarproject.dto.AdvancedUserDto", result.getInstanceClass());
		assertEquals("/api/users", result.getPath());
		assertEquals("UnknownUserCompany", result.getType());
		assertEquals("Company: " + badCompany.getCompany() + " is not valid.", result.getMessage());
	}

	@Test
	void shouldThrowWhenUserHasBadType() {
		Skill s1 = new Skill(1, "DevOps", null, 0);

		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);

		AdvancedUserDto badType = new AdvancedUserDto(0, "Daniel", "Balavoine", 12, "dbalavoine@dawan.fr",
				"newStrongPassword", "BADTYPE", "DAWAN", "", 0, skillIds);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			userService.checkIntegrity(badType);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[0];

		assertEquals(1, resultException.getErrors().size());
		assertEquals("class fr.dawan.calendarproject.dto.AdvancedUserDto", result.getInstanceClass());
		assertEquals("/api/users", result.getPath());
		assertEquals("UnknownUserType", result.getType());
		assertEquals("Type: " + badType.getType() + " is not valid.", result.getMessage());
	}

	@Test
	void shouldReturnTrueWhenUserIsCorrect() {
		Skill s1 = new Skill(1, "DevOps", null, 0);

		List<Long> skillIds = new ArrayList<Long>();
		skillIds.add(1L);

		AdvancedUserDto goodUser = new AdvancedUserDto(0, "Daniel", "Balavoine", 12, "dbalavoine@dawan.fr",
				"newStrongPassword", "ADMINISTRATIF", "DAWAN", "", 0, skillIds);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(Location.class)));
		when(userRepository.findDuplicateEmail(any(String.class), any(Long.class))).thenReturn(null);
		when(skillRepository.findById(any(Long.class))).thenReturn(Optional.of(s1));

		boolean result = userService.checkIntegrity(goodUser);

		assertThat(result).isTrue();
	}

	@Test
	void testToStringUser() {
		assertFalse(new User().toString().contains("@"));
	}

	@SuppressWarnings("unchecked")
	@Test
	void shouldFetchAllDG2UsersWhenUserExistInDb() {
		// mocking
		String body = "[{\"id\":167678,\"firstName\":\"Nadjla\",\"lastName\":\"ADAM IBOURA\",\"locationId\":10,\"email\":\"nadamiboura@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":125334,\"firstName\":\"Wilson\",\"lastName\":\"AGBOR\",\"locationId\":2,\"email\":\"wagbor@dawan.fr\",\"job\":\"Formateur D\\u00e9cisionnel\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":110482,\"firstName\":\"Thomas\",\"lastName\":\"ALDAITZ\",\"locationId\":3,\"email\":\"taldaitz@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\".NET PHP Magento\"},{\"id\":7436,\"firstName\":\"Mathilde\",\"lastName\":\"ALONSO\",\"locationId\":6,\"email\":\"malonso@dawan.fr\",\"job\":\"Formatrice\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":40209,\"firstName\":\"Antoine\",\"lastName\":\"ANDO\",\"locationId\":6,\"email\":\"aando@dawan.fr\",\"job\":\"D\\u00e9veloppeur web\",\"name\":\"DAWAN\",\"skill\":\"PHP, Web\"},{\"id\":173185,\"firstName\":\"Victor\",\"lastName\":\"ANDR\\u00c9\",\"locationId\":23,\"email\":\"vandre@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":183,\"firstName\":\"Emmanuel\",\"lastName\":\"ANNE\",\"locationId\":7,\"email\":\"eanne@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":116696,\"firstName\":\"Dawan\",\"lastName\":\"ANSIBLE\",\"locationId\":3,\"email\":\"ansible@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":97999,\"firstName\":\"Ouma\\u00efma\",\"lastName\":\"AOUFI\",\"locationId\":6,\"email\":\"oaoufi@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167638,\"firstName\":\"Ali-Ha\\u00efdar\",\"lastName\":\"ATIA\",\"locationId\":7,\"email\":\"aatia@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":31645,\"firstName\":\"Richard\",\"lastName\":\"BACONNIER\",\"locationId\":3,\"email\":\"rbaconnier@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes\",\"name\":\"DAWAN\",\"skill\":\"Linux, R\\u00e9seaux\"},{\"id\":55421,\"firstName\":\"Bastien\",\"lastName\":\"BALAUD\",\"locationId\":6,\"email\":\"bbalaud@dawan.fr\",\"job\":\"Alternance - CDD Contrat pro.\",\"name\":\"DAWAN\",\"skill\":\"Linux, Syst\\u00e8mes\"},{\"id\":86642,\"firstName\":\"Laurence\",\"lastName\":\"BARON GOMEZ\",\"locationId\":6,\"email\":\"lbarongomez@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":73992,\"firstName\":\"Fr\\u00e9d\\u00e9ric James\",\"lastName\":\"BAUDOT\",\"locationId\":1,\"email\":\"fbaudot@dawan.fr\",\"job\":\"Formateur\\/Graphiste PAO DAO\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":38754,\"firstName\":\"Damien\",\"lastName\":\"BERAUD\",\"locationId\":3,\"email\":\"dberaud@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux, D\\u00e9veloppeur\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, Linux, VMware, CISCO, R\\u00e9seaux, PHP, Python, LPI, Docker, Vagrant, DevOps\"},{\"id\":167658,\"firstName\":\"Teddy\",\"lastName\":\"BIBOUM\",\"locationId\":7,\"email\":\"tbiboum@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167626,\"firstName\":\"Tanguy\",\"lastName\":\"BILLON\",\"locationId\":6,\"email\":\"tbillon@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":12,\"firstName\":\"Micka\\u00ebl\",\"lastName\":\"BLANCHARD\",\"locationId\":6,\"email\":\"dawancom@gmail.com\",\"job\":\"Manager, formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, Java, C, HTML\"},{\"id\":4313,\"firstName\":\"Aur\\u00e9lien\",\"lastName\":\"BOCQUET\",\"locationId\":6,\"email\":\"abocquet@dawan.fr\",\"job\":\"Formateur, Consultant\",\"name\":\"DAWAN\",\"skill\":\"PHP, .NET, Java, C\\/C++, D\\u00e9veloppement Web\"},{\"id\":119132,\"firstName\":\"Hary\",\"lastName\":\"BOKOLA\",\"locationId\":6,\"email\":\"hbokola@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":34938,\"firstName\":\"Sylvie\",\"lastName\":\"BONNEAU\",\"locationId\":6,\"email\":\"sbonneau@dawan.fr\",\"job\":\"Assistante administrative et comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":10,\"firstName\":\"Yannick\",\"lastName\":\"BONNIEUX\",\"locationId\":6,\"email\":\"ybonnieux@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":95716,\"firstName\":\"Nehaad\",\"lastName\":\"BOODHOWA\",\"locationId\":6,\"email\":\"nboodhowa@dawan.fr\",\"job\":\"admin sys\",\"name\":\"DAWAN\",\"skill\":\"linux\"},{\"id\":167684,\"firstName\":\"Rachid\",\"lastName\":\"BOUDJENANE\",\"locationId\":10,\"email\":\"rboudjenane@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167673,\"firstName\":\"In\\u00e8s\",\"lastName\":\"BOUKHELOUA\",\"locationId\":2,\"email\":\"iboukheloua@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":38669,\"firstName\":\"Xavier\",\"lastName\":\"BOURGET\",\"locationId\":6,\"email\":\"xbourget@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes\",\"name\":\"DAWAN\",\"skill\":\"Linux, R\\u00e9seaux, Virtualisation\"},{\"id\":73197,\"firstName\":\"Ga\\u00ebtan\",\"lastName\":\"BOYER\",\"locationId\":7,\"email\":\"gboyer@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"Syst\\u00e8mes, R\\u00e9seaux, CISCO\"},{\"id\":98384,\"firstName\":\"Anthony\",\"lastName\":\"BREILLOT\",\"locationId\":9,\"email\":\"abreillot@dawan.fr\",\"job\":\"Graphiste Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":12050,\"firstName\":\"Pierre\",\"lastName\":\"BRETECHE\",\"locationId\":6,\"email\":\"pbreteche@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Php, Web\"},{\"id\":59043,\"firstName\":\"Justine\",\"lastName\":\"BROCHARD\",\"locationId\":6,\"email\":\"jbrochard@dawan.fr\",\"job\":\"Assistante Comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":854,\"firstName\":\"St\\u00e9phanie\",\"lastName\":\"BUI\",\"locationId\":6,\"email\":\"sbui@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":130414,\"firstName\":\"Jean-Ren\\u00e9\",\"lastName\":\"CALOVI\",\"locationId\":5,\"email\":\"jrcalovi@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":28002,\"firstName\":\"Gurvan\",\"lastName\":\"CARIOU\",\"locationId\":6,\"email\":\"gcariou@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, Web, Audiovisuel\"},{\"id\":167674,\"firstName\":\"R\\u00e9mi\",\"lastName\":\"CASTIEN\",\"locationId\":2,\"email\":\"rcastien@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167653,\"firstName\":\"Natacha\",\"lastName\":\"CHABAS\",\"locationId\":6,\"email\":\"nchabas@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119131,\"firstName\":\"Christian\",\"lastName\":\"CHAMPETIER\",\"locationId\":6,\"email\":\"cchampetier@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":186,\"firstName\":\"Damien\",\"lastName\":\"CHATELET\",\"locationId\":6,\"email\":\"dchatelet@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167672,\"firstName\":\"Zainab\",\"lastName\":\"CHROROU\",\"locationId\":2,\"email\":\"zchrorou@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167656,\"firstName\":\"Miguel\",\"lastName\":\"CLAIRY\",\"locationId\":7,\"email\":\"mclairy@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":3138,\"firstName\":\"David\",\"lastName\":\"CL\\u00c9MENT\",\"locationId\":7,\"email\":\"dclement@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, Linux, Delphi, XHTML, CSS, Javascript, Dremaweaver, Windows\"},{\"id\":144577,\"firstName\":\"Projets Internes\",\"lastName\":\"COMPTE DE TEST\",\"locationId\":6,\"email\":\"test@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167681,\"firstName\":\"Andr\\u00c9\",\"lastName\":\"COUTO SENTIEIRO\",\"locationId\":10,\"email\":\"acoutosentieiro@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":21609,\"firstName\":\"Pauline\",\"lastName\":\"D\\u0027ANASTASI\",\"locationId\":6,\"email\":\"pdanastasi@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156041,\"firstName\":\"Victor\",\"lastName\":\"DA COSTA FERREIRA\",\"locationId\":7,\"email\":\"vdacosta@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\\/3D\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167633,\"firstName\":\"Jade\",\"lastName\":\"DA SILVA LIMA\",\"locationId\":7,\"email\":\"jdasilva@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119134,\"firstName\":\"C\\u00e9cile\",\"lastName\":\"DABIN\",\"locationId\":6,\"email\":\"cdabin@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":21608,\"firstName\":\"Suzy\",\"lastName\":\"DABIN\",\"locationId\":6,\"email\":\"sdabin@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167680,\"firstName\":\"Leo\",\"lastName\":\"DARCES\",\"locationId\":10,\"email\":\"ldarces@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":76296,\"firstName\":\"Christophe\",\"lastName\":\"DAVID\",\"locationId\":6,\"email\":\"cdavid@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":143123,\"firstName\":\"Relations Externes\",\"lastName\":\"DAWAN\",\"locationId\":6,\"email\":\"relations-ext@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98002,\"firstName\":\"Laura\",\"lastName\":\"DEBRIE\",\"locationId\":6,\"email\":\"ldebrie@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167662,\"firstName\":\"Ana\\u00efs\",\"lastName\":\"DEBUSSCHERE\",\"locationId\":2,\"email\":\"adebusschere@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167671,\"firstName\":\"Damien\",\"lastName\":\"DELAERE\",\"locationId\":2,\"email\":\"ddelaere@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":65714,\"firstName\":\"Guillaume\",\"lastName\":\"DELANOY\",\"locationId\":2,\"email\":\"gdelanoy@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":6346,\"firstName\":\"Mamadou\",\"lastName\":\"DEMBELE\",\"locationId\":6,\"email\":\"mdembele@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, WEB\"},{\"id\":98003,\"firstName\":\"Fr\\u00e9d\\u00e9rique\",\"lastName\":\"DENIAUD\",\"locationId\":6,\"email\":\"fdeniaud@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":763,\"firstName\":\"Mohamed\",\"lastName\":\"DERKAOUI\",\"locationId\":7,\"email\":\"mderkaoui@dawan.fr\",\"job\":\"Manager, Formateur\",\"name\":\"DAWAN\",\"skill\":\"JAVA SE \\/ JAVA EE .NET : C#, VB.NET, ASP.NET D\\u00e9veloppement Mobile Android Web : Initiation \\u00e0 Expert, HTML\\/CSS, JS, jQuery, Ajax, BootstrapProgrammation C, C++, Delphi Gestion de projets : Agile, MS Project, Jenkins\"},{\"id\":167648,\"firstName\":\"Victor\",\"lastName\":\"DESILES\",\"locationId\":6,\"email\":\"vdesiles@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":130419,\"firstName\":\"Pascal\",\"lastName\":\"DEVAUX\",\"locationId\":5,\"email\":\"pdevaux@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\\/Bureautique\",\"name\":\"DAWAN\",\"skill\":\"CAO \\u0026 animation 3D : Solidworks \\/ Autocad \\/ Catia \\/ Creo \\/ Sketchup \\/ 3Ds Max\"},{\"id\":51642,\"firstName\":\"Abd-Raouf\",\"lastName\":\"DJILALI\",\"locationId\":7,\"email\":\"adjilali@dawan.fr\",\"job\":\"Formateur Bureautique\",\"name\":\"DAWAN\",\"skill\":\"Word, Excel\\/VBA, Access\"},{\"id\":167661,\"firstName\":\"Magloire\",\"lastName\":\"DJOMOLITE-BOLENDEA\",\"locationId\":7,\"email\":\"mdjomolite-bolendea@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167635,\"firstName\":\"Alfoussein\",\"lastName\":\"DOUCOURE\",\"locationId\":7,\"email\":\"adoucoure@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159320,\"firstName\":\"Nicolas\",\"lastName\":\"DUFLOT\",\"locationId\":2,\"email\":\"nduflot@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119133,\"firstName\":\"Sandie\",\"lastName\":\"DUPONT\",\"locationId\":6,\"email\":\"sdupont@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159321,\"firstName\":\"Guillaume\",\"lastName\":\"DUVERNEUIL\",\"locationId\":6,\"email\":\"gduverneuil@dawan.fr\",\"job\":\"Formateur nantes\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":39793,\"firstName\":\"Aur\\u00e9lie\",\"lastName\":\"EBURDERY\",\"locationId\":6,\"email\":\"aeburdery@EXdawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":88374,\"firstName\":\"Beno\\u00eet\",\"lastName\":\"ECHAPPE\",\"locationId\":6,\"email\":\"bechappe@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167685,\"firstName\":\"LILA\",\"lastName\":\"ELAB\",\"locationId\":10,\"email\":\"lelab@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4,\"firstName\":\"Guillaume\",\"lastName\":\"ESTIVAL\",\"locationId\":6,\"email\":\"gestival@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167651,\"firstName\":\"Cl\\u00e9ment\",\"lastName\":\"ETIENNE\",\"locationId\":6,\"email\":\"cetienne@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":173559,\"firstName\":\"Laurence\",\"lastName\":\"ETIENNE\",\"locationId\":6,\"email\":\"letienne@dawan.fr\",\"job\":\"Assistance facturation et recouvrement\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167630,\"firstName\":\"Maxime\",\"lastName\":\"FAUDUET\",\"locationId\":6,\"email\":\"mfauduet@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":153188,\"firstName\":\"Camille\",\"lastName\":\"FAVROT\",\"locationId\":6,\"email\":\"cfavrot@dawan.fr\",\"job\":\"Formatrice et charg\\u00e9e E-Marketing\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167687,\"firstName\":\"Nicolas\",\"lastName\":\"FELIX\",\"locationId\":7,\"email\":\"nfelix@jehann.fr\",\"job\":\"DW\",\"name\":\"JEHANN\",\"skill\":\"\"},{\"id\":10909,\"firstName\":\"Romain\",\"lastName\":\"FLACHER\",\"locationId\":6,\"email\":\"rflacher@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Developpement\"},{\"id\":156022,\"firstName\":\"Christophe\",\"lastName\":\"FONTAINE\",\"locationId\":2,\"email\":\"cfontaine@dawan.fr\",\"job\":\"Formateur D\\u00e9veloppeur Java EE\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4206,\"firstName\":\"Ren\\u00e9\",\"lastName\":\"FRANCESCHI\",\"locationId\":6,\"email\":\"rfranceschi@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, Illustrator, Photoshop, Flash, in Design, Dreamweaver\"},{\"id\":156994,\"firstName\":\"Rapha\\u00ebl\",\"lastName\":\"GACHET\",\"locationId\":6,\"email\":\"rgachet@dawan.fr\",\"job\":\"Assistant commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":88378,\"firstName\":\"V\\u00e9ronique\",\"lastName\":\"GARNIER FOUCHE\",\"locationId\":6,\"email\":\"vgarnier@dawan.fr\",\"job\":\"Assistante Administrative\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":165143,\"firstName\":\"El Hadji\",\"lastName\":\"GAYE\",\"locationId\":6,\"email\":\"ehgaye@dawan.fr\",\"job\":\"Formateur Java\\/.Net\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":21610,\"firstName\":\"Eddy\",\"lastName\":\"GHADDAR\",\"locationId\":6,\"email\":\"eghaddar@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"E-Marketing, R\\u00e9f\\u00e9rencement, E-Mailing, R\\u00e9seaux sociaux\"},{\"id\":61989,\"firstName\":\"Florian\",\"lastName\":\"GICQUEL\",\"locationId\":6,\"email\":\"fgicquel@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167659,\"firstName\":\"Marie\",\"lastName\":\"GIGAUD\",\"locationId\":7,\"email\":\"mgigaud@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167643,\"firstName\":\"Eric\",\"lastName\":\"GIGONDAN\",\"locationId\":6,\"email\":\"egigondan@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167650,\"firstName\":\"Geoffrey\",\"lastName\":\"GIRAUD\",\"locationId\":6,\"email\":\"ggiraud@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156017,\"firstName\":\"Lo\\u00efc\",\"lastName\":\"GODIN\",\"locationId\":6,\"email\":\"lgodin@dawan.fr\",\"job\":\"Formateur et d\\u00e9veloppeur Web\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167631,\"firstName\":\"Laura Daniela\",\"lastName\":\"GODINEZ ARANA\",\"locationId\":6,\"email\":\"lgodinez@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4227,\"firstName\":\"Michel\",\"lastName\":\"GORBATKO\",\"locationId\":6,\"email\":\"mgorbatko@dawan.fr\",\"job\":\"Graphiste Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO CAO\"},{\"id\":2789,\"firstName\":\"M\\u00e9lanie\",\"lastName\":\"GOURE\",\"locationId\":6,\"email\":\"mgoure@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":88621,\"firstName\":\"Yves\",\"lastName\":\"GRUAU\",\"locationId\":6,\"email\":\"ygruau@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Management, Commercial\"},{\"id\":7761,\"firstName\":\"No\\u00e9\",\"lastName\":\"GUENEAU\",\"locationId\":6,\"email\":\"ngueneau@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":118049,\"firstName\":\"Bruno\",\"lastName\":\"GUERIN\",\"locationId\":10,\"email\":\"bguerin@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes bases de don\\u00e9es et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":36559,\"firstName\":\"Graziella\",\"lastName\":\"GUERRIER\",\"locationId\":6,\"email\":\"gguerrier@dawan.fr\",\"job\":\"Formatrice\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167646,\"firstName\":\"Axel\",\"lastName\":\"GUIHARD\",\"locationId\":6,\"email\":\"aguihard@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":113582,\"firstName\":\"Jean-Fran\\u00e7ois\",\"lastName\":\"GUILBERT\",\"locationId\":2,\"email\":\"jfguilbert@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":16006,\"firstName\":\"Ana\\u00efs\",\"lastName\":\"GUILLOIS\",\"locationId\":6,\"email\":\"aguillois@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":150092,\"firstName\":\"Ndiaye\",\"lastName\":\"HANN\",\"locationId\":10,\"email\":\"nhann@dawan.fr\",\"job\":\"D\\u00e9veloppeur Web\\/Java\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":51647,\"firstName\":\"Aymen\",\"lastName\":\"HARBAOUI\",\"locationId\":6,\"email\":\"aharbaoui@dawan.fr\",\"job\":\"Formateur Web\",\"name\":\"DAWAN\",\"skill\":\"Web, Prestashop\"},{\"id\":5955,\"firstName\":\"Yoann\",\"lastName\":\"HARDY\",\"locationId\":6,\"email\":\"yhardy@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, HTML, Flash\"},{\"id\":168092,\"firstName\":\"Valentin\",\"lastName\":\"HEGRON\",\"locationId\":6,\"email\":\"vhegron@dawan.fr\",\"job\":\"Assistant administratif recrutement et formation\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159958,\"firstName\":\"Guillaume\",\"lastName\":\"HENRY\",\"locationId\":3,\"email\":\"ghenry@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, Audiovisuel\"},{\"id\":173186,\"firstName\":\"Corentin\",\"lastName\":\"HERDUIN\",\"locationId\":23,\"email\":\"cherduin@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167667,\"firstName\":\"Pierre-Alin\",\"lastName\":\"HERVO\",\"locationId\":2,\"email\":\"phervo@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":24824,\"firstName\":\"Yvonnick\",\"lastName\":\"HERVY\",\"locationId\":6,\"email\":\"yhervy@dawan.fr\",\"job\":\"Manager Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159911,\"firstName\":\"Bruno\",\"lastName\":\"HOUDAYER\",\"locationId\":3,\"email\":\"bhoudayer@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167627,\"firstName\":\"Yosra\",\"lastName\":\"HOUIMLI\",\"locationId\":6,\"email\":\"yhouimli@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167652,\"firstName\":\"Tiphaine\",\"lastName\":\"IMBERT\",\"locationId\":6,\"email\":\"timbert@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":76485,\"firstName\":\"Jean Patrice\",\"lastName\":\"IWOGO\",\"locationId\":7,\"email\":\"jpiwogo@dawan.fr\",\"job\":\"Formateur Graphiste\",\"name\":\"DAWAN\",\"skill\":\"web pao\"},{\"id\":167637,\"firstName\":\"Sylvain\",\"lastName\":\"JANET\",\"locationId\":7,\"email\":\"sjanet@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167644,\"firstName\":\"Jocelyn\",\"lastName\":\"JANNIN\",\"locationId\":6,\"email\":\"jjannin@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4437,\"firstName\":\"Micka\\u00ebl\",\"lastName\":\"JAQUA\",\"locationId\":6,\"email\":\"mjaqua@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":11384,\"firstName\":\"Marjolaine\",\"lastName\":\"JUGEAU\",\"locationId\":6,\"email\":\"mjugeau@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":171918,\"firstName\":\"Woodson\",\"lastName\":\"JUSTE\",\"locationId\":10,\"email\":\"wjuste@dawan.fr\",\"job\":\"Formateur Web\\/Java\\/.NET\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":44624,\"firstName\":\"Thomas\",\"lastName\":\"KERNEM-OM\",\"locationId\":6,\"email\":\"tkernemom@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Business Intelligence\"},{\"id\":40295,\"firstName\":\"Fr\\u00e9d\\u00e9ric\",\"lastName\":\"KIBANZA MAKUIZA\",\"locationId\":7,\"email\":\"fkibanza@dawan.fr\",\"job\":\"Formateur \\/ Administrateur Syst\\u00e8mes\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":170228,\"firstName\":\"Steve\",\"lastName\":\"KOSSOUHO\",\"locationId\":6,\"email\":\"skossouho@dawan.fr\",\"job\":\"Formateur Python \\/ Web \\/ Programmation\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167657,\"firstName\":\"Elliot\",\"lastName\":\"KRAMDI\",\"locationId\":7,\"email\":\"ekramdi@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167641,\"firstName\":\"Denis\",\"lastName\":\"KUCUK\",\"locationId\":7,\"email\":\"dkucuk@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":29744,\"firstName\":\"Marie\",\"lastName\":\"LAFAURIE\",\"locationId\":6,\"email\":\"mlafaurie@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":169209,\"firstName\":\"Matthieu\",\"lastName\":\"LAMAMRA\",\"locationId\":6,\"email\":\"mlamamra@dawan.fr\",\"job\":\"Formateur syst\\u00e8me\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":61990,\"firstName\":\"Am\\u00e9lie\",\"lastName\":\"LAMBARD\",\"locationId\":6,\"email\":\"alambard@dawan.fr\",\"job\":\"Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":11383,\"firstName\":\"Christophe\",\"lastName\":\"LAMI\",\"locationId\":6,\"email\":\"clami@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119135,\"firstName\":\"Elisa\",\"lastName\":\"LASNIER\",\"locationId\":6,\"email\":\"elasnier@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167655,\"firstName\":\"Guillaume\",\"lastName\":\"LAURENS\",\"locationId\":7,\"email\":\"glaurens@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":107590,\"firstName\":\"Benjamin\",\"lastName\":\"LAURENT\",\"locationId\":6,\"email\":\"blaurent@dawan.fr\",\"job\":\"Assistant Comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167663,\"firstName\":\"Fabien\",\"lastName\":\"LAURENT\",\"locationId\":2,\"email\":\"flaurent@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167629,\"firstName\":\"Fabien\",\"lastName\":\"LAUTRU\",\"locationId\":6,\"email\":\"flautru@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":14782,\"firstName\":\"Martin\",\"lastName\":\"LAVOQUET\",\"locationId\":6,\"email\":\"\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":78821,\"firstName\":\"Roxane\",\"lastName\":\"LE BOURG\",\"locationId\":6,\"email\":\"rlebourg@dawan.fr\",\"job\":\"Charg\\u00e9e de formations et RH\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":50037,\"firstName\":\"Pauline\",\"lastName\":\"LE GOFF\",\"locationId\":6,\"email\":\"plegoff@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":153170,\"firstName\":\"Jean-Christophe\",\"lastName\":\"LEBEAU\",\"locationId\":3,\"email\":\"jclebeau@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\\/3D\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":7441,\"firstName\":\"Denis\",\"lastName\":\"LECLERC\",\"locationId\":6,\"email\":\"dleclerc@dawan.fr\",\"job\":\"Manager - Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":167303,\"firstName\":\"Martin\",\"lastName\":\"LEDRUT\",\"locationId\":2,\"email\":\"mledrut@dawan.fr\",\"job\":\"Formateur PAO et Audiovisuel\",\"name\":\"DAWAN\",\"skill\":\"Photoshop, Illustrator, InDesign, After Effect, Premi\\u00e8re Pro\"},{\"id\":586,\"firstName\":\"Fanny\",\"lastName\":\"LEFERT\",\"locationId\":6,\"email\":\"flefert@dawan.fr\",\"job\":\"Graphiste Formatrice\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":78707,\"firstName\":\"Sophie\",\"lastName\":\"LELOU\",\"locationId\":6,\"email\":\"slelou@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":114287,\"firstName\":\"Aur\\u00e9lien\",\"lastName\":\"LESLUYE\",\"locationId\":1,\"email\":\"alesluye@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156563,\"firstName\":\"Yohann\",\"lastName\":\"LESUEUR\",\"locationId\":27,\"email\":\"ylesueur@dawan.fr\",\"job\":\"Formateur d\\u00e9veloppeur informatique\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":104939,\"firstName\":\"Annabelle\",\"lastName\":\"LEVOY\",\"locationId\":2,\"email\":\"alevoy@dawan.fr\",\"job\":\"Conseill\\u00e8re Emploi Formation\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167645,\"firstName\":\"Maxence\",\"lastName\":\"LOGODIN\",\"locationId\":6,\"email\":\"mlogodin@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98411,\"firstName\":\"Christopher\",\"lastName\":\"LORENT\",\"locationId\":6,\"email\":\"clorent@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Java .Net\"},{\"id\":4205,\"firstName\":\"Vincent\",\"lastName\":\"LUCY\",\"locationId\":6,\"email\":\"vlucy@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Linux, Apache, Shell, Postfix\"},{\"id\":39792,\"firstName\":\"Romain\",\"lastName\":\"LUNEAU\",\"locationId\":6,\"email\":\"rluneau@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156024,\"firstName\":\"Eric\",\"lastName\":\"MADRANGE\",\"locationId\":27,\"email\":\"emadrange@dawan.fr\",\"job\":\"Formateur d\\u00e9veloppeur Web\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167676,\"firstName\":\"Anssaka\",\"lastName\":\"MAHAMOUDOU\",\"locationId\":10,\"email\":\"amahamoudou@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156026,\"firstName\":\"Mourad\",\"lastName\":\"MAHRANE\",\"locationId\":27,\"email\":\"mmahrane@dawan.fr\",\"job\":\"Formateur d\\u00e9veloppeur informatique\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167632,\"firstName\":\"Simon\",\"lastName\":\"MALPEL\",\"locationId\":6,\"email\":\"smalpel@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":54664,\"firstName\":\"Harmony\",\"lastName\":\"MARCHAND\",\"locationId\":6,\"email\":\"hmarchand@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":29742,\"firstName\":\"Alexandre\",\"lastName\":\"MARIE-LUCE\",\"locationId\":6,\"email\":\"amarieluce@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":36846,\"firstName\":\"Benjamin\",\"lastName\":\"MARRON\",\"locationId\":3,\"email\":\"bmarron@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"JEE Android .Net\"},{\"id\":11337,\"firstName\":\"Kevin\",\"lastName\":\"MARTIN\",\"locationId\":6,\"email\":\"kmartin@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Web, VBA, MsProject\"},{\"id\":167686,\"firstName\":\"MARCO\",\"lastName\":\"MARTIN\",\"locationId\":10,\"email\":\"mmartin@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167639,\"firstName\":\"Romain\",\"lastName\":\"MAURY\",\"locationId\":7,\"email\":\"rmaury@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":54283,\"firstName\":\"R\\u00e9mi\",\"lastName\":\"MAYEUX\",\"locationId\":2,\"email\":\"rmayeux@dawan.fr\",\"job\":\"Graphiste Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":7016,\"firstName\":\"St\\u00e9phane\",\"lastName\":\"MENUT\",\"locationId\":2,\"email\":\"smenut@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, HTML, XHTML, CSS, Javascript, Excel, Word\"},{\"id\":8,\"firstName\":\"Christelle\",\"lastName\":\"MERCKLING\",\"locationId\":7,\"email\":\"cmerckling@dawan.fr\",\"job\":\"Associ\\u00e9e\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":2,\"firstName\":\"J\\u00e9r\\u00f4me\",\"lastName\":\"MERCKLING\",\"locationId\":6,\"email\":\"jmerckling@dawan.fr\",\"job\":\"G\\u00e9rant\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167654,\"firstName\":\"Lo\\u00efc\",\"lastName\":\"METRINGER\",\"locationId\":7,\"email\":\"lmetringer@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167682,\"firstName\":\"Haidar\",\"lastName\":\"MEZIDI\",\"locationId\":10,\"email\":\"hmezidi@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":258,\"firstName\":\"Olivier\",\"lastName\":\"MICHAUD\",\"locationId\":6,\"email\":\"omichaud@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":121907,\"firstName\":\"Ayla\",\"lastName\":\"MICHENAUD\",\"locationId\":6,\"email\":\"amichenaud@dawan.fr\",\"job\":\"Charg\\u00e9e relations ressources externes\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167647,\"firstName\":\"Florian\",\"lastName\":\"MIOT\",\"locationId\":6,\"email\":\"fmiot@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":172002,\"firstName\":\"Audrey\",\"lastName\":\"MOAL\",\"locationId\":6,\"email\":\"amoal@dawan.fr\",\"job\":\"Manager Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":1809,\"firstName\":\"Olivier\",\"lastName\":\"MOCHK\",\"locationId\":6,\"email\":\"omochk@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes Windows\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, ISA Server, Exchange, Active Directory\"},{\"id\":167642,\"firstName\":\"Ahmed Reda\",\"lastName\":\"MOKHTARI\",\"locationId\":7,\"email\":\"amokhtari@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":145831,\"firstName\":\"Yoann\",\"lastName\":\"MOUI\",\"locationId\":10,\"email\":\"ymoui@dawan.fr\",\"job\":\"Formateur Bureautique\",\"name\":\"DAWAN\",\"skill\":\"Bureautique\"},{\"id\":95885,\"firstName\":\"Rachid\",\"lastName\":\"NEDJAR\",\"locationId\":7,\"email\":\"rnedjar@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167383,\"firstName\":\"Marie-Claire\",\"lastName\":\"NGOM\",\"locationId\":7,\"email\":\"mcngom@dawan.fr\",\"job\":\"Formatrice Management\\/Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167636,\"firstName\":\"Valentin\",\"lastName\":\"NGUYEN\",\"locationId\":7,\"email\":\"vnguyen@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":74418,\"firstName\":\"Nicolas\",\"lastName\":\"NIRDE\",\"locationId\":7,\"email\":\"nnirde@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\",\"name\":\"DAWAN\",\"skill\":\"Autocad Revit ...\"},{\"id\":35316,\"firstName\":\"Paola\",\"lastName\":\"NUNES\",\"locationId\":6,\"email\":\"pnunes@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":6965,\"firstName\":\"Benjamin\",\"lastName\":\"OERTEL\",\"locationId\":6,\"email\":\"boertel@dawan.fr\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167669,\"firstName\":\"Nelida\",\"lastName\":\"OTAN\",\"locationId\":2,\"email\":\"notan@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167660,\"firstName\":\"Hannah\",\"lastName\":\"OUASSI\",\"locationId\":7,\"email\":\"houassi@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":76295,\"firstName\":\"Emma\",\"lastName\":\"PARFAIT\",\"locationId\":6,\"email\":\"eparfait@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":38878,\"firstName\":\"Kevin\",\"lastName\":\"PAUTONNIER\",\"locationId\":6,\"email\":\"kpautonnier@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167640,\"firstName\":\"Aser Amagana\",\"lastName\":\"PEROU\",\"locationId\":7,\"email\":\"aperou@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":111890,\"firstName\":\"Sol\\u00e8ne\",\"lastName\":\"PERROT\",\"locationId\":6,\"email\":\"sperrot@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":50036,\"firstName\":\"H\\u00e9lo\\u00efse\",\"lastName\":\"PETIT\",\"locationId\":6,\"email\":\"hpetit@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":164130,\"firstName\":\"Marie-Claire\",\"lastName\":\"PETIT\",\"locationId\":3,\"email\":\"mcpetit@dawan.fr\",\"job\":\"CEF\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":853,\"firstName\":\"Maude\",\"lastName\":\"PETIT\",\"locationId\":6,\"email\":\"mpetit@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":73086,\"firstName\":\"Luc\",\"lastName\":\"PHAN\",\"locationId\":6,\"email\":\"lphan@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Python, Perl, PHP, HTML, CSS, Javascript, SQL, C\\/C++, Java\"},{\"id\":7484,\"firstName\":\"Gilles\",\"lastName\":\"PIETRI (OLD)\",\"locationId\":6,\"email\":\"gilles0972@dawan.fr\",\"job\":\"Manager Formateur\",\"name\":\"DAWAN\",\"skill\":\"Linux, VMware, Apache,\"},{\"id\":98000,\"firstName\":\"Aimeric\",\"lastName\":\"PLAGES\",\"locationId\":6,\"email\":\"aplages@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":30126,\"firstName\":\"Fr\\u00e9d\\u00e9ric\",\"lastName\":\"PLISSON\",\"locationId\":6,\"email\":\"fplisson@dawan.fr\",\"job\":\"Formateur E-Marketing\",\"name\":\"DAWAN\",\"skill\":\"E-Marketing, SEO, R\\u00e9f\\u00e9rencement, Webmaster, Chef de projet, Ergo\"},{\"id\":156032,\"firstName\":\"Christine\",\"lastName\":\"PLOQUIN\",\"locationId\":6,\"email\":\"cploquin@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167665,\"firstName\":\"Quentin\",\"lastName\":\"PLOUVIER\",\"locationId\":2,\"email\":\"qplouvier@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":8661,\"firstName\":\"Isabelle\",\"lastName\":\"POIROUD\",\"locationId\":6,\"email\":\"ipoiroud@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167634,\"firstName\":\"Nicolas\",\"lastName\":\"POTIER\",\"locationId\":7,\"email\":\"npotier@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167675,\"firstName\":\"Chlo\\u00c9\",\"lastName\":\"POUPON\",\"locationId\":10,\"email\":\"cpoupon@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167688,\"firstName\":\"Vannah\",\"lastName\":\"RANAIVOSON\",\"locationId\":7,\"email\":\"vranaivoson@jehann.fr\",\"job\":\"DW\",\"name\":\"JEHANN\",\"skill\":\"\"},{\"id\":113543,\"firstName\":\"Liana\",\"lastName\":\"RAVAHIMANANA\",\"locationId\":6,\"email\":\"lravahi@dawan.fr\",\"job\":\"Charg\\u00e9e de Recrutement\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156035,\"firstName\":\"Valentin\",\"lastName\":\"RAVAILLE\",\"locationId\":6,\"email\":\"vravaille@dawan.fr\",\"job\":\"Assistant commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167664,\"firstName\":\"Soufiane\",\"lastName\":\"RECHIA\",\"locationId\":2,\"email\":\"srechia@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167668,\"firstName\":\"Ana\\u00efs\",\"lastName\":\"REGNIER\",\"locationId\":2,\"email\":\"aregnier@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":160375,\"firstName\":\"Thais\",\"lastName\":\"REVILLON\",\"locationId\":2,\"email\":\"trevillon@dawan.fr\",\"job\":\"Formatrice er d\\u00e9veloppeuse Web\\/Java\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":15646,\"firstName\":\"Pascal\",\"lastName\":\"RICH\\u00c9\",\"locationId\":3,\"email\":\"priche@dawan.fr\",\"job\":\"Manager - Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO 2D\\/3D, Coaching\"},{\"id\":157020,\"firstName\":\"Melvyn\",\"lastName\":\"ROBERT\",\"locationId\":6,\"email\":\"mrobert@dawan.fr\",\"job\":\"Assistant comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":39555,\"firstName\":\"Yves\",\"lastName\":\"ROCAMORA (OLD)\",\"locationId\":6,\"email\":\"yrocamora@dawan.fr\",\"job\":\"Formateur Web\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":2426,\"firstName\":\"Nathalie\",\"lastName\":\"ROISSE\",\"locationId\":6,\"email\":\"nroisse@dawan.fr\",\"job\":\"Formatrice Administratrice Windows Server\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, ISA Server, Exchange, Active Directory\"},{\"id\":173750,\"firstName\":\"Enzo\",\"lastName\":\"ROSARIO\",\"locationId\":5,\"email\":\"erosario@dawan.fr\",\"job\":\"Formateur BI \\/ Programmation \\/ Linux\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":131102,\"firstName\":\"Thierry\",\"lastName\":\"ROUE\",\"locationId\":10,\"email\":\"troue@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156037,\"firstName\":\"Marie\",\"lastName\":\"ROY\",\"locationId\":6,\"email\":\"mroy@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98882,\"firstName\":\"Pierre\",\"lastName\":\"ROYER\",\"locationId\":6,\"email\":\"nsp@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"Linux, VMware, R\\u00e9seaux, LPI, Docker, DevOps\"},{\"id\":123848,\"firstName\":\"Pierre\",\"lastName\":\"SABLE\",\"locationId\":6,\"email\":\"psable@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes et DevOps\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":7760,\"firstName\":\"Aude\",\"lastName\":\"SAMSON\",\"locationId\":6,\"email\":\"asamson@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156125,\"firstName\":\"Christiane-Marie\",\"lastName\":\"SCIEUR\",\"locationId\":3,\"email\":\"cmscieur@dawan.fr\",\"job\":\"Formatrice bureautique\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167683,\"firstName\":\"Tidiane\",\"lastName\":\"SOW\",\"locationId\":10,\"email\":\"tsow@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167628,\"firstName\":\"Erwan\",\"lastName\":\"STEPHANT\",\"locationId\":6,\"email\":\"estephant@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":16397,\"firstName\":\"Martin\",\"lastName\":\"STIEVENART\",\"locationId\":2,\"email\":\"mstievenart@dawan.fr\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"Php\"},{\"id\":98413,\"firstName\":\"Cedric\",\"lastName\":\"SURQUIN (OLD)\",\"locationId\":2,\"email\":\"csurquin231@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux, D\\u00e9veloppeur\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, Linux, VMware\"},{\"id\":167625,\"firstName\":\"Kanha\",\"lastName\":\"SYDAPHASAVANH\",\"locationId\":6,\"email\":\"ksydaphasavanh@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":20917,\"firstName\":\"Najib\",\"lastName\":\"TALAA\",\"locationId\":6,\"email\":\"ntalaa@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98001,\"firstName\":\"Alain\",\"lastName\":\"TANGUY\",\"locationId\":6,\"email\":\"atanguy@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4436,\"firstName\":\"Florian\",\"lastName\":\"TEMPLIER\",\"locationId\":6,\"email\":\"ftemplier@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":16398,\"firstName\":\"Rajenthen\",\"lastName\":\"THARMABALAN\",\"locationId\":7,\"email\":\"rtharmabalan@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Administration Serveur\"},{\"id\":167670,\"firstName\":\"Victor\",\"lastName\":\"TIMMERMAN\",\"locationId\":2,\"email\":\"vtimmerman@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167649,\"firstName\":\"Sibel\",\"lastName\":\"URBAIN\",\"locationId\":6,\"email\":\"surbain@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167679,\"firstName\":\"Cesar Aim\\u00c9\",\"lastName\":\"VAILLANT\",\"locationId\":10,\"email\":\"cvaillant@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":42528,\"firstName\":\"Thibault\",\"lastName\":\"VAN WYNENDAELE\",\"locationId\":2,\"email\":\"tvanwynendaele@dawan.fr\",\"job\":\"Formateur, D\\u00e9veloppeur\",\"name\":\"DAWAN\",\"skill\":\"php, android, objective-c, swift\"},{\"id\":20126,\"firstName\":\"Maxime\",\"lastName\":\"VANWYNSBERGHE\",\"locationId\":4,\"email\":\"mvanwynsberghe@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":2223,\"firstName\":\"C\\u00e9dric\",\"lastName\":\"VASSEUR\",\"locationId\":6,\"email\":\"cvasseur@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, Java, HTML, Dreamweaver, Photoshop, Access, VBA, Excel\"},{\"id\":83389,\"firstName\":\"Richard\",\"lastName\":\"VERDON\",\"locationId\":6,\"email\":\"rverdon@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":116087,\"firstName\":\"Cyprien\",\"lastName\":\"VIDREQUIN\",\"locationId\":6,\"email\":\"cvidrequin@jehann.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167677,\"firstName\":\"Somsanouk\",\"lastName\":\"VONGKINGKEO\",\"locationId\":10,\"email\":\"svongkingkeo@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":608,\"firstName\":\"Vincent\",\"lastName\":\"VOYER\",\"locationId\":6,\"email\":\"vvoyer@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":115637,\"firstName\":\"Patience\",\"lastName\":\"WAITNAME\",\"locationId\":6,\"email\":\"pwaitname@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167666,\"firstName\":\"Aur\\u00e9lien\",\"lastName\":\"WALTER\",\"locationId\":2,\"email\":\"awalter@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":6964,\"firstName\":\"Hongbin\",\"lastName\":\"XU\",\"locationId\":6,\"email\":\"hxu@dawan.fr\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":163963,\"firstName\":\"Amenan\",\"lastName\":\"YAO\",\"locationId\":27,\"email\":\"ayao@dawan.fr\",\"job\":\"CEF\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":7412,\"firstName\":\"Othman\",\"lastName\":\"YAQOUBI\",\"locationId\":6,\"email\":\"oyaqoubi@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"}]";
		ResponseEntity<String> res = new ResponseEntity<String>(body, HttpStatus.OK);
		when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
				.thenReturn(res);
		when(userMapper.userDG2DtoToUser(any(UserDG2Dto.class))).thenReturn(userFromDG2);
		when(userRepository.findByEmail(any(String.class))).thenReturn(userFromDG2);
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(userFromDG2);

		// assert
		assertDoesNotThrow(() -> {
			userService.fetchAllDG2Users("userEmail", "userPassword");
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	void shouldFetchAllDG2UsersWhenUserDoNotExistInDb() {
		// mocking
		String body = "[{\"id\":167678,\"firstName\":\"Nadjla\",\"lastName\":\"ADAM IBOURA\",\"locationId\":10,\"email\":\"nadamiboura@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":125334,\"firstName\":\"Wilson\",\"lastName\":\"AGBOR\",\"locationId\":2,\"email\":\"wagbor@dawan.fr\",\"job\":\"Formateur D\\u00e9cisionnel\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":110482,\"firstName\":\"Thomas\",\"lastName\":\"ALDAITZ\",\"locationId\":3,\"email\":\"taldaitz@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\".NET PHP Magento\"},{\"id\":7436,\"firstName\":\"Mathilde\",\"lastName\":\"ALONSO\",\"locationId\":6,\"email\":\"malonso@dawan.fr\",\"job\":\"Formatrice\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":40209,\"firstName\":\"Antoine\",\"lastName\":\"ANDO\",\"locationId\":6,\"email\":\"aando@dawan.fr\",\"job\":\"D\\u00e9veloppeur web\",\"name\":\"DAWAN\",\"skill\":\"PHP, Web\"},{\"id\":173185,\"firstName\":\"Victor\",\"lastName\":\"ANDR\\u00c9\",\"locationId\":23,\"email\":\"vandre@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":183,\"firstName\":\"Emmanuel\",\"lastName\":\"ANNE\",\"locationId\":7,\"email\":\"eanne@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":116696,\"firstName\":\"Dawan\",\"lastName\":\"ANSIBLE\",\"locationId\":3,\"email\":\"ansible@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":97999,\"firstName\":\"Ouma\\u00efma\",\"lastName\":\"AOUFI\",\"locationId\":6,\"email\":\"oaoufi@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167638,\"firstName\":\"Ali-Ha\\u00efdar\",\"lastName\":\"ATIA\",\"locationId\":7,\"email\":\"aatia@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":31645,\"firstName\":\"Richard\",\"lastName\":\"BACONNIER\",\"locationId\":3,\"email\":\"rbaconnier@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes\",\"name\":\"DAWAN\",\"skill\":\"Linux, R\\u00e9seaux\"},{\"id\":55421,\"firstName\":\"Bastien\",\"lastName\":\"BALAUD\",\"locationId\":6,\"email\":\"bbalaud@dawan.fr\",\"job\":\"Alternance - CDD Contrat pro.\",\"name\":\"DAWAN\",\"skill\":\"Linux, Syst\\u00e8mes\"},{\"id\":86642,\"firstName\":\"Laurence\",\"lastName\":\"BARON GOMEZ\",\"locationId\":6,\"email\":\"lbarongomez@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":73992,\"firstName\":\"Fr\\u00e9d\\u00e9ric James\",\"lastName\":\"BAUDOT\",\"locationId\":1,\"email\":\"fbaudot@dawan.fr\",\"job\":\"Formateur\\/Graphiste PAO DAO\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":38754,\"firstName\":\"Damien\",\"lastName\":\"BERAUD\",\"locationId\":3,\"email\":\"dberaud@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux, D\\u00e9veloppeur\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, Linux, VMware, CISCO, R\\u00e9seaux, PHP, Python, LPI, Docker, Vagrant, DevOps\"},{\"id\":167658,\"firstName\":\"Teddy\",\"lastName\":\"BIBOUM\",\"locationId\":7,\"email\":\"tbiboum@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167626,\"firstName\":\"Tanguy\",\"lastName\":\"BILLON\",\"locationId\":6,\"email\":\"tbillon@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":12,\"firstName\":\"Micka\\u00ebl\",\"lastName\":\"BLANCHARD\",\"locationId\":6,\"email\":\"dawancom@gmail.com\",\"job\":\"Manager, formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, Java, C, HTML\"},{\"id\":4313,\"firstName\":\"Aur\\u00e9lien\",\"lastName\":\"BOCQUET\",\"locationId\":6,\"email\":\"abocquet@dawan.fr\",\"job\":\"Formateur, Consultant\",\"name\":\"DAWAN\",\"skill\":\"PHP, .NET, Java, C\\/C++, D\\u00e9veloppement Web\"},{\"id\":119132,\"firstName\":\"Hary\",\"lastName\":\"BOKOLA\",\"locationId\":6,\"email\":\"hbokola@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":34938,\"firstName\":\"Sylvie\",\"lastName\":\"BONNEAU\",\"locationId\":6,\"email\":\"sbonneau@dawan.fr\",\"job\":\"Assistante administrative et comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":10,\"firstName\":\"Yannick\",\"lastName\":\"BONNIEUX\",\"locationId\":6,\"email\":\"ybonnieux@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":95716,\"firstName\":\"Nehaad\",\"lastName\":\"BOODHOWA\",\"locationId\":6,\"email\":\"nboodhowa@dawan.fr\",\"job\":\"admin sys\",\"name\":\"DAWAN\",\"skill\":\"linux\"},{\"id\":167684,\"firstName\":\"Rachid\",\"lastName\":\"BOUDJENANE\",\"locationId\":10,\"email\":\"rboudjenane@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167673,\"firstName\":\"In\\u00e8s\",\"lastName\":\"BOUKHELOUA\",\"locationId\":2,\"email\":\"iboukheloua@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":38669,\"firstName\":\"Xavier\",\"lastName\":\"BOURGET\",\"locationId\":6,\"email\":\"xbourget@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes\",\"name\":\"DAWAN\",\"skill\":\"Linux, R\\u00e9seaux, Virtualisation\"},{\"id\":73197,\"firstName\":\"Ga\\u00ebtan\",\"lastName\":\"BOYER\",\"locationId\":7,\"email\":\"gboyer@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"Syst\\u00e8mes, R\\u00e9seaux, CISCO\"},{\"id\":98384,\"firstName\":\"Anthony\",\"lastName\":\"BREILLOT\",\"locationId\":9,\"email\":\"abreillot@dawan.fr\",\"job\":\"Graphiste Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":12050,\"firstName\":\"Pierre\",\"lastName\":\"BRETECHE\",\"locationId\":6,\"email\":\"pbreteche@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Php, Web\"},{\"id\":59043,\"firstName\":\"Justine\",\"lastName\":\"BROCHARD\",\"locationId\":6,\"email\":\"jbrochard@dawan.fr\",\"job\":\"Assistante Comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":854,\"firstName\":\"St\\u00e9phanie\",\"lastName\":\"BUI\",\"locationId\":6,\"email\":\"sbui@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":130414,\"firstName\":\"Jean-Ren\\u00e9\",\"lastName\":\"CALOVI\",\"locationId\":5,\"email\":\"jrcalovi@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":28002,\"firstName\":\"Gurvan\",\"lastName\":\"CARIOU\",\"locationId\":6,\"email\":\"gcariou@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, Web, Audiovisuel\"},{\"id\":167674,\"firstName\":\"R\\u00e9mi\",\"lastName\":\"CASTIEN\",\"locationId\":2,\"email\":\"rcastien@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167653,\"firstName\":\"Natacha\",\"lastName\":\"CHABAS\",\"locationId\":6,\"email\":\"nchabas@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119131,\"firstName\":\"Christian\",\"lastName\":\"CHAMPETIER\",\"locationId\":6,\"email\":\"cchampetier@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":186,\"firstName\":\"Damien\",\"lastName\":\"CHATELET\",\"locationId\":6,\"email\":\"dchatelet@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167672,\"firstName\":\"Zainab\",\"lastName\":\"CHROROU\",\"locationId\":2,\"email\":\"zchrorou@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167656,\"firstName\":\"Miguel\",\"lastName\":\"CLAIRY\",\"locationId\":7,\"email\":\"mclairy@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":3138,\"firstName\":\"David\",\"lastName\":\"CL\\u00c9MENT\",\"locationId\":7,\"email\":\"dclement@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, Linux, Delphi, XHTML, CSS, Javascript, Dremaweaver, Windows\"},{\"id\":144577,\"firstName\":\"Projets Internes\",\"lastName\":\"COMPTE DE TEST\",\"locationId\":6,\"email\":\"test@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167681,\"firstName\":\"Andr\\u00c9\",\"lastName\":\"COUTO SENTIEIRO\",\"locationId\":10,\"email\":\"acoutosentieiro@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":21609,\"firstName\":\"Pauline\",\"lastName\":\"D\\u0027ANASTASI\",\"locationId\":6,\"email\":\"pdanastasi@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156041,\"firstName\":\"Victor\",\"lastName\":\"DA COSTA FERREIRA\",\"locationId\":7,\"email\":\"vdacosta@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\\/3D\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167633,\"firstName\":\"Jade\",\"lastName\":\"DA SILVA LIMA\",\"locationId\":7,\"email\":\"jdasilva@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119134,\"firstName\":\"C\\u00e9cile\",\"lastName\":\"DABIN\",\"locationId\":6,\"email\":\"cdabin@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":21608,\"firstName\":\"Suzy\",\"lastName\":\"DABIN\",\"locationId\":6,\"email\":\"sdabin@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167680,\"firstName\":\"Leo\",\"lastName\":\"DARCES\",\"locationId\":10,\"email\":\"ldarces@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":76296,\"firstName\":\"Christophe\",\"lastName\":\"DAVID\",\"locationId\":6,\"email\":\"cdavid@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":143123,\"firstName\":\"Relations Externes\",\"lastName\":\"DAWAN\",\"locationId\":6,\"email\":\"relations-ext@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98002,\"firstName\":\"Laura\",\"lastName\":\"DEBRIE\",\"locationId\":6,\"email\":\"ldebrie@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167662,\"firstName\":\"Ana\\u00efs\",\"lastName\":\"DEBUSSCHERE\",\"locationId\":2,\"email\":\"adebusschere@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167671,\"firstName\":\"Damien\",\"lastName\":\"DELAERE\",\"locationId\":2,\"email\":\"ddelaere@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":65714,\"firstName\":\"Guillaume\",\"lastName\":\"DELANOY\",\"locationId\":2,\"email\":\"gdelanoy@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":6346,\"firstName\":\"Mamadou\",\"lastName\":\"DEMBELE\",\"locationId\":6,\"email\":\"mdembele@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, WEB\"},{\"id\":98003,\"firstName\":\"Fr\\u00e9d\\u00e9rique\",\"lastName\":\"DENIAUD\",\"locationId\":6,\"email\":\"fdeniaud@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":763,\"firstName\":\"Mohamed\",\"lastName\":\"DERKAOUI\",\"locationId\":7,\"email\":\"mderkaoui@dawan.fr\",\"job\":\"Manager, Formateur\",\"name\":\"DAWAN\",\"skill\":\"JAVA SE \\/ JAVA EE .NET : C#, VB.NET, ASP.NET D\\u00e9veloppement Mobile Android Web : Initiation \\u00e0 Expert, HTML\\/CSS, JS, jQuery, Ajax, BootstrapProgrammation C, C++, Delphi Gestion de projets : Agile, MS Project, Jenkins\"},{\"id\":167648,\"firstName\":\"Victor\",\"lastName\":\"DESILES\",\"locationId\":6,\"email\":\"vdesiles@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":130419,\"firstName\":\"Pascal\",\"lastName\":\"DEVAUX\",\"locationId\":5,\"email\":\"pdevaux@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\\/Bureautique\",\"name\":\"DAWAN\",\"skill\":\"CAO \\u0026 animation 3D : Solidworks \\/ Autocad \\/ Catia \\/ Creo \\/ Sketchup \\/ 3Ds Max\"},{\"id\":51642,\"firstName\":\"Abd-Raouf\",\"lastName\":\"DJILALI\",\"locationId\":7,\"email\":\"adjilali@dawan.fr\",\"job\":\"Formateur Bureautique\",\"name\":\"DAWAN\",\"skill\":\"Word, Excel\\/VBA, Access\"},{\"id\":167661,\"firstName\":\"Magloire\",\"lastName\":\"DJOMOLITE-BOLENDEA\",\"locationId\":7,\"email\":\"mdjomolite-bolendea@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167635,\"firstName\":\"Alfoussein\",\"lastName\":\"DOUCOURE\",\"locationId\":7,\"email\":\"adoucoure@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159320,\"firstName\":\"Nicolas\",\"lastName\":\"DUFLOT\",\"locationId\":2,\"email\":\"nduflot@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119133,\"firstName\":\"Sandie\",\"lastName\":\"DUPONT\",\"locationId\":6,\"email\":\"sdupont@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159321,\"firstName\":\"Guillaume\",\"lastName\":\"DUVERNEUIL\",\"locationId\":6,\"email\":\"gduverneuil@dawan.fr\",\"job\":\"Formateur nantes\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":39793,\"firstName\":\"Aur\\u00e9lie\",\"lastName\":\"EBURDERY\",\"locationId\":6,\"email\":\"aeburdery@EXdawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":88374,\"firstName\":\"Beno\\u00eet\",\"lastName\":\"ECHAPPE\",\"locationId\":6,\"email\":\"bechappe@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167685,\"firstName\":\"LILA\",\"lastName\":\"ELAB\",\"locationId\":10,\"email\":\"lelab@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4,\"firstName\":\"Guillaume\",\"lastName\":\"ESTIVAL\",\"locationId\":6,\"email\":\"gestival@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167651,\"firstName\":\"Cl\\u00e9ment\",\"lastName\":\"ETIENNE\",\"locationId\":6,\"email\":\"cetienne@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":173559,\"firstName\":\"Laurence\",\"lastName\":\"ETIENNE\",\"locationId\":6,\"email\":\"letienne@dawan.fr\",\"job\":\"Assistance facturation et recouvrement\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167630,\"firstName\":\"Maxime\",\"lastName\":\"FAUDUET\",\"locationId\":6,\"email\":\"mfauduet@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":153188,\"firstName\":\"Camille\",\"lastName\":\"FAVROT\",\"locationId\":6,\"email\":\"cfavrot@dawan.fr\",\"job\":\"Formatrice et charg\\u00e9e E-Marketing\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167687,\"firstName\":\"Nicolas\",\"lastName\":\"FELIX\",\"locationId\":7,\"email\":\"nfelix@jehann.fr\",\"job\":\"DW\",\"name\":\"JEHANN\",\"skill\":\"\"},{\"id\":10909,\"firstName\":\"Romain\",\"lastName\":\"FLACHER\",\"locationId\":6,\"email\":\"rflacher@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Developpement\"},{\"id\":156022,\"firstName\":\"Christophe\",\"lastName\":\"FONTAINE\",\"locationId\":2,\"email\":\"cfontaine@dawan.fr\",\"job\":\"Formateur D\\u00e9veloppeur Java EE\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4206,\"firstName\":\"Ren\\u00e9\",\"lastName\":\"FRANCESCHI\",\"locationId\":6,\"email\":\"rfranceschi@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, Illustrator, Photoshop, Flash, in Design, Dreamweaver\"},{\"id\":156994,\"firstName\":\"Rapha\\u00ebl\",\"lastName\":\"GACHET\",\"locationId\":6,\"email\":\"rgachet@dawan.fr\",\"job\":\"Assistant commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":88378,\"firstName\":\"V\\u00e9ronique\",\"lastName\":\"GARNIER FOUCHE\",\"locationId\":6,\"email\":\"vgarnier@dawan.fr\",\"job\":\"Assistante Administrative\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":165143,\"firstName\":\"El Hadji\",\"lastName\":\"GAYE\",\"locationId\":6,\"email\":\"ehgaye@dawan.fr\",\"job\":\"Formateur Java\\/.Net\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":21610,\"firstName\":\"Eddy\",\"lastName\":\"GHADDAR\",\"locationId\":6,\"email\":\"eghaddar@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"E-Marketing, R\\u00e9f\\u00e9rencement, E-Mailing, R\\u00e9seaux sociaux\"},{\"id\":61989,\"firstName\":\"Florian\",\"lastName\":\"GICQUEL\",\"locationId\":6,\"email\":\"fgicquel@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167659,\"firstName\":\"Marie\",\"lastName\":\"GIGAUD\",\"locationId\":7,\"email\":\"mgigaud@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167643,\"firstName\":\"Eric\",\"lastName\":\"GIGONDAN\",\"locationId\":6,\"email\":\"egigondan@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167650,\"firstName\":\"Geoffrey\",\"lastName\":\"GIRAUD\",\"locationId\":6,\"email\":\"ggiraud@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156017,\"firstName\":\"Lo\\u00efc\",\"lastName\":\"GODIN\",\"locationId\":6,\"email\":\"lgodin@dawan.fr\",\"job\":\"Formateur et d\\u00e9veloppeur Web\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167631,\"firstName\":\"Laura Daniela\",\"lastName\":\"GODINEZ ARANA\",\"locationId\":6,\"email\":\"lgodinez@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4227,\"firstName\":\"Michel\",\"lastName\":\"GORBATKO\",\"locationId\":6,\"email\":\"mgorbatko@dawan.fr\",\"job\":\"Graphiste Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO CAO\"},{\"id\":2789,\"firstName\":\"M\\u00e9lanie\",\"lastName\":\"GOURE\",\"locationId\":6,\"email\":\"mgoure@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":88621,\"firstName\":\"Yves\",\"lastName\":\"GRUAU\",\"locationId\":6,\"email\":\"ygruau@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Management, Commercial\"},{\"id\":7761,\"firstName\":\"No\\u00e9\",\"lastName\":\"GUENEAU\",\"locationId\":6,\"email\":\"ngueneau@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":118049,\"firstName\":\"Bruno\",\"lastName\":\"GUERIN\",\"locationId\":10,\"email\":\"bguerin@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes bases de don\\u00e9es et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":36559,\"firstName\":\"Graziella\",\"lastName\":\"GUERRIER\",\"locationId\":6,\"email\":\"gguerrier@dawan.fr\",\"job\":\"Formatrice\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167646,\"firstName\":\"Axel\",\"lastName\":\"GUIHARD\",\"locationId\":6,\"email\":\"aguihard@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":113582,\"firstName\":\"Jean-Fran\\u00e7ois\",\"lastName\":\"GUILBERT\",\"locationId\":2,\"email\":\"jfguilbert@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":16006,\"firstName\":\"Ana\\u00efs\",\"lastName\":\"GUILLOIS\",\"locationId\":6,\"email\":\"aguillois@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":150092,\"firstName\":\"Ndiaye\",\"lastName\":\"HANN\",\"locationId\":10,\"email\":\"nhann@dawan.fr\",\"job\":\"D\\u00e9veloppeur Web\\/Java\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":51647,\"firstName\":\"Aymen\",\"lastName\":\"HARBAOUI\",\"locationId\":6,\"email\":\"aharbaoui@dawan.fr\",\"job\":\"Formateur Web\",\"name\":\"DAWAN\",\"skill\":\"Web, Prestashop\"},{\"id\":5955,\"firstName\":\"Yoann\",\"lastName\":\"HARDY\",\"locationId\":6,\"email\":\"yhardy@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, HTML, Flash\"},{\"id\":168092,\"firstName\":\"Valentin\",\"lastName\":\"HEGRON\",\"locationId\":6,\"email\":\"vhegron@dawan.fr\",\"job\":\"Assistant administratif recrutement et formation\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159958,\"firstName\":\"Guillaume\",\"lastName\":\"HENRY\",\"locationId\":3,\"email\":\"ghenry@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, Audiovisuel\"},{\"id\":173186,\"firstName\":\"Corentin\",\"lastName\":\"HERDUIN\",\"locationId\":23,\"email\":\"cherduin@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167667,\"firstName\":\"Pierre-Alin\",\"lastName\":\"HERVO\",\"locationId\":2,\"email\":\"phervo@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":24824,\"firstName\":\"Yvonnick\",\"lastName\":\"HERVY\",\"locationId\":6,\"email\":\"yhervy@dawan.fr\",\"job\":\"Manager Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":159911,\"firstName\":\"Bruno\",\"lastName\":\"HOUDAYER\",\"locationId\":3,\"email\":\"bhoudayer@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167627,\"firstName\":\"Yosra\",\"lastName\":\"HOUIMLI\",\"locationId\":6,\"email\":\"yhouimli@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167652,\"firstName\":\"Tiphaine\",\"lastName\":\"IMBERT\",\"locationId\":6,\"email\":\"timbert@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":76485,\"firstName\":\"Jean Patrice\",\"lastName\":\"IWOGO\",\"locationId\":7,\"email\":\"jpiwogo@dawan.fr\",\"job\":\"Formateur Graphiste\",\"name\":\"DAWAN\",\"skill\":\"web pao\"},{\"id\":167637,\"firstName\":\"Sylvain\",\"lastName\":\"JANET\",\"locationId\":7,\"email\":\"sjanet@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167644,\"firstName\":\"Jocelyn\",\"lastName\":\"JANNIN\",\"locationId\":6,\"email\":\"jjannin@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4437,\"firstName\":\"Micka\\u00ebl\",\"lastName\":\"JAQUA\",\"locationId\":6,\"email\":\"mjaqua@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":11384,\"firstName\":\"Marjolaine\",\"lastName\":\"JUGEAU\",\"locationId\":6,\"email\":\"mjugeau@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":171918,\"firstName\":\"Woodson\",\"lastName\":\"JUSTE\",\"locationId\":10,\"email\":\"wjuste@dawan.fr\",\"job\":\"Formateur Web\\/Java\\/.NET\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":44624,\"firstName\":\"Thomas\",\"lastName\":\"KERNEM-OM\",\"locationId\":6,\"email\":\"tkernemom@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Business Intelligence\"},{\"id\":40295,\"firstName\":\"Fr\\u00e9d\\u00e9ric\",\"lastName\":\"KIBANZA MAKUIZA\",\"locationId\":7,\"email\":\"fkibanza@dawan.fr\",\"job\":\"Formateur \\/ Administrateur Syst\\u00e8mes\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":170228,\"firstName\":\"Steve\",\"lastName\":\"KOSSOUHO\",\"locationId\":6,\"email\":\"skossouho@dawan.fr\",\"job\":\"Formateur Python \\/ Web \\/ Programmation\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167657,\"firstName\":\"Elliot\",\"lastName\":\"KRAMDI\",\"locationId\":7,\"email\":\"ekramdi@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167641,\"firstName\":\"Denis\",\"lastName\":\"KUCUK\",\"locationId\":7,\"email\":\"dkucuk@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":29744,\"firstName\":\"Marie\",\"lastName\":\"LAFAURIE\",\"locationId\":6,\"email\":\"mlafaurie@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":169209,\"firstName\":\"Matthieu\",\"lastName\":\"LAMAMRA\",\"locationId\":6,\"email\":\"mlamamra@dawan.fr\",\"job\":\"Formateur syst\\u00e8me\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":61990,\"firstName\":\"Am\\u00e9lie\",\"lastName\":\"LAMBARD\",\"locationId\":6,\"email\":\"alambard@dawan.fr\",\"job\":\"Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":11383,\"firstName\":\"Christophe\",\"lastName\":\"LAMI\",\"locationId\":6,\"email\":\"clami@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":119135,\"firstName\":\"Elisa\",\"lastName\":\"LASNIER\",\"locationId\":6,\"email\":\"elasnier@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167655,\"firstName\":\"Guillaume\",\"lastName\":\"LAURENS\",\"locationId\":7,\"email\":\"glaurens@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":107590,\"firstName\":\"Benjamin\",\"lastName\":\"LAURENT\",\"locationId\":6,\"email\":\"blaurent@dawan.fr\",\"job\":\"Assistant Comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167663,\"firstName\":\"Fabien\",\"lastName\":\"LAURENT\",\"locationId\":2,\"email\":\"flaurent@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167629,\"firstName\":\"Fabien\",\"lastName\":\"LAUTRU\",\"locationId\":6,\"email\":\"flautru@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":14782,\"firstName\":\"Martin\",\"lastName\":\"LAVOQUET\",\"locationId\":6,\"email\":\"\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":78821,\"firstName\":\"Roxane\",\"lastName\":\"LE BOURG\",\"locationId\":6,\"email\":\"rlebourg@dawan.fr\",\"job\":\"Charg\\u00e9e de formations et RH\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":50037,\"firstName\":\"Pauline\",\"lastName\":\"LE GOFF\",\"locationId\":6,\"email\":\"plegoff@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":153170,\"firstName\":\"Jean-Christophe\",\"lastName\":\"LEBEAU\",\"locationId\":3,\"email\":\"jclebeau@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\\/3D\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":7441,\"firstName\":\"Denis\",\"lastName\":\"LECLERC\",\"locationId\":6,\"email\":\"dleclerc@dawan.fr\",\"job\":\"Manager - Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":167303,\"firstName\":\"Martin\",\"lastName\":\"LEDRUT\",\"locationId\":2,\"email\":\"mledrut@dawan.fr\",\"job\":\"Formateur PAO et Audiovisuel\",\"name\":\"DAWAN\",\"skill\":\"Photoshop, Illustrator, InDesign, After Effect, Premi\\u00e8re Pro\"},{\"id\":586,\"firstName\":\"Fanny\",\"lastName\":\"LEFERT\",\"locationId\":6,\"email\":\"flefert@dawan.fr\",\"job\":\"Graphiste Formatrice\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":78707,\"firstName\":\"Sophie\",\"lastName\":\"LELOU\",\"locationId\":6,\"email\":\"slelou@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":114287,\"firstName\":\"Aur\\u00e9lien\",\"lastName\":\"LESLUYE\",\"locationId\":1,\"email\":\"alesluye@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156563,\"firstName\":\"Yohann\",\"lastName\":\"LESUEUR\",\"locationId\":27,\"email\":\"ylesueur@dawan.fr\",\"job\":\"Formateur d\\u00e9veloppeur informatique\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":104939,\"firstName\":\"Annabelle\",\"lastName\":\"LEVOY\",\"locationId\":2,\"email\":\"alevoy@dawan.fr\",\"job\":\"Conseill\\u00e8re Emploi Formation\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167645,\"firstName\":\"Maxence\",\"lastName\":\"LOGODIN\",\"locationId\":6,\"email\":\"mlogodin@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98411,\"firstName\":\"Christopher\",\"lastName\":\"LORENT\",\"locationId\":6,\"email\":\"clorent@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Java .Net\"},{\"id\":4205,\"firstName\":\"Vincent\",\"lastName\":\"LUCY\",\"locationId\":6,\"email\":\"vlucy@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Linux, Apache, Shell, Postfix\"},{\"id\":39792,\"firstName\":\"Romain\",\"lastName\":\"LUNEAU\",\"locationId\":6,\"email\":\"rluneau@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156024,\"firstName\":\"Eric\",\"lastName\":\"MADRANGE\",\"locationId\":27,\"email\":\"emadrange@dawan.fr\",\"job\":\"Formateur d\\u00e9veloppeur Web\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167676,\"firstName\":\"Anssaka\",\"lastName\":\"MAHAMOUDOU\",\"locationId\":10,\"email\":\"amahamoudou@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156026,\"firstName\":\"Mourad\",\"lastName\":\"MAHRANE\",\"locationId\":27,\"email\":\"mmahrane@dawan.fr\",\"job\":\"Formateur d\\u00e9veloppeur informatique\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167632,\"firstName\":\"Simon\",\"lastName\":\"MALPEL\",\"locationId\":6,\"email\":\"smalpel@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":54664,\"firstName\":\"Harmony\",\"lastName\":\"MARCHAND\",\"locationId\":6,\"email\":\"hmarchand@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":29742,\"firstName\":\"Alexandre\",\"lastName\":\"MARIE-LUCE\",\"locationId\":6,\"email\":\"amarieluce@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":36846,\"firstName\":\"Benjamin\",\"lastName\":\"MARRON\",\"locationId\":3,\"email\":\"bmarron@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"JEE Android .Net\"},{\"id\":11337,\"firstName\":\"Kevin\",\"lastName\":\"MARTIN\",\"locationId\":6,\"email\":\"kmartin@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Web, VBA, MsProject\"},{\"id\":167686,\"firstName\":\"MARCO\",\"lastName\":\"MARTIN\",\"locationId\":10,\"email\":\"mmartin@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167639,\"firstName\":\"Romain\",\"lastName\":\"MAURY\",\"locationId\":7,\"email\":\"rmaury@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":54283,\"firstName\":\"R\\u00e9mi\",\"lastName\":\"MAYEUX\",\"locationId\":2,\"email\":\"rmayeux@dawan.fr\",\"job\":\"Graphiste Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":7016,\"firstName\":\"St\\u00e9phane\",\"lastName\":\"MENUT\",\"locationId\":2,\"email\":\"smenut@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, HTML, XHTML, CSS, Javascript, Excel, Word\"},{\"id\":8,\"firstName\":\"Christelle\",\"lastName\":\"MERCKLING\",\"locationId\":7,\"email\":\"cmerckling@dawan.fr\",\"job\":\"Associ\\u00e9e\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":2,\"firstName\":\"J\\u00e9r\\u00f4me\",\"lastName\":\"MERCKLING\",\"locationId\":6,\"email\":\"jmerckling@dawan.fr\",\"job\":\"G\\u00e9rant\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167654,\"firstName\":\"Lo\\u00efc\",\"lastName\":\"METRINGER\",\"locationId\":7,\"email\":\"lmetringer@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167682,\"firstName\":\"Haidar\",\"lastName\":\"MEZIDI\",\"locationId\":10,\"email\":\"hmezidi@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":258,\"firstName\":\"Olivier\",\"lastName\":\"MICHAUD\",\"locationId\":6,\"email\":\"omichaud@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":121907,\"firstName\":\"Ayla\",\"lastName\":\"MICHENAUD\",\"locationId\":6,\"email\":\"amichenaud@dawan.fr\",\"job\":\"Charg\\u00e9e relations ressources externes\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167647,\"firstName\":\"Florian\",\"lastName\":\"MIOT\",\"locationId\":6,\"email\":\"fmiot@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":172002,\"firstName\":\"Audrey\",\"lastName\":\"MOAL\",\"locationId\":6,\"email\":\"amoal@dawan.fr\",\"job\":\"Manager Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":1809,\"firstName\":\"Olivier\",\"lastName\":\"MOCHK\",\"locationId\":6,\"email\":\"omochk@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes Windows\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, ISA Server, Exchange, Active Directory\"},{\"id\":167642,\"firstName\":\"Ahmed Reda\",\"lastName\":\"MOKHTARI\",\"locationId\":7,\"email\":\"amokhtari@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":145831,\"firstName\":\"Yoann\",\"lastName\":\"MOUI\",\"locationId\":10,\"email\":\"ymoui@dawan.fr\",\"job\":\"Formateur Bureautique\",\"name\":\"DAWAN\",\"skill\":\"Bureautique\"},{\"id\":95885,\"firstName\":\"Rachid\",\"lastName\":\"NEDJAR\",\"locationId\":7,\"email\":\"rnedjar@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167383,\"firstName\":\"Marie-Claire\",\"lastName\":\"NGOM\",\"locationId\":7,\"email\":\"mcngom@dawan.fr\",\"job\":\"Formatrice Management\\/Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167636,\"firstName\":\"Valentin\",\"lastName\":\"NGUYEN\",\"locationId\":7,\"email\":\"vnguyen@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":74418,\"firstName\":\"Nicolas\",\"lastName\":\"NIRDE\",\"locationId\":7,\"email\":\"nnirde@dawan.fr\",\"job\":\"Formateur PAO\\/DAO\",\"name\":\"DAWAN\",\"skill\":\"Autocad Revit ...\"},{\"id\":35316,\"firstName\":\"Paola\",\"lastName\":\"NUNES\",\"locationId\":6,\"email\":\"pnunes@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":6965,\"firstName\":\"Benjamin\",\"lastName\":\"OERTEL\",\"locationId\":6,\"email\":\"boertel@dawan.fr\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167669,\"firstName\":\"Nelida\",\"lastName\":\"OTAN\",\"locationId\":2,\"email\":\"notan@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167660,\"firstName\":\"Hannah\",\"lastName\":\"OUASSI\",\"locationId\":7,\"email\":\"houassi@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":76295,\"firstName\":\"Emma\",\"lastName\":\"PARFAIT\",\"locationId\":6,\"email\":\"eparfait@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":38878,\"firstName\":\"Kevin\",\"lastName\":\"PAUTONNIER\",\"locationId\":6,\"email\":\"kpautonnier@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167640,\"firstName\":\"Aser Amagana\",\"lastName\":\"PEROU\",\"locationId\":7,\"email\":\"aperou@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":111890,\"firstName\":\"Sol\\u00e8ne\",\"lastName\":\"PERROT\",\"locationId\":6,\"email\":\"sperrot@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":50036,\"firstName\":\"H\\u00e9lo\\u00efse\",\"lastName\":\"PETIT\",\"locationId\":6,\"email\":\"hpetit@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":164130,\"firstName\":\"Marie-Claire\",\"lastName\":\"PETIT\",\"locationId\":3,\"email\":\"mcpetit@dawan.fr\",\"job\":\"CEF\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":853,\"firstName\":\"Maude\",\"lastName\":\"PETIT\",\"locationId\":6,\"email\":\"mpetit@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":73086,\"firstName\":\"Luc\",\"lastName\":\"PHAN\",\"locationId\":6,\"email\":\"lphan@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Python, Perl, PHP, HTML, CSS, Javascript, SQL, C\\/C++, Java\"},{\"id\":7484,\"firstName\":\"Gilles\",\"lastName\":\"PIETRI (OLD)\",\"locationId\":6,\"email\":\"gilles0972@dawan.fr\",\"job\":\"Manager Formateur\",\"name\":\"DAWAN\",\"skill\":\"Linux, VMware, Apache,\"},{\"id\":98000,\"firstName\":\"Aimeric\",\"lastName\":\"PLAGES\",\"locationId\":6,\"email\":\"aplages@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":30126,\"firstName\":\"Fr\\u00e9d\\u00e9ric\",\"lastName\":\"PLISSON\",\"locationId\":6,\"email\":\"fplisson@dawan.fr\",\"job\":\"Formateur E-Marketing\",\"name\":\"DAWAN\",\"skill\":\"E-Marketing, SEO, R\\u00e9f\\u00e9rencement, Webmaster, Chef de projet, Ergo\"},{\"id\":156032,\"firstName\":\"Christine\",\"lastName\":\"PLOQUIN\",\"locationId\":6,\"email\":\"cploquin@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167665,\"firstName\":\"Quentin\",\"lastName\":\"PLOUVIER\",\"locationId\":2,\"email\":\"qplouvier@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":8661,\"firstName\":\"Isabelle\",\"lastName\":\"POIROUD\",\"locationId\":6,\"email\":\"ipoiroud@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167634,\"firstName\":\"Nicolas\",\"lastName\":\"POTIER\",\"locationId\":7,\"email\":\"npotier@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167675,\"firstName\":\"Chlo\\u00c9\",\"lastName\":\"POUPON\",\"locationId\":10,\"email\":\"cpoupon@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167688,\"firstName\":\"Vannah\",\"lastName\":\"RANAIVOSON\",\"locationId\":7,\"email\":\"vranaivoson@jehann.fr\",\"job\":\"DW\",\"name\":\"JEHANN\",\"skill\":\"\"},{\"id\":113543,\"firstName\":\"Liana\",\"lastName\":\"RAVAHIMANANA\",\"locationId\":6,\"email\":\"lravahi@dawan.fr\",\"job\":\"Charg\\u00e9e de Recrutement\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156035,\"firstName\":\"Valentin\",\"lastName\":\"RAVAILLE\",\"locationId\":6,\"email\":\"vravaille@dawan.fr\",\"job\":\"Assistant commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167664,\"firstName\":\"Soufiane\",\"lastName\":\"RECHIA\",\"locationId\":2,\"email\":\"srechia@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167668,\"firstName\":\"Ana\\u00efs\",\"lastName\":\"REGNIER\",\"locationId\":2,\"email\":\"aregnier@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":160375,\"firstName\":\"Thais\",\"lastName\":\"REVILLON\",\"locationId\":2,\"email\":\"trevillon@dawan.fr\",\"job\":\"Formatrice er d\\u00e9veloppeuse Web\\/Java\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":15646,\"firstName\":\"Pascal\",\"lastName\":\"RICH\\u00c9\",\"locationId\":3,\"email\":\"priche@dawan.fr\",\"job\":\"Manager - Formateur\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO 2D\\/3D, Coaching\"},{\"id\":157020,\"firstName\":\"Melvyn\",\"lastName\":\"ROBERT\",\"locationId\":6,\"email\":\"mrobert@dawan.fr\",\"job\":\"Assistant comptable\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":39555,\"firstName\":\"Yves\",\"lastName\":\"ROCAMORA (OLD)\",\"locationId\":6,\"email\":\"yrocamora@dawan.fr\",\"job\":\"Formateur Web\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":2426,\"firstName\":\"Nathalie\",\"lastName\":\"ROISSE\",\"locationId\":6,\"email\":\"nroisse@dawan.fr\",\"job\":\"Formatrice Administratrice Windows Server\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, ISA Server, Exchange, Active Directory\"},{\"id\":173750,\"firstName\":\"Enzo\",\"lastName\":\"ROSARIO\",\"locationId\":5,\"email\":\"erosario@dawan.fr\",\"job\":\"Formateur BI \\/ Programmation \\/ Linux\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":131102,\"firstName\":\"Thierry\",\"lastName\":\"ROUE\",\"locationId\":10,\"email\":\"troue@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156037,\"firstName\":\"Marie\",\"lastName\":\"ROY\",\"locationId\":6,\"email\":\"mroy@dawan.fr\",\"job\":\"Assistante commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98882,\"firstName\":\"Pierre\",\"lastName\":\"ROYER\",\"locationId\":6,\"email\":\"nsp@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux\",\"name\":\"DAWAN\",\"skill\":\"Linux, VMware, R\\u00e9seaux, LPI, Docker, DevOps\"},{\"id\":123848,\"firstName\":\"Pierre\",\"lastName\":\"SABLE\",\"locationId\":6,\"email\":\"psable@dawan.fr\",\"job\":\"Formateur Syst\\u00e8mes et DevOps\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":7760,\"firstName\":\"Aude\",\"lastName\":\"SAMSON\",\"locationId\":6,\"email\":\"asamson@dawan.fr\",\"job\":\"Assistante Commerciale\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":156125,\"firstName\":\"Christiane-Marie\",\"lastName\":\"SCIEUR\",\"locationId\":3,\"email\":\"cmscieur@dawan.fr\",\"job\":\"Formatrice bureautique\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167683,\"firstName\":\"Tidiane\",\"lastName\":\"SOW\",\"locationId\":10,\"email\":\"tsow@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167628,\"firstName\":\"Erwan\",\"lastName\":\"STEPHANT\",\"locationId\":6,\"email\":\"estephant@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":16397,\"firstName\":\"Martin\",\"lastName\":\"STIEVENART\",\"locationId\":2,\"email\":\"mstievenart@dawan.fr\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"Php\"},{\"id\":98413,\"firstName\":\"Cedric\",\"lastName\":\"SURQUIN (OLD)\",\"locationId\":2,\"email\":\"csurquin231@dawan.fr\",\"job\":\"Administrateur Syst\\u00e8mes et R\\u00e9seaux, D\\u00e9veloppeur\",\"name\":\"DAWAN\",\"skill\":\"Windows Server, Linux, VMware\"},{\"id\":167625,\"firstName\":\"Kanha\",\"lastName\":\"SYDAPHASAVANH\",\"locationId\":6,\"email\":\"ksydaphasavanh@dawan.fr\",\"job\":\"CDA\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":20917,\"firstName\":\"Najib\",\"lastName\":\"TALAA\",\"locationId\":6,\"email\":\"ntalaa@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":98001,\"firstName\":\"Alain\",\"lastName\":\"TANGUY\",\"locationId\":6,\"email\":\"atanguy@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":4436,\"firstName\":\"Florian\",\"lastName\":\"TEMPLIER\",\"locationId\":6,\"email\":\"ftemplier@dawan.fr\",\"job\":\"Assistant Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":16398,\"firstName\":\"Rajenthen\",\"lastName\":\"THARMABALAN\",\"locationId\":7,\"email\":\"rtharmabalan@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"Administration Serveur\"},{\"id\":167670,\"firstName\":\"Victor\",\"lastName\":\"TIMMERMAN\",\"locationId\":2,\"email\":\"vtimmerman@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167649,\"firstName\":\"Sibel\",\"lastName\":\"URBAIN\",\"locationId\":6,\"email\":\"surbain@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167679,\"firstName\":\"Cesar Aim\\u00c9\",\"lastName\":\"VAILLANT\",\"locationId\":10,\"email\":\"cvaillant@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":42528,\"firstName\":\"Thibault\",\"lastName\":\"VAN WYNENDAELE\",\"locationId\":2,\"email\":\"tvanwynendaele@dawan.fr\",\"job\":\"Formateur, D\\u00e9veloppeur\",\"name\":\"DAWAN\",\"skill\":\"php, android, objective-c, swift\"},{\"id\":20126,\"firstName\":\"Maxime\",\"lastName\":\"VANWYNSBERGHE\",\"locationId\":4,\"email\":\"mvanwynsberghe@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"PAO, DAO, CAO, audiovisuel\"},{\"id\":2223,\"firstName\":\"C\\u00e9dric\",\"lastName\":\"VASSEUR\",\"locationId\":6,\"email\":\"cvasseur@dawan.fr\",\"job\":\"Formateur\",\"name\":\"DAWAN\",\"skill\":\"PHP, Java, HTML, Dreamweaver, Photoshop, Access, VBA, Excel\"},{\"id\":83389,\"firstName\":\"Richard\",\"lastName\":\"VERDON\",\"locationId\":6,\"email\":\"rverdon@dawan.fr\",\"job\":\"Commercial\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":116087,\"firstName\":\"Cyprien\",\"lastName\":\"VIDREQUIN\",\"locationId\":6,\"email\":\"cvidrequin@jehann.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167677,\"firstName\":\"Somsanouk\",\"lastName\":\"VONGKINGKEO\",\"locationId\":10,\"email\":\"svongkingkeo@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":608,\"firstName\":\"Vincent\",\"lastName\":\"VOYER\",\"locationId\":6,\"email\":\"vvoyer@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":115637,\"firstName\":\"Patience\",\"lastName\":\"WAITNAME\",\"locationId\":6,\"email\":\"pwaitname@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":167666,\"firstName\":\"Aur\\u00e9lien\",\"lastName\":\"WALTER\",\"locationId\":2,\"email\":\"awalter@dawan.fr\",\"job\":\"DW\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":6964,\"firstName\":\"Hongbin\",\"lastName\":\"XU\",\"locationId\":6,\"email\":\"hxu@dawan.fr\",\"job\":\"Stagiaire\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":163963,\"firstName\":\"Amenan\",\"lastName\":\"YAO\",\"locationId\":27,\"email\":\"ayao@dawan.fr\",\"job\":\"CEF\",\"name\":\"DAWAN\",\"skill\":\"\"},{\"id\":7412,\"firstName\":\"Othman\",\"lastName\":\"YAQOUBI\",\"locationId\":6,\"email\":\"oyaqoubi@dawan.fr\",\"job\":\"\",\"name\":\"DAWAN\",\"skill\":\"\"}]";
		ResponseEntity<String> res = new ResponseEntity<String>(body, HttpStatus.OK);
		when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
				.thenReturn(res);
		when(userMapper.userDG2DtoToUser(any(UserDG2Dto.class))).thenReturn(userFromDG2);
		when(userRepository.findByEmail(any(String.class))).thenReturn(null);
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(userFromDG2);

		// assert
		assertDoesNotThrow(() -> {
			userService.fetchAllDG2Users("userEmail", "userPassword");
		});
	}
	
	@Test
	void shouldResetPassword() throws Exception {
		
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		when(userRepository.findByEmail(email)).thenReturn(uList.get(0));
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(uList.get(0));
		
		boolean resetStatus = userService.resetPassword(resetResponse);
		
		assertThat(resetStatus).isTrue();
	}
	
	@Test
	void shouldResetPasswordWhenUserNotFound() throws Exception {
		
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		when(userRepository.findByEmail(email)).thenReturn(null);
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(null);
		
		boolean resetStatus = userService.resetPassword(resetResponse);
		
		assertThat(resetStatus).isFalse();
	}
	
	@Test
	void shouldResetPasswordWhenSameAsOld() throws Exception {
		resetResponse.setPassword(uList.get(0).getPassword());
		
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		when(userRepository.findByEmail(email)).thenReturn(uList.get(0));
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(uList.get(0));
		
		boolean resetStatus = userService.resetPassword(resetResponse);
		
		assertThat(resetStatus).isFalse();
	}
}
