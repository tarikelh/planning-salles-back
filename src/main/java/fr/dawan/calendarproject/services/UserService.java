package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.HistoricDto;
import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.dto.TrainingDG2Dto;

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

	List<AdvancedUserDto> insertNotAssigned();

	HistoricDto fetchUserHistoric(long id, String email, String password);

	List<CourseDG2Dto> fetchAnimatedTraning(long id, String email, String password) throws Exception;
	List<TrainingDG2Dto> fetchFollowedTraning(long id, String email, String password) throws Exception;
}
