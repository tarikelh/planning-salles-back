package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionCaretaker;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.CsvToolsGeneric;
import fr.dawan.calendarproject.tools.ICalTools;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;

@Service
@Transactional
public class InterventionServiceImpl implements InterventionService {

	@Autowired
	private InterventionRepository interventionRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InterventionCaretaker caretaker;

	@Autowired
	private InterventionMementoRepository interventionMementoRepository;

	@Override
	public List<InterventionDto> getAllInterventions() {
		List<Intervention> interventions = interventionRepository.findAll();
		List<InterventionDto> interventionsDto = new ArrayList<InterventionDto>();

		for (Intervention intervention : interventions) {
			interventionsDto.add(DtoTools.convert(intervention, InterventionDto.class));
		}
		return interventionsDto;
	}

	@Override
	public List<InterventionDto> getAllInterventions(int page, int max) {
		List<Intervention> interventions = interventionRepository.findAll(PageRequest.of(page, max)).get()
				.collect(Collectors.toList());
		List<InterventionDto> interventionsDto = new ArrayList<InterventionDto>();
		for (Intervention intervention : interventions) {
			interventionsDto.add(DtoTools.convert(intervention, InterventionDto.class));
		}
		return interventionsDto;
	}

	// For InterventionMemento CSV - to move in InterventionMementoServiceImpl ?
	@Override
	public void getAllIntMementoCSV() throws Exception {
		CsvToolsGeneric.toCsv("interventionMemento.csv", caretaker.getAllMemento(), ";");
	}

	// For InterventionMemento CSV between two dates - to move in
	// InterventionMementoServiceImpl ?
	public void getAllIntMementoCSVDates(LocalDate dateStart, LocalDate dateEnd) throws Exception {
		CsvToolsGeneric.toCsv("interventionMementoDates.csv", caretaker.getAllMementoDates(dateStart, dateEnd), ";");
	}

	@Override
	public InterventionDto getById(long id) {
		Optional<Intervention> intervention = interventionRepository.findById(id);
		if (intervention.isPresent())
			return DtoTools.convert(intervention.get(), InterventionDto.class);
		return null;
	}

	@Override
	public void deleteById(long id) {
		interventionRepository.deleteById(id);
	}

	@Override
	public InterventionDto saveOrUpdate(InterventionDto intervention) throws Exception {
		checkIntegrity(intervention);
		Intervention interv = DtoTools.convert(intervention, Intervention.class);

		interv.setLocation(locationRepository.getOne(intervention.getLocationId()));
		interv.setCourse(courseRepository.getOne(intervention.getCourseId()));
		interv.setUser(userRepository.getOne(intervention.getUserId()));

		if (intervention.getMasterInterventionId() > 0)
			interv.setMasterIntervention(interventionRepository.getOne(intervention.getMasterInterventionId()));

		interv = interventionRepository.saveAndFlush(interv);

		// Memento creation
		// Build interventionMemento object
		InterventionMemento intMemento = new InterventionMemento();
		intMemento.setState(DtoTools.convert(interv, InterventionMementoDto.class));
		// Save memento
		try {
			caretaker.addMemento(intervention.getId(), "test", intMemento.createMemento());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		interventionMementoRepository.saveAndFlush(intMemento);
		return DtoTools.convert(interv, InterventionDto.class);
	}

	// Search
	@Override
	public List<InterventionDto> getByCourseId(long id) {
		List<Intervention> interventions = interventionRepository.findByCourseId(id);
		List<InterventionDto> iDtos = new ArrayList<InterventionDto>();

		for (Intervention i : interventions)
			iDtos.add(DtoTools.convert(i, InterventionDto.class));

		return iDtos;
	}

	// Search
	@Override
	public List<InterventionDto> getByCourseTitle(String title) {
		List<Intervention> interventions = interventionRepository.findByCourseTitle(title);
		List<InterventionDto> iDtos = new ArrayList<InterventionDto>();
		for (Intervention i : interventions)
			iDtos.add(DtoTools.convert(i, InterventionDto.class));

		return iDtos;
	}

	@Override
	public List<InterventionDto> getFromUserByDateRange(long userId, LocalDate start, LocalDate end) {
		List<Intervention> interventions = interventionRepository.findFromUserByDateRange(userId, start, end);
		List<InterventionDto> iDtos = new ArrayList<InterventionDto>();
		for (Intervention i : interventions)
			iDtos.add(DtoTools.convert(i, InterventionDto.class));
		return iDtos;
	}
	
	public List<InterventionDto> getAllByDateRange(LocalDate start, LocalDate end) {
		List<Intervention> interventions = interventionRepository.findAllByDateRange(start, end);
		List<InterventionDto> iDtos = new ArrayList<InterventionDto>();
		for (Intervention i : interventions)
			iDtos.add(DtoTools.convert(i, InterventionDto.class));
		return iDtos;
	}

	@Override
	public long count() {
		return interventionRepository.count();
	}

	@Override
	public List<InterventionDto> getMasterIntervention() {
		List<Intervention> interventions = interventionRepository.getMasterIntervention();
		List<InterventionDto> iDtos = new ArrayList<InterventionDto>();
		for (Intervention i : interventions)
			iDtos.add(DtoTools.convert(i, InterventionDto.class));

		return iDtos;
	}

	@Override
	public List<InterventionDto> getSubInterventions(String type, LocalDate dateStart, LocalDate dateEnd) {
		if(UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			List<Intervention> interventions = interventionRepository.getAllChildrenByUserTypeAndDates(userType, dateStart, dateEnd);
			List<InterventionDto> iDtos = new ArrayList<InterventionDto>();
			for (Intervention i : interventions)
				iDtos.add(DtoTools.convert(i, InterventionDto.class));

			return iDtos;
		} else {
			// HANDLE ERROR
			return null;
		}
	}

	public Calendar exportCalendarAsICal(long userId) {

		List<Intervention> lst = interventionRepository.findByUserId(userId) ;
		
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Dawan Calendar//iCal4j 1.0//FR"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		String calName = lst.get(0).getUser().getLastName() + lst.get(0).getUser().getFirstName();
		calendar.getProperties().add(new XProperty("X-CALNAME", calName));
		
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timeZone = registry.getTimeZone("Europe/Berlin");
		VTimeZone tz = timeZone.getVTimeZone();
		
		if (lst != null) {
			for (Intervention intervention : lst) {
				VEvent event = ICalTools.createVEvent(intervention, tz);
				calendar.getComponents().add(event);
			}
		}

		return calendar;
	}

	public boolean checkIntegrity(InterventionDto i) {
		Set<APIError> errors = new HashSet<APIError>();
		String instanceClass = i.getClass().toString();
		String path = "/api/interventions";

		if (i.getDateStart().isAfter(i.getDateEnd()))
			errors.add(
					new APIError(401, instanceClass, "BadDatesSequence", "Start date must be before end date.", path));

		if (i.isMaster() == true && i.getMasterInterventionId() != 0)
			errors.add(new APIError(402, instanceClass, "MasterInterventionLoop",
					"A master intervention cannot has a master intervention.", path));

		if (!InterventionStatus.contains(i.getType())) {
			String message = "Type: " + i.getType().toString() + " is not a valid type.";
			errors.add(new APIError(403, instanceClass, "UnknownInterventionType", message, path));
		}
		
		if (i.isMaster()) {
			if(i.getLocationId() != 0) {
				String message = "Location id should be 0 for a master event.";
				errors.add(new APIError(407, instanceClass, "MasterEventLocation", message, path));
			}
			
			if(i.getCourseId() != 0) {
				String message = "Course id should be 0 for a master event.";
				errors.add(new APIError(407, instanceClass, "MasterEventCourse", message, path));
			}
			
			if(i.getUserId() != 0) {
				String message = "User id should be 0 for a master event.";
				errors.add(new APIError(407, instanceClass, "MasterEventUser", message, path));
			}
		} else {
			if (!locationRepository.findById(i.getLocationId()).isPresent()) {
				String message = "Location with id: " + i.getLocationId() + " does not exist.";
				errors.add(new APIError(404, instanceClass, "LocationNotFound", message, path));
			}
			
			if (!courseRepository.findById(i.getCourseId()).isPresent()) {
				String message = "Course with id: " + i.getCourseId() + " does not exist.";
				errors.add(new APIError(404, instanceClass, "CourseNotFound", message, path));
			}
			
			if (!userRepository.findById(i.getUserId()).isPresent()) {
				String message = "User with id: " + i.getUserId() + " does not exist.";
				errors.add(new APIError(404, instanceClass, "UserNotFound", message, path));
			}		
		}

		
		for (Intervention interv : interventionRepository.findFromUserByDateRange(i.getUserId(), i.getDateStart(), i.getDateEnd())) {
			if (interv.getId() != i.getId()) {
				String message = "Intervention dates overlap the intervention with id: "+ interv.getId() + ".";
				errors.add(new APIError(404, instanceClass, "DateOverlap",
						message, path));
			}
		}

		if (!errors.isEmpty()) {
			throw new EntityFormatException(errors);
		}

		return true;
	}
}
