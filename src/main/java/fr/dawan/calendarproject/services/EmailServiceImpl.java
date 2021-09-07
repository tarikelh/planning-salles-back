package fr.dawan.calendarproject.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
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
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
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

		Calendar calendar = ICalTools.createCalendar("-//Google Inc//Google Calendar 70.9054//EN");

		for (long i : mailingList.getInterventionIds()) {
			Optional<Intervention> interv = interventionRepository.findById(i);
			content += interv.get().toContentString() + "\n";
			calendar.getComponents().add(ICalTools.createVEvent(interv.get(), tz, recipients));
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
		String content = "Bonjour" + trainer.get().getFullname() + ".\n"
				+ "Veuillez trouvez ci-joint le planning complet de vos interventions.";

		VTimeZone tz = ICalTools.getTimeZone("Europe/Berlin");

		Calendar calendar = ICalTools.createCalendar("-//Google Inc//Google Calendar 70.9054//EN");

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

	
	@Override
	public void sendCalendarToSelectedEmployees(List<Long> userId, LocalDate dateStart, LocalDate dateEnd) throws Exception {
		// add asyncrhone method
		
		for (long uId : userId) {
			List<Intervention> interventions = interventionRepository.findByUserIdAndDates(uId, dateStart, dateEnd);
			Optional<User> trainer = userRepository.findById(uId);
			
			String subject = "Planning Intervention " + trainer.get().getFullname();
			String content = "Bonjour" + trainer.get().getFullname() +".\n" + "Veuillez trouvez ci-joint le planning complet de vos interventions.";

			VTimeZone tz = ICalTools.getTimeZone("Europe/Berlin");
			
			Calendar calendar = ICalTools.createCalendar("-//Google Inc//Google Calendar 70.9054//EN");
			
			/*
			calendar.getProperties().add(new Organizer());
			calendar.getProperties().getProperty(Property.ORGANIZER).setValue("MAILTO:valentin.dawan@gmail.com");
			calendar.getProperties().getProperty(Property.ORGANIZER).getParameters().add(new Cn("Val val"));
			*/
			
			/*
			Organizer organizer = new Organizer(URI.create("mailto:" + trainer.get().getEmail()));
			organizer.getParameters().add(new Cn(trainer.get().getFullname()));
			calendar.getProperties().add(organizer);
			*/
			
			if(interventions.size() > 0) {
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
			
		}
		
	}

	private MimeMessage setCalendarMessage(String recipient, String subject, String content, Calendar calendar)
			throws MessagingException, IOException, ValidationException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		Multipart multipart = new MimeMultipart("mixed");

		message.addHeaderLine("method=REQUEST");
		message.addHeaderLine("charset=UTF-8");
		message.addHeaderLine("component=VEVENT");

		helper.setFrom("noreply@dawan-calendar.fr");
		helper.setTo(recipient);
		helper.setSubject(subject);

		multipart.addBodyPart(createTextPart(content, "utf-8"));

		multipart.addBodyPart(createCalendarBodyPart(calendar, "dawan-planning-formation"));

		message.setContent(multipart);

		return message;
	}


	private MimeMessage setMailingListMessage(String[] recipients, String subject, String content, Calendar calendar)
			throws MessagingException, IOException, ValidationException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		Multipart multipart = new MimeMultipart("mixed");

		message.addHeaderLine("method=REQUEST");
		message.addHeaderLine("charset=UTF-8");
		message.addHeaderLine("component=VEVENT");

		helper.setFrom("noreply@dawan-calendar.fr");
		helper.setTo("noreply@dawan-calendar.fr");
		helper.setCc(recipients);
		helper.setSubject(subject);

		multipart.addBodyPart(createTextPart(content, "utf-8"));

		multipart.addBodyPart(createCalendarBodyPart(calendar, "dawan-planning-formation"));

		message.setContent(multipart);

		return message;
	}

	private MimeBodyPart createCalendarBodyPart(Calendar calendar, String filename)
			throws IOException, ValidationException, MessagingException {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, bout);

		MimeBodyPart calendarPart = new MimeBodyPart();
		DataHandler data = new DataHandler(new ByteArrayDataSource(new ByteArrayInputStream(bout.toByteArray()),
				"text/calendar; charset=\"utf-8\"; method=REQUEST"));
		calendarPart.setDataHandler(data);
		calendarPart.setHeader("Content-Transfer-Encoding", "quoted-printable");
		calendarPart.setDisposition(String.format("attachment; filename=\"%s.ics\"", filename));

		return calendarPart;
	}

	private MimeBodyPart createTextPart(String content, String encoding) throws MessagingException {
		MimeBodyPart text = new MimeBodyPart();
		text.setText(content);
		text.setHeader("Content-Type", String.format("text/plain; charset=\"%s\"", encoding));

		return text;
	}

}
