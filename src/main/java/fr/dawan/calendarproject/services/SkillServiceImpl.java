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
import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
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

	@Override
	public List<AdvancedSkillDto> getAllSkills() {
		List<Skill> skills = skillRepository.findAll();
		List<AdvancedSkillDto> result = new ArrayList<AdvancedSkillDto>();

		for (Skill s : skills) {
			result.add(DtoTools.convert(s, AdvancedSkillDto.class));
		}

		return result;
	}

	@Override
	public List<AdvancedSkillDto> getAllSkills(int page, int max) {
		List<Skill> skills = skillRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<AdvancedSkillDto> result = new ArrayList<AdvancedSkillDto>();

		for (Skill s : skills) {
			result.add(DtoTools.convert(s, AdvancedSkillDto.class));
		}
		return result;
	}

	@Override
	public AdvancedSkillDto getById(long id) {
		Optional<Skill> skill = skillRepository.findById(id);
		return DtoTools.convert(skill.get(), AdvancedSkillDto.class);
	}

	@Override
	public void deleteById(long id) {
		skillRepository.deleteById(id);
	}

	@Override
	public AdvancedSkillDto saveOrUpdate(AdvancedSkillDto skill) {
		checkIntegrity(skill);
		Skill s = DtoTools.convert(skill, Skill.class);
		
		Set<User> usersList = new HashSet<User>();
		if (skill.getUsersId() != null) {
			skill.getUsersId().forEach(id -> {
				usersList.add(userRepository.getOne(id));
			});
		}
		s.setUsers(usersList);

		if (s.getId() != 0) {
			s.setVersion(skillRepository.getOne(s.getId()).getVersion());
		}
		s = skillRepository.saveAndFlush(s);
		return DtoTools.convert(s, AdvancedSkillDto.class);
	}

	@Override
	public long count() {
		return skillRepository.count();
	}
	
	public boolean checkIntegrity(AdvancedSkillDto s) {
		Set<APIError> errors = new HashSet<APIError>();
		String instanceClass = s.getClass().toString();
		String path = "/api/skills";
		
		for (long userId : s.getUsersId()) {
			if(!userRepository.findById(userId).isPresent()) {
				String message = "User with id: " + userId + " does not exist.";
				errors.add(new APIError(404, instanceClass, "UserNotFound",
						message, path));
			}
		}
		
		return true;
	}
}
