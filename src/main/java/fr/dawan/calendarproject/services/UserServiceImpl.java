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

import fr.dawan.calendarproject.dto.AvancedUserDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.SkillRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository employeeRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Override
	public List<AvancedUserDto> getAllUsers() {
		List<User> employees = employeeRepository.findAll();
		List<AvancedUserDto> result = new ArrayList<AvancedUserDto>();

		for (User e : employees) {
			result.add(DtoTools.convert(e, AvancedUserDto.class));
		}

		return result;
	}

	@Override
	public List<AvancedUserDto> getAllUsers(int page, int max) {
		List<User> employees = employeeRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<AvancedUserDto> result = new ArrayList<AvancedUserDto>();

		for (User e : employees) {
			result.add(DtoTools.convert(e, AvancedUserDto.class));
		}
		return result;
	}

	@Override
	public AvancedUserDto getById(long id) {
		Optional<User> employee = employeeRepository.findById(id);
		return DtoTools.convert(employee.get(), AvancedUserDto.class);
	}

	@Override
	public void deleteById(long id) {
		employeeRepository.deleteById(id);
	}

	@Override
	public AvancedUserDto saveOrUpdate(AvancedUserDto user) {
		User u = DtoTools.convert(user, User.class);
		if (u.getId() != 0) {
			u.setLocation(locationRepository.getOne(u.getLocation().getId()));
			// pb with skills - BETTER TO TRANSFORM WITH ID SKILL and not the whole object skill
			// Or add Version in the Dto
			// Skill version to update
			List<Skill> listSkill = skillRepository.findAll();
			Set<Skill> listSkillToUpdate = u.getSkills();
			for (Skill skill : listSkillToUpdate) {
				for (Skill skillRef : listSkill) {
					if (skill.getId() == skillRef.getId()) {
						int updatedVersion = skillRef.getVersion();
						skill.setVersion(updatedVersion);
					}
				}
			}
			u.setSkills(listSkillToUpdate);
			u.setVersion(employeeRepository.getOne(u.getId()).getVersion());
		}
		u = employeeRepository.saveAndFlush(u);
		return DtoTools.convert(u, AvancedUserDto.class);
	}

	@Override
	public AvancedUserDto findByEmail(String email) {
		User u = employeeRepository.findByEmail(email);
		if (u != null)
			return DtoTools.convert(u, AvancedUserDto.class);

		return null;
	}

	@Override
	public long count() {
		return employeeRepository.count();
	}

}
