package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.InterventionDto;

public interface InterventionService {
	
	List<InterventionDto> getAllInterventions();

	List<InterventionDto> getAllInterventions(int page, int max);

	InterventionDto getById(long id);

	void deleteById(long id);

	InterventionDto saveOrUpdate(InterventionDto intervention);
	
	List<InterventionDto> getByCourseId(long id);
	
	List<InterventionDto> getByCourseTitle(String title);

	long count();

}
