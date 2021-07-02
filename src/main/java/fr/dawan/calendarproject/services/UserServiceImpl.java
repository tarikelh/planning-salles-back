package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.SkillRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Override
	public List<AdvancedUserDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<AdvancedUserDto> result = new ArrayList<AdvancedUserDto>();

		for (User u : users) {
			result.add(DtoTools.convert(u, AdvancedUserDto.class));
		}

		return result;
	}
	
	@Override
	public List<AdvancedUserDto> getAllUsersByType(String type) {
		if (UserType.contains(type)){
			UserType userType = UserType.valueOf(type);
			List<User> users = userRepository.findAllByType(userType);
			List<AdvancedUserDto> result = new ArrayList<AdvancedUserDto>();
			for (User u : users) {
				result.add(DtoTools.convert(u, AdvancedUserDto.class));
			}
			return result;
		} else {
			// HANDLE ERROR
			return null;
		}
	}

	@Override
	public List<AdvancedUserDto> getAllUsers(int page, int max) {
		List<User> users = userRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<AdvancedUserDto> result = new ArrayList<AdvancedUserDto>();

		for (User u : users) {
			result.add(DtoTools.convert(u, AdvancedUserDto.class));
		}
		return result;
	}

	@Override
	public AdvancedUserDto getById(long id) {
		Optional<User> user = userRepository.findById(id);
		return DtoTools.convert(user.get(), AdvancedUserDto.class);
	}

	@Override
	public void deleteById(long id) {
		userRepository.deleteById(id);
	}

	@Override
	public AdvancedUserDto saveOrUpdate(AdvancedUserDto user) {
		checkIntegrity(user);
		
		User u = DtoTools.convert(user, User.class);

		Set<Skill> skillsList = new HashSet<Skill>();

		if (user.getSkillsId() != null) {
			user.getSkillsId().forEach(id -> {
				skillsList.add(skillRepository.getOne(id));
			});
		}
		u.setSkills(skillsList);

		u.setLocation(locationRepository.getOne(user.getLocationId()));

		u = userRepository.saveAndFlush(u);
		return DtoTools.convert(u, AdvancedUserDto.class);
	}

	@Override
	public AdvancedUserDto findByEmail(String email) {
		User u = userRepository.findByEmail(email);
		if (u != null)
			return DtoTools.convert(u, AdvancedUserDto.class);

		return null;
	}

	@Override
	public long count() {
		return userRepository.count();
	}
	
	public boolean checkIntegrity(AdvancedUserDto u) {
		Set<APIError> errors = new HashSet<APIError>();
		String instanceClass = u.getClass().toString();
		String path = "/api/users";
		//Location Must EXIST
		if (!locationRepository.findById(u.getLocationId()).isPresent()) {
			String message = "Location with id: " + u.getLocationId() + " does not exist.";
			errors.add(new APIError(301, instanceClass, "LocationNotFound",
					message, path));
		}
		
		//IF Skill > Must EXIST
		if (u.getSkillsId() != null) {
			for (long skillId : u.getSkillsId()) {
				if(!skillRepository.findById(skillId).isPresent()) {
					String message = "Skill with id: " + skillId + " does not exist.";
					errors.add(new APIError(302, instanceClass, "SkillNotFound",
							message, path));
				}
			}
		}
		
		//Email > valid, uniq
		if (!User.emailIsValid(u.getEmail())) {
			String message = "Email must be valid.";
			errors.add(new APIError(303, instanceClass, "InvalidEmail",
					message, path));
		}
		
		if(userRepository.findByEmail(u.getEmail()) != null) {
			String message = "Email already used.";
			errors.add(new APIError(304, instanceClass, "EmailNotUniq",
					message, path));
		}
		
		//password > 8 char min
		if(u.getPassword().length() < 8) {
			String message = "Password must be at least 8 characters long";
			errors.add(new APIError(305, instanceClass, "PasswordTooShort",
					message, path));
		}
		
		// Company + type enums must be valid
		if (!UserCompany.contains(u.getCompany())) {
			String message = "Company: " + u.getCompany() + " is not valid.";
			errors.add(new APIError(306, instanceClass, "UnknownUserCompany",
					message, path));
		}
		
		if (!UserType.contains(u.getType())) {
			String message = "Type: " + u.getType() + " is not valid.";
			errors.add(new APIError(307, instanceClass, "UnknownUserType",
					message, path));
		}
		//If Image > must exist
		if (!errors.isEmpty()) {
			throw new EntityFormatException(errors);
		}

		return true;
	}
}
