package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AvancedUserDto;

public interface UserService {

	List<AvancedUserDto> getAllUsers();

	List<AvancedUserDto> getAllUsers(int page, int max);

	AvancedUserDto getById(long id);

	void deleteById(long id);

	AvancedUserDto saveOrUpdate(AvancedUserDto employee);
	
	AvancedUserDto findByEmail(String email);

	long count();

}
