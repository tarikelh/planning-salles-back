package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.entities.User;

public interface UserService {

	List<AdvancedUserDto> getAllUsers(int page, int size, String search);
	
	CountDto count(String search);

	List<AdvancedUserDto> getAllUsersByType(String type);

	AdvancedUserDto getById(long id);

	void deleteById(long id);

	AdvancedUserDto saveOrUpdate(AdvancedUserDto employee);

	AdvancedUserDto findByEmail(String email);

	boolean checkIntegrity(AdvancedUserDto u);

	AdvancedUserDto saveOrUpdatePassword(AdvancedUserDto user);
	
	User getEntityById(long id);
	
	Long getLongId(User user);
}
