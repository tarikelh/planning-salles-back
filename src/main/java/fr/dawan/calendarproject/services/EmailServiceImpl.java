package fr.dawan.calendarproject.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.interceptors.TokenSaver;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.ICalTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VTimeZone;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InterventionRepository interventionRepository;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${vue.baseurl}")
	private String vueUrl;

	/**
	 * Will send an email to every employees selected. Their intervention will be in
	 * the email body.
	 * 
	 * @param userId    List of user id (long) at who we want to send an email.
	 * @param dateStart date when we want to start to gather intervention.
	 * @param dateEnd   date when we want to stop to gather intervention.
	 */
	@Override
	public void sendCalendarToSelectedEmployees(List<Long> userId, LocalDate dateStart, LocalDate dateEnd) {

		for (long uId : userId) {
			List<Intervention> interventions = interventionRepository.findByUserIdAndDates(uId, dateStart, dateEnd);
			Optional<User> trainer = userRepository.findById(uId);

			if (trainer.isPresent()) {
				String subject = "Planning Intervention " + trainer.get().getFullname();
				String content = "Bonjour " + trainer.get().getFullname() + ".\n"
						+ "Veuillez trouvez ci-joint le planning complet de vos interventions.";

				VTimeZone tz = ICalTools.getTimeZone("Europe/Berlin");

				Calendar calendar = ICalTools.createCalendar("-//Dawan Planning//iCal4j 1.0//FR");

				if (!interventions.isEmpty()) {
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
	}

	/**
	 * Forms the body of the email.
	 * 
	 * @param recipient A String representing the recipient of the email.
	 * @param subject   A String designating the object of the email.
	 * @param content   A String representing the body of the message.
	 * @param calendar  Defines a calendar-specific data to send.
	 * 
	 * @return message Returns a MimeMessage to be sent.
	 */

	public MimeMessage setCalendarMessage(String recipient, String subject, String content, Calendar calendar)
			throws Exception {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		Multipart multipart = new MimeMultipart("mixed");

		message.addHeaderLine("method=REQUEST");
		message.addHeaderLine("charset=UTF-8");
		message.addHeaderLine("component=VEVENT");

		helper.setTo(recipient);
		helper.setSubject(subject);

		multipart.addBodyPart(createTextPart(content, "utf-8"));

		multipart.addBodyPart(createCalendarBodyPart(calendar, "dawan-planning-formation"));

		message.setContent(multipart);

		return message;
	}

	/**
	 * Forms the body of the calendar component.
	 * 
	 * @param calendar Defines a calendar-specific data.
	 * @param filename A String defining the name of the calendar file.
	 * 
	 * @return calendar Returns the body of the calendar to be sent.
	 */

	public MimeBodyPart createCalendarBodyPart(Calendar calendar, String filename) throws Exception {

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

	/**
	 * Forms the text of the email.
	 * 
	 * @param content  A String representing the text of the email.
	 * @param encoding A String defining the type of encoding.
	 * 
	 * @return calendar Returns the body of the text for the email.
	 */

	public MimeBodyPart createTextPart(String content, String encoding) throws MessagingException {
		MimeBodyPart text = new MimeBodyPart();
		text.setText(content);
		text.setHeader("Content-Type", String.format("text/plain; charset=\"%s\"", encoding));

		return text;
	}

	/**
	 * Will send an email containing a link to reset the user's password.
	 * 
	 * @param uDto A User object containing the user's profile
	 * 
	 * @exception Exception Returns an exception if the message is invalid.
	 */

	@Override
	public void sendMailForResetPassword(UserDto uDto) throws Exception {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("name", uDto.getFullName());

		String token = jwtTokenUtil.doGenerateToken(claims, uDto.getEmail());
		TokenSaver.getTokensbyemail().put(uDto.getEmail(), token);

		String resetLink = vueUrl + "/#/fr/reset-password?token=" + token;
		String body = "<HTML><body> <a href=\"" + resetLink + "\">Réinitialiser mon mot de passe</a></body></HTML>";

		MimeMessage msg = emailSender.createMimeMessage();
		msg.addRecipients(Message.RecipientType.TO, uDto.getEmail());
		msg.setSubject("Réinitialisation du mot de passe du Calendrier Dawan");
		msg.setText("Bonjour " + uDto.getLastName()
				+ ". <br /><br />Ce message vous a été envoyé car vous avez oublié votre mot de passe sur l'application"
				+ " Calendrier Dawan. <br />Pour réinitialiser votre mot de passe, veuillez cliquer sur ce lien : "
				+ body, "UTF-8", "html");

		emailSender.send(msg);

	}

}
