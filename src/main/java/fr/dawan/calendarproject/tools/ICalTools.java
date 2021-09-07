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

import org.springframework.core.io.ByteArrayResource;

import fr.dawan.calendarproject.entities.Intervention;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.UidGenerator;

public class ICalTools {

//	Tool To Generate Event
	public static VEvent createVEvent(Intervention i, VTimeZone tz) {
		Date start = ICalTools.formatLocalDate(i.getDateStart());
		Date end = ICalTools.formatLocalDate(i.getDateEnd().plusDays(1));
		String description = i.getUser().getFullname() + " - " + 
				i.getCourse().getTitle() + " - " + i.getComment();
		
		VEvent event = new VEvent(start, end, i.getCourse().getTitle());
		event.getProperties().add(new Description(description.toString()));
		event.getProperties().add(new Location(i.getLocation().getCity()));
		event.getProperties().add(tz.getTimeZoneId());
		try {
			event.getProperties().add(generateUID());
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Attendee dev1 = new Attendee(URI.create("mailto:rom1.maury@gmail.com"));
		dev1.getParameters().add(Role.REQ_PARTICIPANT);
		dev1.getParameters().add(new Cn("Developer 1"));
		event.getProperties().add(dev1);

		return event;
	}
	
//	Tool To Format Date
	public static Date formatLocalDate(LocalDate date) {
		java.util.Calendar calDate = java.util.Calendar.getInstance();
		calDate.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
		return new Date(calDate.getTime());
	}
	
//	Tool To Generate Uid
	public static Uid generateUID() throws SocketException {
		UidGenerator ug = new UidGenerator("uidGen");
		return ug.generateUid();
	}
	
//	Tool to Generate File
	public static ByteArrayResource generateICSFile(Calendar calendar, String fileName, File f) throws IOException, ValidationException {
		FileOutputStream stream = new FileOutputStream(fileName);
		Path path = Paths.get(f.getAbsolutePath());
		
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, stream);
		
		return new ByteArrayResource(Files.readAllBytes(path));
	}
	
}
