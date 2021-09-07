package fr.dawan.calendarproject.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import fr.dawan.calendarproject.entities.Intervention;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.FixedUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;


public class ICalTools {

	public static Calendar createCalendar(String prodId) {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId(prodId));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getProperties().add(Method.REQUEST);
		return calendar;
	}

	public static VEvent createVEvent(Intervention i, VTimeZone tz, String... attendees) {
		Date start = ICalTools.formatLocalDate(i.getDateStart());
		Date end = ICalTools.formatLocalDate(i.getDateEnd().plusDays(1));
		String description = i.getUser().getFullname() + " - " + i.getCourse().getTitle() + " - " + i.getComment();
		String mailTo = "mailto:" + i.getUser().getEmail();
		List<String> attendeesMailTo = new ArrayList<String>();

		attendeesMailTo.add(mailTo);

		for (String a : attendees) {
			attendeesMailTo.add("mailto:" + a);
		}

		VEvent event = new VEvent(start, end, i.getCourse().getTitle());
		event.getProperties().add(new Description(description.toString()));
		event.getProperties().add(new Location(i.getLocation().getCity()));
		event.getProperties().add(tz.getTimeZoneId());

		try {
			event.getProperties().add(generateUID("uidGen"));
		} catch (SocketException e) {
			e.printStackTrace();
		}

		Organizer organizer = new Organizer(URI.create(mailTo));
		event.getProperties().add(organizer);

		for (String email : attendeesMailTo) {
			Attendee attendee = new Attendee(URI.create(email));
			attendee.getParameters().add(Role.REQ_PARTICIPANT);
			attendee.getParameters().add(new Cn(email));
			attendee.getParameters().add(PartStat.NEEDS_ACTION);

			event.getProperties().add(attendee);
		}

		event.getProperties().add(Status.VEVENT_CONFIRMED);
		event.getProperties().add(Transp.TRANSPARENT);

		return event;
	}

	public static Date formatLocalDate(LocalDate date) {
		java.util.Calendar calDate = java.util.Calendar.getInstance();
		calDate.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
		return new Date(calDate.getTime());
	}

	public static Uid generateUID(String pid) throws SocketException {
		UidGenerator ug = new FixedUidGenerator(pid);
		return ug.generateUid();
	}

	public static ByteArrayResource generateICSFile(Calendar calendar, String fileName, File f)
			throws IOException {
		FileOutputStream stream = new FileOutputStream(fileName);
		Path path = Paths.get(f.getAbsolutePath());

		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, stream);

		return new ByteArrayResource(Files.readAllBytes(path));
	}

	public static VTimeZone getTimeZone(String timezone) {
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timeZone = registry.getTimeZone(timezone);
		return timeZone.getVTimeZone();
	}
	
}
