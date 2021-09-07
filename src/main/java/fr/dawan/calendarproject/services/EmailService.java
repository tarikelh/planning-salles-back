package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.InterventionMailingListDto;

public interface EmailService {

	void sendCalendarToAttendees(InterventionMailingListDto mailingList);
	void sendCalendarToTrainer(long userId);
	void sendCalendarToSelectedEmployees(List<Long> userId, LocalDate dateStart, LocalDate dateEnd) throws Exception;
}

