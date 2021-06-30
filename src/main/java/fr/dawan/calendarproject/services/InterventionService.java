package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import net.fortuna.ical4j.model.Calendar;

public interface InterventionService {
	
	List<InterventionDto> getAllInterventions();

	List<InterventionDto> getAllInterventions(int page, int max);

	InterventionDto getById(long id);

	void deleteById(long id);

	InterventionDto saveOrUpdate(InterventionDto intervention) throws Exception;
	
	List<InterventionDto> getByCourseId(long id);
	
	List<InterventionDto> getByCourseTitle(String title);

	CountDto count(String type);
	
	List<InterventionDto> getMasterIntervention();
	
	List<InterventionDto> getSubInterventions(String type, LocalDate dateStart, LocalDate dateEnd);
	
	//Method created for the test - to delete from here after?
	void getAllIntMementoCSV() throws Exception;
	
	//Method created for the test - to delete from here after?
	void getAllIntMementoCSVDates(LocalDate dateStart, LocalDate dateEnd) throws Exception;

	Calendar exportCalendarAsICal(long userId);

	public List<InterventionDto> getFromUserByDateRange(long userId, LocalDate start, LocalDate end);
	
	public List<InterventionDto> getAllByDateRange(LocalDate start, LocalDate end);

	boolean checkIntegrity(InterventionDto i);
	
}
