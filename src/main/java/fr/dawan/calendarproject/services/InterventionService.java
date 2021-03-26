package fr.dawan.calendarproject.services;

import java.util.Date;
import java.util.List;

import fr.dawan.calendarproject.dto.InterventionDto;

public interface InterventionService {
	
	List<InterventionDto> getAllInterventions();

	List<InterventionDto> getAllInterventions(int page, int max);

	InterventionDto getById(long id);

	void deleteById(long id);

	InterventionDto saveOrUpdate(InterventionDto intervention) throws Exception;
	
	List<InterventionDto> getByCourseId(long id);
	
	List<InterventionDto> getByCourseTitle(String title);

	long count();
	
	//Method created for the test - to delete from here after?
	public void getAllIntMementoCSV() throws Exception;
	
	//Method created for the test - to delete from here after?
	public void getAllIntMementoCSVDates(Date dateStart, Date dateEnd) throws Exception;

}
