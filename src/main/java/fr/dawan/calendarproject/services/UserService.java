package fr.dawan.calendarproject.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.ResetResponse;

public interface UserService {

	List<AdvancedUserDto> getAllUsers();

	List<AdvancedUserDto> getAllUsers(int page, int size, String search);

	CountDto count(String search);

	List<AdvancedUserDto> getAllUsersByType(String type);

	AdvancedUserDto getById(long id);

	void deleteById(long id);

	AdvancedUserDto saveOrUpdate(AdvancedUserDto employee);

	AdvancedUserDto findByEmail(String email);

	boolean checkIntegrity(AdvancedUserDto u);

	void fetchAllDG2Users(String email, String password) throws Exception;
	
	boolean resetPassword(ResetResponse reset) throws Exception;

}
