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

import fr.dawan.calendarproject.dto.AdvancedUserDto;
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
	UserRepository userRepository;

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
		User u = DtoTools.convert(user, User.class);

		Set<Skill> skillsList = new HashSet<Skill>();
		user.getSkillsId().forEach(id -> {
			skillsList.add(skillRepository.getOne(id));
		});
		u.setSkills(skillsList);

		u.setLocation(locationRepository.getOne(user.getLocationId()));
		if (u.getId() != 0) {
			u.setVersion(userRepository.getOne(u.getId()).getVersion());
		}
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

}
