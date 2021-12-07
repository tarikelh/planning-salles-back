package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DateRangeDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.mapper.DoIgnore;
import net.fortuna.ical4j.model.Calendar;

public interface InterventionService {

	@DoIgnore
	List<InterventionDto> getAllInterventions();

	@DoIgnore
	List<InterventionDto> getAllInterventions(int page, int max);

	List<InterventionDto> getAllByUserId(long userId);

	List<InterventionDto> searchBy(long userId, Map<String, String[]> paramsMap);

	InterventionDto getById(long id);

	void deleteById(long id, String email);

	InterventionDto saveOrUpdate(InterventionDto intervention, String email) throws Exception;

	List<InterventionDto> getByCourseId(long id);

	List<InterventionDto> getByCourseTitle(String title);

	CountDto count(String type);

	@DoIgnore
	List<InterventionDto> getMasterIntervention();

	List<InterventionDto> getSubInterventions(String type, LocalDate dateStart, LocalDate dateEnd);

	Calendar exportCalendarAsICal(long userId);

	List<InterventionDto> getFromUserByDateRange(long userId, LocalDate start, LocalDate end);

	List<InterventionDto> getAllByDateRange(LocalDate start, LocalDate end);

	boolean checkIntegrity(InterventionDto i);

	List<InterventionDto> splitIntervention(long interventionId, List<DateRangeDto> dates);

	List<InterventionDto> getSubByMasterId(long id);

	int fetchDG2Interventions(String email, String pwd, LocalDate start, LocalDate end) throws Exception;
}
