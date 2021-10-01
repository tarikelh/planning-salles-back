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

	@Override
	public List<AdvancedSkillDto> getAllSkills() {
		Set<Skill> skills = skillMapper.listSkillToSetSkill(skillRepository.findAll());
		List<AdvancedSkillDto> result = new ArrayList<AdvancedSkillDto>();

		for (Skill s : skills) {
			result.add(skillMapper.skillToAdvancedSkillDto(s));
		}
		return result;
	}

	@Override
	public List<AdvancedSkillDto> getAllSkills(int page, int size, String search) {
		Pageable pagination = null;

		if (page != -1 & size != -1)
			pagination = PageRequest.of(page, size);
		else
			pagination = Pageable.unpaged();

		List<Skill> skills = skillRepository.findAllByTitleContaining(search, pagination).get()
				.collect(Collectors.toList());
		List<AdvancedSkillDto> result = new ArrayList<AdvancedSkillDto>();

		for (Skill s : skills) {
			result.add(skillMapper.skillToAdvancedSkillDto(s));
		}
		return result;
	}

	@Override
	public CountDto count(String search) {
		return new CountDto(skillRepository.countByTitleContaining(search));
	}

	@Override
	public AdvancedSkillDto getById(long id) {
		Optional<Skill> skill = skillRepository.findById(id);
		return skillMapper.skillToAdvancedSkillDto(skill.get());
	}

	@Override
	public void deleteById(long id) {
		skillRepository.deleteById(id);
	}

	@Override
	public AdvancedSkillDto saveOrUpdate(AdvancedSkillDto skill) {
		checkIntegrity(skill);

		if (skill.getId() > 0 && !skillRepository.findById(skill.getId()).isPresent())
			return null;

		Skill s = skillMapper.AdvancedSkillDtoToSkill(skill);

		Set<User> usersList = new HashSet<User>();
		if (skill.getUsersId() != null) {
			skill.getUsersId().forEach(id -> {
				usersList.add(userRepository.getOne(id));
			});
		}
		s.setUsers(usersList);

		s = skillRepository.saveAndFlush(s);
		return skillMapper.skillToAdvancedSkillDto(s);
	}

	public boolean checkIntegrity(AdvancedSkillDto s) {
		Set<APIError> errors = new HashSet<APIError>();
		String instanceClass = s.getClass().toString();
		String path = "/api/skills";

		for (long userId : s.getUsersId()) {
			if (!userRepository.findById(userId).isPresent()) {
				String message = "User with id: " + userId + " does not exist.";
				errors.add(new APIError(404, instanceClass, "UserNotFound", message, path));
			}
		}

		if (!errors.isEmpty()) {
			throw new EntityFormatException(errors);
		}

		return true;
	}
}
