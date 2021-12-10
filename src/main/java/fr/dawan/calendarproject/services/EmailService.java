package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.springframework.http.ResponseEntity;

import fr.dawan.calendarproject.dto.ResetResponse;
import fr.dawan.calendarproject.dto.UserDto;
import net.fortuna.ical4j.model.Calendar;

public interface EmailService {

	void sendCalendarToSelectedEmployees(List<Long> userId, LocalDate dateStart, LocalDate dateEnd) throws Exception;
	
	MimeMessage setCalendarMessage(String recipient, String subject, String content, Calendar calendar) throws Exception;
	
	MimeBodyPart createTextPart(String content, String encoding) throws MessagingException;
	
	MimeBodyPart createCalendarBodyPart(Calendar calendar, String filename) throws Exception;

	void sendMailForResetPassword(UserDto uDto) throws Exception;

}
