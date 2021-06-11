package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.exceptions.InvalidInterventionFormatException;
import net.fortuna.ical4j.model.Calendar;

public interface InterventionService {
	
	List<InterventionDto> getAllInterventions();

	List<InterventionDto> getAllInterventions(int page, int max);

	InterventionDto getById(long id);

	void deleteById(long id);

	InterventionDto saveOrUpdate(InterventionDto intervention) throws Exception;
	
	List<InterventionDto> getByCourseId(long id);
	
	List<InterventionDto> getByCourseTitle(String title);

	long count();
	
	List<InterventionDto> getMasterIntervention();
	
	List<InterventionDto> getSubInterventions();
	
	//Method created for the test - to delete from here after?
	void getAllIntMementoCSV() throws Exception;
	
	//Method created for the test - to delete from here after?
	void getAllIntMementoCSVDates(LocalDate dateStart, LocalDate dateEnd) throws Exception;

	List<InterventionDto> getFromUserByDateRange(long userId, LocalDate start, LocalDate end, int page, int size);
	
	List<InterventionDto> getAllByDateRange(LocalDate start, LocalDate end, int page, int size);
	
	Calendar exportCalendarAsICal(long userId);
	
	boolean checkIntegrity(InterventionDto i) throws InvalidInterventionFormatException;
}
