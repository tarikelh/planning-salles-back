package fr.dawan.calendarproject.services;

import fr.dawan.calendarproject.dto.InterventionMailingListDto;

public interface EmailService {

	void sendCalendarToAttendees(InterventionMailingListDto mailingList);
	void sendCalendarToTrainer(long userId);
	void sendResetPassword();
}

