package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.AvancedUserDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository employeeRepository;

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
		u = employeeRepository.saveAndFlush(u);
		return DtoTools.convert(u, AvancedUserDto.class);
	}

	@Override
	public long count() {
		return employeeRepository.count();
	}

}
