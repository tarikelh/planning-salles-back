package fr.dawan.calendarproject.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.UserDG2Dto;
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

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<AdvancedUserDto> getAllUsers() {
		List<User> users = userRepository.findAll();

		List<AdvancedUserDto> result = new ArrayList<AdvancedUserDto>();

		for (User u : users) {
			result.add(userMapper.userToAdvancedUserDto(u));
		}

		return result;
	}

	@Override
	public List<AdvancedUserDto> getAllUsers(int page, int size, String search) {
		Pageable pagination = null;

		if (page > -1 && size > 0)
			pagination = PageRequest.of(page, size);
		else
			pagination = Pageable.unpaged();

		List<User> users = userRepository
				.findAllByFirstNameContainingOrLastNameContainingOrEmailContaining(search, search, search, pagination)
				.get().collect(Collectors.toList());
		List<AdvancedUserDto> result = new ArrayList<AdvancedUserDto>();

		for (User u : users) {
			result.add(userMapper.userToAdvancedUserDto(u));
		}

		return result;
	}

	@Override
	public CountDto count(String search) {
		return new CountDto(
				userRepository.countByFirstNameContainingOrLastNameContainingOrEmailContaining(search, search, search));
	}

	@Override
	public List<AdvancedUserDto> getAllUsersByType(String type) {
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			List<User> users = userRepository.findAllByType(userType);
			List<AdvancedUserDto> result = new ArrayList<AdvancedUserDto>();

			for (User u : users) {
				result.add(userMapper.userToAdvancedUserDto(u));
			}

			return result;
		} else {
			// HANDLE ERROR
			return null;
		}
	}

	@Override
	public AdvancedUserDto getById(long id) {
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent())
			return userMapper.userToAdvancedUserDto(user.get());
		else
			return null;
	}

	@Override
	public void deleteById(long id) {
		userRepository.deleteById(id);
	}

	@Override
	public AdvancedUserDto saveOrUpdate(AdvancedUserDto user) {
		if (user.getId() > 0 && !userRepository.findById(user.getId()).isPresent())
			return null;

		checkIntegrity(user);

		User u = userMapper.advancedUserDtoToUser(user);

		Set<Skill> skillsList = new HashSet<Skill>();

		if (user.getSkillsId() != null) {
			user.getSkillsId().forEach(id -> {
				skillsList.add(skillRepository.getOne(id));
			});
		}
		u.setSkills(skillsList);

		u.setLocation(locationRepository.getOne(user.getLocationId()));

		// Hash Password
		try {
			if (user.getId() == 0) {
				u.setPassword(HashTools.hashSHA512(u.getPassword()));
			} else {
				User userInDB = userRepository.getOne(u.getId());
				if (!userInDB.getPassword().equals(u.getPassword())) {
					u.setPassword(HashTools.hashSHA512(u.getPassword()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		u = userRepository.saveAndFlush(u);
		AdvancedUserDto advUser = userMapper.userToAdvancedUserDto(u);
		return advUser;
	}

	@Override
	public AdvancedUserDto findByEmail(String email) {
		User u = userRepository.findByEmail(email);

		if (u != null)
			return userMapper.userToAdvancedUserDto(u);

		return null;
	}

	public boolean checkIntegrity(AdvancedUserDto u) {
		Set<APIError> errors = new HashSet<APIError>();
		String instanceClass = u.getClass().toString();
		String path = "/api/users";
		// Location Must EXIST
		if (!locationRepository.findById(u.getLocationId()).isPresent()) {
			String message = "Location with id: " + u.getLocationId() + " does not exist.";
			errors.add(new APIError(301, instanceClass, "LocationNotFound", message, path));
		}

		// IF Skill > Must EXIST
		if (u.getSkillsId() != null) {
			for (long skillId : u.getSkillsId()) {
				if (!skillRepository.findById(skillId).isPresent()) {
					String message = "Skill with id: " + skillId + " does not exist.";
					errors.add(new APIError(302, instanceClass, "SkillNotFound", message, path));
				}
			}
		}

		// Email > valid, uniq
		if (!User.emailIsValid(u.getEmail())) {
			String message = "Email must be valid.";
			errors.add(new APIError(303, instanceClass, "InvalidEmail", message, path));
		}

		if (userRepository.findDuplicateEmail(u.getEmail(), u.getId()) != null) {
			String message = "Email already used.";
			errors.add(new APIError(304, instanceClass, "EmailNotUniq", message, path));
		}

		// password > 8 char min
		if (u.getPassword().length() < 8) {
			String message = "Password must be at least 8 characters long";
			errors.add(new APIError(305, instanceClass, "PasswordTooShort", message, path));
		}

		// Company + type enums must be valid
		if (!UserCompany.contains(u.getCompany())) {
			String message = "Company: " + u.getCompany() + " is not valid.";
			errors.add(new APIError(306, instanceClass, "UnknownUserCompany", message, path));
		}

		if (!UserType.contains(u.getType())) {
			String message = "Type: " + u.getType() + " is not valid.";
			errors.add(new APIError(307, instanceClass, "UnknownUserType", message, path));
		}
		// If Image > must exist
		if (!errors.isEmpty()) {
			throw new EntityFormatException(errors);
		}

		return true;
	}

	@Override
	public void fetchAllDG2Users(String email, String password) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<UserDG2Dto> lResJson = new ArrayList<UserDG2Dto>();

		URI url = new URI("https://dawan.org/api2/planning/employees");

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + password);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
			String json = repWs.getBody();

			try {
				lResJson = objectMapper.readValue(json, new TypeReference<List<UserDG2Dto>>() {
				});
				for (UserDG2Dto userDG2Dto : lResJson) {
					userDG2Dto.setType(userDG2JobToUserTypeString(userDG2Dto.getType()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (UserDG2Dto cDG2 : lResJson) {
				User userImported = userMapper.userDG2DtoToUser(cDG2);
				User userInDb = userRepository.findByEmail(userImported.getEmail());

				if (userInDb != null) {
					userInDb.setId(userImported.getId());
					userInDb.setCompany(userImported.getCompany());
					userInDb.setEmail(userImported.getEmail());
					userInDb.setEnumCompany(userImported.getEnumCompany());
					userInDb.setEnumType(userImported.getEnumType());
					userInDb.setFirstName(userImported.getFirstName());
					userInDb.setImagePath(userImported.getImagePath());
					userInDb.setLastName(userImported.getLastName());
					userInDb.setLocation(userImported.getLocation());
					userInDb.setPassword(userImported.getPassword());
					userInDb.setSkills(userImported.getSkills());
					userInDb.setType(userImported.getType());
					userInDb.setVersion(userImported.getVersion());
				} else {
					userInDb = userImported;
				}

				if (userInDb.getPassword() == null) {
					userInDb.setPassword(HashTools.hashSHA512("7ayh8j9bpcFyjYF6u+wc"));
				}
				userRepository.saveAndFlush(userInDb);
			}
		} else {
			throw new Exception("ResponseEntity from the webservice WDG2 not correct");
		}
	}

	private String userDG2JobToUserTypeString(String job) {
		if (job == null) {
			job = "";
		}
		if (!job.isEmpty()) {
			String lowerCaseJob = job.toLowerCase();
			if (lowerCaseJob.contains("cda") || lowerCaseJob.contains("dw") || lowerCaseJob.contains("apprenti")) {
				return UserType.APPRENTI.toString();
			} else if (lowerCaseJob.contains("format")) {
				return UserType.FORMATEUR.toString();
			} else if (lowerCaseJob.contains("admin")) {
				return UserType.ADMINISTRATIF.toString();
			}
		}
		return UserType.NOT_FOUND.toString();
	}
}
