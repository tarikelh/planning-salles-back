package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;

import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.ICalTools;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class EmailServiceTest {

	@Autowired
	@SpyBean
	private EmailService emailService;
	
	@MockBean
	private InterventionRepository interventionRepository;
	
	@MockBean
	private UserRepository userRepository;
	
	@SpyBean
	private JavaMailSender emailSender;
	
	private User receiver;
	private List<Intervention> iList = new ArrayList<Intervention>();
	
	@BeforeEach
	public void beforeEach() {
		Course mockedCourse = Mockito.mock(Course.class);
		
		Location loc = new Location(1, "Paris", "red", "FR", 0);

		LocalDate date = LocalDate.now();
		
		receiver = new User(1, 1, 1, "Daniel", "Balavoine", loc,
				"dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", date, 0);
		
		iList.add(new Intervention(1, 1, "lambdaSlug", "I am lambda Intervention",
				loc, mockedCourse, receiver, 1, InterventionStatus.SUR_MESURE, true,
				LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0));

		Intervention masterDummy = new Intervention(2, 2, "masterSlug", "I am a master Intervention",
				loc, mockedCourse, receiver, 1, InterventionStatus.INTERN, true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0),
				null, true, 0);
		iList.add(masterDummy);

		Intervention slaveDummy = new Intervention(3, 3, "slaveSlug", "I am a slave Intervention",
				loc, mockedCourse, receiver, 1, InterventionStatus.INTERN, true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0),
				masterDummy, false, 0);
		
		iList.add(slaveDummy);
	}
	
	@Test
	void contextLoads() {
		assertThat(emailService).isNotNull();
	}

	@Test
	void shouldSendCalendarToSelectedEmployees() {
		MockedStatic<ICalTools> calTools = Mockito.mockStatic(ICalTools.class);
		VTimeZone tz = Mockito.mock(VTimeZone.class);
		Calendar cal = new Calendar();
		cal.getProperties().add(new ProdId("TestProdID"));
		cal.getProperties().add(Version.VERSION_2_0);
		VEvent evt = Mockito.mock(VEvent.class);
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(1L);
		
		when(interventionRepository.findByUserIdAndDates(any(Long.class), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(iList);
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(receiver));
		calTools.when(() -> ICalTools.getTimeZone(any(String.class))).thenReturn(tz);
		calTools.when(() -> ICalTools.createCalendar(any(String.class))).thenReturn(cal);
		calTools.when(() -> ICalTools.createVEvent(any(Intervention.class), any(VTimeZone.class), any(String.class)))
			.thenReturn(evt);
		doNothing().when(emailSender).send(any(MimeMessage.class));
		
		assertDoesNotThrow(() -> {
			emailService.sendCalendarToSelectedEmployees(
					userIds,
					Mockito.mock(LocalDate.class),
					Mockito.mock(LocalDate.class));
		});
		
		if(!calTools.isClosed())
			calTools.close();
	}

	@Test
	void shouldCreateAndReturnCalendarMimeMessage() throws Exception {
		Calendar cal = new Calendar();
		String recipient = receiver.getEmail();
		String subject = "TestSubject";
		String content = "TestContent";
		
		cal.getProperties().add(new ProdId("TestProdID"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getComponents().add(Mockito.mock(VEvent.class));
		
		MimeMessage result = assertDoesNotThrow(() -> emailService.setCalendarMessage(
				recipient, subject, content, cal));
		
		assertThat(result).isNotNull();
		assertEquals(subject, result.getSubject());
	}

	@Test
	void shouldCreateAndReturnTextMimeBodyPart() throws IOException, MessagingException {
		String testContent = "This content is for testing purpose";
		String encoding = "utf8";
		
		MimeBodyPart result = assertDoesNotThrow(() -> {
			return emailService.createTextPart(testContent, encoding);
		});

		assertThat(result).isNotNull();
		assertEquals(testContent, result.getContent().toString());
		assertEquals(
				String.format("text/plain; charset=\"%s\"", encoding),
				result.getHeader("Content-Type")[0]);
	}

	@Test
	void shouldCreateAndReturnCalendarMimeBodyPart() throws IOException, MessagingException {
		Calendar cal = new Calendar();
		String filename ="TestFileName";

		cal.getProperties().add(new ProdId("TestProdID"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getComponents().add(Mockito.mock(VEvent.class));
		
		MimeBodyPart result = assertDoesNotThrow(
				() -> emailService.createCalendarBodyPart(cal, filename));
		
		assertThat(result).isNotNull();
		assertEquals("text/calendar; charset=\"utf-8\"; method=REQUEST",
				result.getDataHandler().getContentType());
		assertEquals("quoted-printable",
				result.getHeader("Content-Transfer-Encoding")[0]);
		assertEquals(String.format("attachment; filename=\"%s.ics\"", filename),
				result.getHeader("Content-Disposition")[0]);
	}

	//TODO: Finir email service test
	/*@Test
	void shouldgetAllByEmail() {
	
	}*/

	/*@Test
	void shouldDeleteById() throws Exception {

		doNothing().when(userRepository).deleteById(any(Long.class));

		assertDoesNotThrow(() -> emailService.deleteById(any(Long.class)));
	}*/
	

}
