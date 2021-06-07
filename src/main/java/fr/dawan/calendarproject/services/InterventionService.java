package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.exceptions.InvalidInterventionFormatException;

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
	public void getAllIntMementoCSVDates(LocalDate dateStart, LocalDate dateEnd) throws Exception;

	public List<InterventionDto> getFromUserByDateRange(long userId, LocalDate start, LocalDate end, int page, int size);
	
	public List<InterventionDto> getAllByDateRange(LocalDate start, LocalDate end, int page, int size);
	
	boolean checkIntegrity(Intervention i) throws InvalidInterventionFormatException;
}
