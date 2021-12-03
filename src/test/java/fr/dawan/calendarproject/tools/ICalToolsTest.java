package fr.dawan.calendarproject.tools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

class ICalToolsTest {
	
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		
	}

	@Test
	void shouldCreateCalendar() {
		Calendar calendar = ICalTools.createCalendar("prodId");
		
		assertThat(calendar).isNotNull();
		assertEquals(Version.VERSION_2_0, calendar.getVersion());
		assertEquals(Method.REQUEST, calendar.getMethod());
		assertEquals(CalScale.GREGORIAN, calendar.getCalendarScale());
	}

	@Test
	void testCreateVEvent() {
		/*
		Location mockedLoc = Mockito.mock(Location.class);
		Course mockedCourse = Mockito.mock(Course.class);
		User mockedUser = Mockito.mock(User.class);
		
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timeZone = registry.getTimeZone("Europe/Berlin");
		
		Intervention intervention = new Intervention(1, "I am lambda Intervention", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0), false, null, 0);
		
		VEvent result = ICalTools.createVEvent(intervention, timeZone);
		*/
	}

	@Test
	void shouldFormatLocalDate() {
		LocalDate date = LocalDate.of(2021, 11, 01);

		Date result = ICalTools.formatLocalDate(date);
		
		assertEquals("20211101", result.toString());
	}

	@Test
	void shouldGenerateUID() throws Exception {
		Uid result = ICalTools.generateUID();
		
		assertThat(result).isNotNull();
	}

	@Test
	void shouldGetTimeZone() {		

		VTimeZone result = ICalTools.getTimeZone("America/Denver");
		
		assertThat(result).isNotNull();
		assertEquals("VTIMEZONE", result.getName().toString());
	}

}
