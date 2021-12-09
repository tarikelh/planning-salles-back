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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.SkillMapper;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.SkillRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	private SkillMapper skillMapper;
	
	/**
	 * Fetches all of the existing skills.
	 * 
	 * @return Returns a list of skills.
	 *
	 */

	@Override
	public List<AdvancedSkillDto> getAllSkills() {
		List<Skill> skills = skillRepository.findAll();

		List<AdvancedSkillDto> result = new ArrayList<>();

		for (Skill s : skills) {
			result.add(skillMapper.skillToAdvancedSkillDto(s));
		}
		return result;
	}
	
	/**
	 * Fetches all of the existing skills, with a pagination system.
	 * 
	 * @param page An integer representing the current page displaying the skills.
	 * @param size An integer defining the number of skills displayed by page.
	 * @param search A String representing the admin's input to search for a specific skill.
	 * 
	 * @return Returns a list of skills, according to the pagination criteria.
	 *
	 */

	@Override
	public List<AdvancedSkillDto> getAllSkills(int page, int size, String search) {
		Pageable pagination = null;

		if (page > -1 && size > 0)
			pagination = PageRequest.of(page, size);
		else
			pagination = Pageable.unpaged();

		List<Skill> skills = skillRepository.findAllByTitleContaining(search, pagination).get()
				.collect(Collectors.toList());
		List<AdvancedSkillDto> result = new ArrayList<>();

		for (Skill s : skills) {
			result.add(skillMapper.skillToAdvancedSkillDto(s));
		}

		return result;
	}
	
	/**
	 * Counts the number of skills.
	 * 
	 * @param search A String representing the admin's input to search for a specific skill.
	 * 
	 * @return Returns the number of skills, according to the search criteria.
	 *
	 */

	@Override
	public CountDto count(String search) {
		return new CountDto(skillRepository.countByTitleContaining(search));
	}
	
	/**
	 * Fetches a single skill, according to its id.
	 * 
	 * @param id An unique Integer used to identify each skill.
	 * 
	 * @return Returns a single skill.
	 *
	 */

	@Override
	public AdvancedSkillDto getById(long id) {
		Optional<Skill> skill = skillRepository.findById(id);

		if (skill.isPresent())
			return skillMapper.skillToAdvancedSkillDto(skill.get());

		return null;
	}
	
	/**
	 * Delete a single skill, according to its id.
	 * 
	 * @param id An unique Integer used to identify each user.
	 *
	 */

	@Override
	public void deleteById(long id) {
		skillRepository.deleteById(id);
	}
	
	/**
	 * Adds a new skill or updates an existing one.
	 * 
	 * @param skill An object representing an Skill.
	 * 
	 * @return Returns the newly created skill or an updated one.
	 *
	 */

	@Override
	public AdvancedSkillDto saveOrUpdate(AdvancedSkillDto skill) {
		checkIntegrity(skill);

		if (skill.getId() > 0 && !skillRepository.findById(skill.getId()).isPresent())
			return null;

		Skill s = skillMapper.advancedSkillDtoToSkill(skill);

		Set<User> usersList = new HashSet<>();
		if (skill.getUsersId() != null) {
			skill.getUsersId().forEach(id -> usersList.add(userRepository.getOne(id)));
		}
		s.setUsers(usersList);

		s = skillRepository.saveAndFlush(s);

		return skillMapper.skillToAdvancedSkillDto(s);
	}
	
	/**
	 * Checks whether a newly registered skill is valid.
	 * 
	 * @param s An object representing an Skill.
	 * 
	 * @return boolean Returns a boolean to say whether or not the skill is correct.
	 *
	 */

	public boolean checkIntegrity(AdvancedSkillDto s) {
		Set<APIError> errors = new HashSet<>();
		String instanceClass = s.getClass().toString();
		String path = "/api/skills";

		if (s.getUsersId() != null) {
			for (long userId : s.getUsersId()) {
				if (!userRepository.findById(userId).isPresent()) {
					String message = "User with id: " + userId + " does not exist.";
					errors.add(new APIError(404, instanceClass, "UserNotFound", message, path));
				}
			}
		}

		if (!errors.isEmpty()) {
			throw new EntityFormatException(errors);
		}

		return true;
	}
}
