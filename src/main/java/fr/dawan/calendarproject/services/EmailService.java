package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

public interface EmailService {

	void sendCalendarToSelectedEmployees(List<Long> userId, LocalDate dateStart, LocalDate dateEnd) throws Exception;
}

