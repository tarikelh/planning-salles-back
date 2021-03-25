package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.AvancedSkillDto;
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
	public List<AvancedSkillDto> getAllSkills() {
		List<Skill> skills = skillRepository.findAll();
		List<AvancedSkillDto> result = new ArrayList<AvancedSkillDto>();

		for (Skill s : skills) {
			result.add(DtoTools.convert(s, AvancedSkillDto.class));
		}

		return result;
	}

	@Override
	public List<AvancedSkillDto> getAllSkills(int page, int max) {
		List<Skill> skills = skillRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<AvancedSkillDto> result = new ArrayList<AvancedSkillDto>();

		for (Skill s : skills) {
			result.add(DtoTools.convert(s, AvancedSkillDto.class));
		}
		return result;
	}

	@Override
	public AvancedSkillDto getById(long id) {
		Optional<Skill> skill = skillRepository.findById(id);
		return DtoTools.convert(skill.get(), AvancedSkillDto.class);
	}

	@Override
	public void deleteById(long id) {
		skillRepository.deleteById(id);
	}

	@Override
	public AvancedSkillDto saveOrUpdate(AvancedSkillDto skill) {
		Skill s = DtoTools.convert(skill, Skill.class);
		// pb with users - BETTER TO TRANSFORM WITH ID User and not the whole object user
		//Or add Version in the Dto
		// Skill version to update
		List<User> listUser = userRepository.findAll();
		List<Location> listLoc = locationRepository.findAll();
		Set<User> listUserToUpdate = s.getUsers();
		for (User user : listUserToUpdate) {
			for (User userRef : listUser) {
				if (user.getId() == userRef.getId()) {
					int updatedUserVersion = userRef.getVersion();
					user.setVersion(updatedUserVersion);
				}
			}
			for (Location loc : listLoc) {
				if(user.getLocation().getId() == loc.getId()) {
					int updatedLocVersion = loc.getVersion();
					user.getLocation().setVersion(updatedLocVersion);
				}
			}
		}
		s.setUsers(listUserToUpdate);
		if (s.getId() != 0) {
			s.setVersion(skillRepository.getOne(s.getId()).getVersion());
		}
		s = skillRepository.saveAndFlush(s);
		return DtoTools.convert(s, AvancedSkillDto.class);
	}

	@Override
	public long count() {
		return skillRepository.count();
	}
}
