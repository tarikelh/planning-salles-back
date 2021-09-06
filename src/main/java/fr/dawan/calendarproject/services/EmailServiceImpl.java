package fr.dawan.calendarproject.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.InterventionMailingListDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.ICalTools;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private InterventionRepository interventionRepository;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Override
	public void sendCalendarToAttendees(InterventionMailingListDto mailingList) {

		String subject = "Planning Intervention";
		String content = "Planning Intervention : \n";
		String[] recipients = mailingList.getEmails();
		
		VTimeZone tz = ICalTools.getTimeZone("Europe/Berlin");
		
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Google Inc//Google Calendar 70.9054//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getProperties().add(Method.REQUEST);
		
		for (long i : mailingList.getInterventionIds()) {
			Optional<Intervention> interv = interventionRepository.findById(i);
			content += interv.get().toContentString() + "\n";
			calendar.getComponents().add(ICalTools.createVEvent(interv.get(), tz));
		}
		
		try {
			MimeMessage message = setMailingListMessage(recipients, subject, content, calendar);
			emailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendCalendarToTrainer(long userId) {
		
		List<Intervention> interventions = interventionRepository.findByUserId(userId);
		Optional<User> trainer = userRepository.findById(userId);
		
		String subject = "Planning Intervention " + trainer.get().getFullname();
		String content = "Bonjour" + trainer.get().getFullname() +".\n" + "Veuillez trouvez ci-joint le planning complet de vos interventions.";

		VTimeZone tz = ICalTools.getTimeZone("Europe/Berlin");
		
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Google Inc//Google Calendar 70.9054//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getProperties().add(Method.REQUEST);
		
		for (Intervention intervention : interventions) {
			calendar.getComponents().add(ICalTools.createVEvent(intervention, tz));
		}
		
		try {
			MimeMessage message = setCalendarMessage(trainer.get().getEmail(), subject, content, calendar);
			emailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MimeMessage setCalendarMessage(String recipient, String subject, String content, Calendar calendar) throws MessagingException, IOException, ValidationException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		Multipart multipart = new MimeMultipart("mixed");
		Multipart alternative = new MimeMultipart("alternative");
		
		message.addHeaderLine("method=REQUEST");
		message.addHeaderLine("charset=UTF-8");
		message.addHeaderLine("component=VEVENT");
		
		helper.setFrom("noreply@dawan-calendar.fr");
		helper.setTo(recipient);
		helper.setSubject(subject);
		helper.setText(content);
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, bout);
		
		MimeBodyPart event = new MimeBodyPart();
		event.setDataHandler(new DataHandler(new ByteArrayDataSource(
				new ByteArrayInputStream(bout.toByteArray()), "text/calendar; charset=\"utf-8\"; method=REQUEST")));
		event.setHeader("Content-Transfer-Encoding", "quoted-printable");
		alternative.addBodyPart(event);
		
		MimeBodyPart end = new MimeBodyPart();
		end.setText("");
		end.setHeader("Content-Type", "application/ics; name=\"invite.ics\"");
		end.setDisposition("attachment; filename=\"invite.ics\"");
		end.setHeader("Content-Transfer-Encoding", "base64");

		MimeBodyPart mainBody = new MimeBodyPart();
		mainBody.setContent(alternative);
		multipart.addBodyPart(mainBody);
		multipart.addBodyPart(end);
		
		message.setContent(multipart);
		
		return message;
	}

	@Override
	public void sendResetPassword() {
		// TODO Auto-generated method stub
		
	}
	
	private MimeMessage setMailingListMessage(String[] recipients, String subject, String content, Calendar calendar) throws MessagingException, IOException, ValidationException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		Multipart multipart = new MimeMultipart("mixed");
		Multipart alternative = new MimeMultipart("alternative");
		
		message.addHeaderLine("method=REQUEST");
		message.addHeaderLine("charset=UTF-8");
		message.addHeaderLine("component=VEVENT");
		
		helper.setFrom("noreply@dawan-calendar.fr");
		helper.setTo("noreply@dawan-calendar.fr");
		helper.setCc(recipients);
		helper.setSubject(subject);
		helper.setText(content);
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, bout);
		
		MimeBodyPart event = new MimeBodyPart();
		event.setDataHandler(new DataHandler(new ByteArrayDataSource(
				new ByteArrayInputStream(bout.toByteArray()), "text/calendar; charset=\"utf-8\"; method=REQUEST")));
		event.setHeader("Content-Transfer-Encoding", "quoted-printable");
		alternative.addBodyPart(event);
		
		MimeBodyPart end = new MimeBodyPart();
		end.setText("");
		end.setHeader("Content-Type", "application/ics; name=\"invite.ics\"");
		end.setDisposition("attachment; filename=\"invite.ics\"");
		end.setHeader("Content-Transfer-Encoding", "base64");

		MimeBodyPart mainBody = new MimeBodyPart();
		mainBody.setContent(alternative);
		multipart.addBodyPart(mainBody);
		multipart.addBodyPart(end);
		
		message.setContent(multipart);
		
		return message;
	}
}
