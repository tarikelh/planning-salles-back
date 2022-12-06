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
import org.springframework.beans.factory.annotation.Value;
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
import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.HistoricDto;
import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.dto.TrainingDG2Dto;
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
import fr.dawan.calendarproject.tools.JwtTokenUtil;

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

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${vue.baseurl}")
	private String vueUrl;

	/**
	 * Fetches all of the existing users.
	 * 
	 * @return result Returns a list of users.
	 *
	 */

	@Override
	public List<AdvancedUserDto> getAllUsers() {
		List<User> users = userRepository.findAll();

		List<AdvancedUserDto> result = new ArrayList<>();

		for (User u : users) {
			result.add(userMapper.userToAdvancedUserDto(u));
		}

		return result;
	}

	/**
	 * Fetches all of the existing users, with a pagination system.
	 * 
	 * @param page   An integer representing the current page displaying the users.
	 * @param size   An integer defining the number of users displayed by page.
	 * @param search A String representing the admin's input to search for a
	 *               specific user.
	 * 
	 * @return result Returns a list of users, according to the pagination criteria.
	 *
	 */

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
		List<AdvancedUserDto> result = new ArrayList<>();

		for (User u : users) {
			result.add(userMapper.userToAdvancedUserDto(u));
		}

		return result;
	}

	/**
	 * Counts the number of users.
	 * 
	 * @param search A String representing the admin's input to search for a
	 *               specific user.
	 * 
	 * @return CountDto Returns the number of users, according to the search
	 *         criteria.
	 *
	 */
	@Override
	public CountDto count(String search) {
		return new CountDto(
				userRepository.countByFirstNameContainingOrLastNameContainingOrEmailContaining(search, search, search));
	}

	/**
	 * Fetches all of the existing users, according their type.
	 * 
	 * @param type A String designating the type of user search by the admin.
	 * 
	 * @return result Returns a list of users, according to the type criteria.
	 *
	 */

	@Override
	public List<AdvancedUserDto> getAllUsersByType(String type) {
		List<AdvancedUserDto> result = new ArrayList<>();
		type = type.toUpperCase();
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			List<User> users = userRepository.findAllByTypeAndEndDateIsNull(userType);

			for (User u : users) {
				result.add(userMapper.userToAdvancedUserDto(u));
			}

			return result;
		} else {
			return result;
		}
	}

	/**
	 * Fetches a single user, according to their id.
	 * 
	 * @param id An unique Integer used to identify each user.
	 * 
	 * @return AdvancedUserDto Returns a single user.
	 *
	 */

	@Override
	public AdvancedUserDto getById(long id) {
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent())
			return userMapper.userToAdvancedUserDto(user.get());
		else
			return null;
	}

	/**
	 * Delete a single user, according to their id.
	 * 
	 * @param id An unique Integer used to identify each user.
	 *
	 */

	@Override
	public void deleteById(long id) {
		userRepository.deleteById(id);
	}

	/**
	 * Adds a new user or updates an existing one.
	 * 
	 * @param user An object representing an User.
	 * 
	 * @return advUser Returns the newly created user or an updated one.
	 *
	 */

	@Override
	public AdvancedUserDto saveOrUpdate(AdvancedUserDto user) {
		if (user.getId() > 0 && !userRepository.findById(user.getId()).isPresent())
			return null;

		checkIntegrity(user);

		User u = userMapper.advancedUserDtoToUser(user);

		Set<Skill> skillsList = new HashSet<>();

		if (user.getSkills() != null) {
			user.getSkills().forEach(skill -> {

				Optional<Skill> skillinDb = skillRepository.findByTitle(skill);
				if (skillinDb.isPresent())
					skillsList.add(skillinDb.get());
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
		return userMapper.userToAdvancedUserDto(u);
	}

	/**
	 * Fetches a single user, according to their email.
	 * 
	 * @param email A String representing the user's email adress.
	 * 
	 * @return Returns a single user.
	 *
	 */

	@Override
	public AdvancedUserDto findByEmail(String email) {
		Optional<User> u = userRepository.findByEmail(email);

		if (u.isPresent())
			return userMapper.userToAdvancedUserDto(u.get());

		return null;
	}

	/**
	 * Checks whether a newly registered user is valid.
	 * 
	 * @param u An object representing an User.
	 * 
	 * @return Returns a boolean to say whether or not the user is correct.
	 *
	 */

	public boolean checkIntegrity(AdvancedUserDto u) {
		Set<APIError> errors = new HashSet<>();
		String instanceClass = u.getClass().toString();
		String path = "/api/users";
		// Location Must EXIST
		if (!locationRepository.findById(u.getLocationId()).isPresent()) {
			String message = "Location with id: " + u.getLocationId() + " does not exist.";
			errors.add(new APIError(301, instanceClass, "LocationNotFound", message, path));
		}

		// IF Skill > Must EXIST
		if (u.getSkills() != null) {
			for (String skill : u.getSkills()) {
				if (!skillRepository.findByTitle(skill).isPresent()) {
					String message = "Skill with id: " + skill + " does not exist.";
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
//		if (u.getPassword().length() < 8) {
//			String message = "Password must be at least 8 characters long";
//			errors.add(new APIError(305, instanceClass, "PasswordTooShort", message, path));
//		}

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
		// import des employées
		fetchAllDG2EmployeesOrFreelancers(email, password, false);

		// import des freelancers
		fetchAllDG2EmployeesOrFreelancers(email, password, true);
	}

	/**
	 * Fetches all users in the Dawan API.
	 * 
	 * @param email A String defining a user's email.
	 * @param pwd   A String defining a user's password.
	 * 
	 * @exception Exception Returns an exception if the request fails.
	 *
	 */

	public void fetchAllDG2EmployeesOrFreelancers(String email, String password, boolean freelancers) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<UserDG2Dto> lResJson = new ArrayList<>();
		String endPoint;
		if (freelancers) {
			endPoint = "freelancers";
		} else {
			endPoint = "employees";
		}

		URI url = new URI("https://dawan.org/api2/planning/" + endPoint);

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
					if (freelancers) {
						userDG2Dto.setType("INDEPENDANT");
					} else {
						userDG2Dto.setType(userDG2JobToUserTypeString(userDG2Dto.getType()));
					}

					userDG2Dto.setCompany(userDG2CompanyToUserCompanyString(userDG2Dto.getCompany()));

					if (userDG2Dto.getEndDate() == "" || userDG2Dto.getEndDate() == null)
						userDG2Dto.setEndDate(null);
					else {
						userDG2Dto.setEndDate(userDG2Dto.getEndDate().split("T")[0]);
					}

					if (userDG2Dto.getEmail() == null || userDG2Dto.getEmail() == "") {
						StringBuffer buffer = new StringBuffer();
						buffer.append(userDG2Dto.getFirstName()).append(userDG2Dto.getLastName())
								.append("@no-email.fr");
						userDG2Dto.setEmail(buffer.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (UserDG2Dto cDG2 : lResJson) {

				User userImported = userMapper.userDG2DtoToUser(cDG2);
				userImported.setLastName(userImported.getLastName().toUpperCase());
				userImported.setLocation(locationRepository.findByIdDg2(cDG2.getLocationId()).orElse(null));
				
				if (freelancers) {
					userImported.setEmployeeIdDg2(null);
				}
				try {
					userImported.setSkills(stringToSkillList(cDG2.getSkills()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				User user = userRepository.findByEmail(userImported.getEmail()).orElse(null);

				if (userImported.getPassword() == null) {
					if (userImported.getType() == UserType.ADMINISTRATIF) {
						userImported.setPassword(
								"23b70069ca9be765d92cd05afd7cf009a595732e3c8b477783672e1f0edb74ba01cff566a4fc1e8483da47f96dace545b5cf78540dc68630e06ffe97fc110619");

					} else {
						userImported.setPassword(
								"1ccf2b75e2131f50f791b4589a9af59f4e69d9c2f6199f494a6207128f14d222d81f1db3b59cb94045ac1c71c4e008cfaffc9802e273cbe7d378eac0c1360e38");
					}
				}

				if (user != null) {
					if (userImported.getId() == 0)
						userImported.setId(user.getId());

					if (!user.equals(userImported) && !freelancers) {
						userImported.setId(user.getId());
						try {
							userRepository.saveAndFlush(userImported);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						userRepository.saveAndFlush(userImported);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new Exception("ResponseEntity from the webservice WDG2 not correct");
		}
	}

	/**
	 * Returns a role depending on the user's job.
	 * 
	 * @param job A String defining the user's job.
	 * 
	 * @return String Returns the user's role.
	 *
	 */

	private String userDG2JobToUserTypeString(String job) {
		if (job == null) {
			job = "";
		}
		String lowerCaseJob = job.toLowerCase();

		if (lowerCaseJob.contains("associé") || lowerCaseJob.contains("gérant") || lowerCaseJob.contains("manager") || lowerCaseJob.contains("Assistante Administrative")) {
			return UserType.ADMINISTRATIF.toString();
		} else if (lowerCaseJob.contains("formateur") || lowerCaseJob.contains("formatrice")) {
			return UserType.FORMATEUR.toString();
		} else if (lowerCaseJob.contains("commercial") || lowerCaseJob.contains("assistant commercial") || lowerCaseJob.contains("commerciale export")) {
			return UserType.COMMERCIAL.toString();
		
		} else if (lowerCaseJob.contains("independant") || lowerCaseJob.contains("independante")) {
			return UserType.INDEPENDANT.toString();
		} else if (lowerCaseJob.contains("apprenti") || lowerCaseJob.contains("technicien support") || lowerCaseJob.contains("concepteur développeur d'applications")
			|| lowerCaseJob.contains("concepteur UI")){
			return UserType.APPRENTI.toString();
		} else if (lowerCaseJob.contains("it") || lowerCaseJob.contains("administrateur systèmes et réseaux") || lowerCaseJob.contains("administrateur systèmes et réseaux, développeur")) {
			return UserType.IT.toString();
		} else if (lowerCaseJob.contains("compt") || lowerCaseJob.contains("comptable")){
			return UserType.COMPTABLE.toString();
		} else if (lowerCaseJob.contains("rh") || lowerCaseJob.contains("assistante rh cfa") || lowerCaseJob.contains("responsable recrutement et formation")
				|| lowerCaseJob.contains("consultant") || lowerCaseJob.contains("assistant administratif recrutement et formation")
				|| lowerCaseJob.contains("assistante emploi formation") || lowerCaseJob.contains("conseillère emploi formation")){
			return UserType.RH.toString();
		} else {
			return UserType.NOT_FOUND.toString();
		}		
	}

	/**
	 * Returns a Response Entity to check whether the password was updated or not.
	 * 
	 * @param reset A Reset Response containing the new password.
	 * 
	 * @exception Exception Returns an exception if the token is expired.
	 */

	@Override
	public boolean resetPassword(ResetResponse reset) throws Exception {
		String hashedPwd = HashTools.hashSHA512(reset.getPassword());
		String email = jwtTokenUtil.getUsernameFromToken(reset.getToken());

		User u = userRepository.findByEmail(email).orElse(null);
		String currentPwd = "";

		if (u != null)
			currentPwd = HashTools.hashSHA512(u.getPassword());

		if (u != null && !currentPwd.equals(hashedPwd)) {

			u.setPassword(hashedPwd);
			userRepository.saveAndFlush(u);

			return true;
		} else if (u != null && currentPwd.equals(hashedPwd)) {
			// same password
			return false;
		} else {
			// if user == null
			return false;
		}
	}

	private String userDG2CompanyToUserCompanyString(String company) {
		if (company == null) {
			company = "";
		}

		if (company.toLowerCase().contains("dawan"))
			return UserCompany.DAWAN.toString();
		else if (company.toLowerCase().contains("jehann"))
			return UserCompany.JEHANN.toString();
		else
			return UserCompany.OTHER.toString();
	}

	private Set<Skill> stringToSkillList(String skills) {
		Set<Skill> skillsSet = new HashSet<>();
		String[] splitedSkills = null;

		if (skills != null) {
			if (skills.length() > 0) {
				if (skills.contains(", ")) {
					splitedSkills = skills.split(", ");
				} else if (skills.contains(" / ")) {
					splitedSkills = skills.split(" / ");

				} else if (skills.contains(" ")) {
					splitedSkills = skills.split(" ");
				} else {
					Optional<Skill> skillInDb = skillRepository.findByTitle(skills);

					if (skillInDb.isPresent()) {
						skillsSet.add(skillInDb.get());
					} else {
						skillsSet.add(new Skill(0, skills, null, 0));
					}
				}

				if (splitedSkills != null) {
					for (String skill : splitedSkills) {
						Optional<Skill> skillInDb = skillRepository.findByTitle(skill);

						if (skillInDb.isPresent()) {
							skillsSet.add(skillInDb.get());
						} else {
							skillsSet.add(new Skill(0, skill, null, 0));
						}
					}
				}
			}
		}
		return skillsSet;
	}

	@Override
	public List<AdvancedUserDto> insertNotAssigned() {
		List<AdvancedUserDto> result = new ArrayList<>();

		locationRepository.findAll().forEach(lo -> {
			try {

				User u = new User();
				u.setIdDg2(-1 * lo.getIdDg2());

				if (!userRepository.findByIdDg2(u.getIdDg2()).isPresent()) {
					u.setLocation(lo);
					u.setEmail("not-assigned-" + lo.getCity() + "@dawan.fr");
					u.setPassword(HashTools.hashSHA512("NoTaSsIgNeDdAwAn!"));
					u.setSkills(null);
					u.setCompany(UserCompany.DAWAN);
					u.setType(UserType.INTERV_NOT_ASSIGN);
					u.setFirstName("NOT ASSIGNED");
					u.setLastName(lo.getCity());
					u.setEmployeeIdDg2(-1 * lo.getIdDg2());

					u = userRepository.saveAndFlush(u);
					result.add(userMapper.userToAdvancedUserDto(u));

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return result;
	}

	@Override
	public HistoricDto fetchUserHistoric(long id, String email, String password) {
		HistoricDto historic = null;
		try {
			historic = new HistoricDto(fetchAnimatedTraning(id, email, password),
					fetchFollowedTraning(id, email, password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return historic;
	}

	@Override
	public List<CourseDG2Dto> fetchAnimatedTraning(long id, String email, String password) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<CourseDG2Dto> courseDG2Dtos = new ArrayList<>();

		URI url = new URI("https://dawan.org/api2/planning/animated-trainings/" + id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + password);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
			String json = repWs.getBody();

			courseDG2Dtos = objectMapper.readValue(json, new TypeReference<List<CourseDG2Dto>>() {
			});
		}
		return courseDG2Dtos;
	}

	@Override
	public List<TrainingDG2Dto> fetchFollowedTraning(long id, String email, String password) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<TrainingDG2Dto> trainingDG2Dtos = new ArrayList<>();

		URI url = new URI("https://dawan.org/api2/planning/trainings-followed/" + id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + password);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
			String json = repWs.getBody();

			trainingDG2Dtos = objectMapper.readValue(json, new TypeReference<List<TrainingDG2Dto>>() {
			});
		}
		return trainingDG2Dtos;
	}
}
