package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AdvancedUserDto;

public interface UserService {

	List<AdvancedUserDto> getAllUsers();

	List<AdvancedUserDto> getAllUsers(int page, int max);

	AdvancedUserDto getById(long id);

	void deleteById(long id);

	AdvancedUserDto saveOrUpdate(AdvancedUserDto employee);
	
	AdvancedUserDto findByEmail(String email);

	long count();

}
