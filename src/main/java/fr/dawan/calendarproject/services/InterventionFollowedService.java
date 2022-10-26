package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionFollowedDto;

public interface InterventionFollowedService {

	List<InterventionFollowedDto> getAllInterventionsFollowed();

	List<InterventionFollowedDto> getAllInterventionsFollowed(int page, int max);

	CountDto count(String type);

	InterventionFollowedDto getById(long id);

	void deleteById(long id);

	InterventionFollowedDto saveOrUpdate(InterventionFollowedDto interventionsFollowed) throws Exception;
	
	List<InterventionFollowedDto> findAllByUserType(String type);
	
	List<InterventionFollowedDto> findAllByDateRange(LocalDate start, LocalDate end);

	int fetchAllDG2InterventionsFollowed(String email, String password, LocalDate start, LocalDate end) throws Exception;

	List<InterventionFollowedDto> findAllByUserTypeAndDateRange(String type, LocalDate start, LocalDate end);
	
}
