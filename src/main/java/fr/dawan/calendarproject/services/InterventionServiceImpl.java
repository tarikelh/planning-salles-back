package fr.dawan.calendarproject.services;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.AdvancedInterventionDto2;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DateRangeDto;
import fr.dawan.calendarproject.dto.InterventionDG2Dto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.InterventionMapper;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionCustomRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.ICalTools;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.XProperty;

@Service
@Transactional
public class InterventionServiceImpl implements InterventionService {

	@Autowired
	private InterventionRepository interventionRepository;

	@Autowired
	private InterventionCustomRepository interventionCustomRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InterventionCaretaker caretaker;

	@Autowired
	private InterventionMapper interventionMapper;

	@Autowired
	private LeavePeriodService leavePeriodService;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Fetches all of the existing interventions.
	 * 
	 * @return interventionsDto Returns a list of interventions.
	 *
	 */

	@Override
	public List<InterventionDto> getAllInterventions() {
		List<Intervention> interventions = interventionRepository.findAll();
		List<InterventionDto> interventionsDto = new ArrayList<>();

		for (Intervention intervention : interventions) {
			interventionsDto.add(interventionMapper.interventionToInterventionDto(intervention));
		}

		return interventionsDto;
	}

	/**
	 * Fetches all of the existing interventions, with a pagination system.
	 * 
	 * @param page An integer representing the current page displaying the
	 *             interventions.
	 * @param size An integer representing the last page of the pagination results.
	 * 
	 * @return interventionsDto Returns a list of interventions, according to the
	 *         pagination criteria.
	 *
	 */

	@Override
	public List<InterventionDto> getAllInterventions(int page, int max) {
		List<Intervention> interventions = interventionRepository.findAll(PageRequest.of(page, max)).get()
				.collect(Collectors.toList());
		List<InterventionDto> interventionsDto = new ArrayList<>();
		for (Intervention intervention : interventions) {
			interventionsDto.add(interventionMapper.interventionToInterventionDto(intervention));
		}
		return interventionsDto;
	}

	/**
	 * Fetches all of the existing interventions for a single user.
	 * 
	 * @param userId An unique Integer used to identify each the interventions
	 *               involving a specific user.
	 * 
	 * @return List<InterventionDto> Returns a list of interventions.
	 *
	 */

	@Override
	public List<InterventionDto> getAllByUserId(long userId) {
		return interventionMapper.listInterventionToListInterventionDto(interventionRepository.getAllByUserId(userId));
	}

	// NB : method used for mobile application

	/**
	 * Fetches all of the existing interventions for a specific user.
	 * 
	 * @param userId    An unique Integer used to identify each the interventions
	 *                  involving a specific user.
	 * @param paramsMap
	 * 
	 * @return interventionsDto Returns a list of interventions.
	 *
	 */

	@Override
	public List<InterventionDto> searchBy(long userId, Map<String, String[]> paramsMap) {
		List<InterventionDto> interventionsDto = new ArrayList<>();

		// verify if user exists
		if (userRepository.findById(userId).isPresent()) {
			// search filtered interventions from the user selected
			List<Intervention> interventions = interventionCustomRepository.searchBy(userId, paramsMap);
			for (Intervention intervention : interventions) {
				interventionsDto.add(interventionMapper.interventionToInterventionDto(intervention));
			}
			return interventionsDto;
		}
		return interventionsDto;
	}

	/**
	 * Fetches a single intervention, according to its id.
	 * 
	 * @param id An unique Integer used to identify each intervention.
	 * 
	 * @return InterventionDto Returns a single intervention.
	 *
	 */

	@Override
	public InterventionDto getById(long id) {
		Optional<Intervention> intervention = interventionRepository.findById(id);
		if (intervention.isPresent())
			return interventionMapper.interventionToInterventionDto(intervention.get());
		return null;
	}

	/**
	 * Delete a single intervention, according to its id.
	 * 
	 * @param id An unique Integer used to identify each intervention.
	 *
	 */

	@Override
	public void deleteById(long id, String email) {
		// Memento creation and save
		Optional<Intervention> intToDelete = interventionRepository.findById(id);

		if (intToDelete.isPresent()) {
			interventionRepository.deleteById(id);

			try {
				caretaker.addMemento(email, intToDelete.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds a new intervention or update an existing one.
	 * 
	 * @param intervention An object representing an Intervention.
	 * @param email        A String referring to the email of the current user.
	 * 
	 * @return InterventionDto Returns the newly created Intervention or an updated
	 *         one.
	 *
	 */

	@Override
	public AdvancedInterventionDto2 saveOrUpdate(InterventionDto intervention, String email) throws Exception {
		if (intervention.getId() > 0 && !interventionRepository.existsById(intervention.getId()))
			return null;

		checkIntegrity(intervention);
		Intervention interv = interventionMapper.interventionDtoToIntervention(intervention);

		if (!intervention.isMaster()) {
			interv.setLocation(locationRepository.getOne(intervention.getLocationId()));
			interv.setCourse(courseRepository.getOne(intervention.getCourseId()));
			interv.setUser(userRepository.getOne(intervention.getUserId()));
		} else {
			interv.setLocation(null);
			interv.setCourse(null);
			interv.setUser(null);
		}

		if (intervention.getMasterInterventionId() > 0)
			interv.setMasterIntervention(interventionRepository.getOne(intervention.getMasterInterventionId()));
		else
			interv.setMasterIntervention(null);

		if (interv.getSlug() == null) {
			interv.setSlug(creatInterventionSlug(interv));
		}

		interv = interventionRepository.saveAndFlush(interv);

		caretaker.addMemento(email, interv);

		AdvancedInterventionDto2 result = interventionMapper.interventionToAdvInterventionDto2(interv);
		result.setEventSiblings(
				interventionMapper.listInterventionToListAdvInterventionDto(
						interventionRepository.findSibblings(
								interv.getCourse().getId(), 
								interv.getDateStart(), 
								interv.getDateEnd(), 
								interv.getId(), 
								interv.getUser().getId()
						)
				)
		);
		return result;
	}

	/**
	 * Fetches all of the existing interventions involving a specific course through
	 * its id.
	 * 
	 * @param id An unique Integer used to identify each the interventions involving
	 *           a specific course.
	 * 
	 * @return iDtos Returns a list of interventions.
	 *
	 */

	// Search
	@Override
	public List<InterventionDto> getByCourseId(long id) {
		List<Intervention> interventions = interventionRepository.findByCourseId(id);
		List<InterventionDto> iDtos = new ArrayList<>();

		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));

		return iDtos;
	}

	/**
	 * Fetches all of the existing interventions involving a specific course through
	 * its title.
	 * 
	 * @param title A String referring to a Course's title.
	 * 
	 * @return iDto Returns a list of interventions.
	 *
	 */

	// Search
	@Override
	public List<InterventionDto> getByCourseTitle(String title) {
		List<Intervention> interventions = interventionRepository.findByCourseTitle(title);
		List<InterventionDto> iDtos = new ArrayList<>();
		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));

		return iDtos;
	}

	/**
	 * Fetches all of the existing interventions involving a specific user between
	 * two dates.
	 * 
	 * @param userId An unique Integer used to identify each the interventions
	 *               involving a specific user.
	 * @param start  A LocalDate referring to the starting date.
	 * @param end    A LocalDate referring to the end date.
	 * 
	 * @return iDtos Returns a list of interventions.
	 *
	 */

	@Override
	public List<InterventionDto> getFromUserByDateRange(long userId, LocalDate start, LocalDate end) {
		List<Intervention> interventions = interventionRepository.findFromUserByDateRange(userId, start, end);
		List<InterventionDto> iDtos = new ArrayList<>();
		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));
		return iDtos;
	}

	/**
	 * Fetches all of the existing interventions between two dates.
	 * 
	 * @param start A LocalDate referring to the starting date.
	 * @param end   A LocalDate referring to the end date.
	 * 
	 * @return iDtos Returns a list of interventions.
	 *
	 */

	public List<InterventionDto> getAllByDateRange(LocalDate start, LocalDate end) {
		List<Intervention> interventions = interventionRepository.findAllByDateRange(start, end);
		List<InterventionDto> iDtos = new ArrayList<>();
		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));
		return iDtos;
	}

	/**
	 * Counts the number of interventions involving to a specific type of user.
	 * 
	 * @param type A String referring to a type of user.
	 * 
	 * @return CountDto Returns the number of interventions, according to the search
	 *         criteria.
	 *
	 */

	@Override
	public CountDto count(String type) {
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			return new CountDto(interventionRepository.countByUserTypeNoMaster(userType));
		}

		return null; // Exception
	}

	/**
	 * Fetches all master interventions.
	 * 
	 * @return iDtos Returns a list of all master interventions.
	 *
	 */

	@Override
	public List<InterventionDto> getMasterIntervention() {
		List<Intervention> interventions = interventionRepository.getMasterIntervention();
		List<InterventionDto> iDtos = new ArrayList<>();

		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));

		return iDtos;
	}

	/**
	 * Fetches all sub-interventions, involving a specific type of user and between
	 * two dates.
	 * 
	 * @param type  A String referring to a type of user.
	 * @param start A LocalDate referring to the starting date.
	 * @param end   A LocalDate referring to the end date.
	 * 
	 * @return iDtos Returns a list of sub-interventions.
	 *
	 */

	@Override
	public List<InterventionDto> getSubInterventions(String type, LocalDate dateStart, LocalDate dateEnd) {
		List<InterventionDto> iDtos = new ArrayList<>();
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			List<Intervention> interventions = interventionRepository.getAllChildrenByUserTypeAndDates(userType,
					dateStart, dateEnd);
			for (Intervention i : interventions)
				iDtos.add(interventionMapper.interventionToInterventionDto(i));

			return iDtos;
		} else {
			return iDtos;
		}
	}
	
	
	@Override
	public List<AdvancedInterventionDto2> getAdvSubInterventions(String type, LocalDate dateStart, LocalDate dateEnd) {
		List<AdvancedInterventionDto2> iDtos = new ArrayList<>();
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			List<Intervention> interventions = interventionRepository.getAllChildrenByUserTypeAndDates(userType,
					dateStart, dateEnd);
			for (Intervention i : interventions) {
				
				AdvancedInterventionDto2 result = interventionMapper.interventionToAdvInterventionDto2(i);
				
				List<Intervention> interventionSibllings = interventionRepository.findSibblings(i.getCourse().getId(), i.getDateStart(), i.getDateEnd(), i.getId(), i.getUser().getId());
				
				result.setEventSiblings(interventionMapper.listInterventionToListAdvInterventionDto(interventionSibllings));
				
				iDtos.add(result);
			}
				
			return iDtos;
		} else {
			return iDtos;
		}
	}

	/**
	 * Creates a Calendar component for the user.
	 * 
	 * @param userId An unique Integer used to identify the current user.
	 * 
	 * @return calendar Returns a calendar component.
	 *
	 */

	public Calendar exportCalendarAsICal(long userId) {
		List<Intervention> lst = interventionRepository.findByUserId(userId);

		if (lst == null || lst.isEmpty())
			return null;

		Calendar calendar = ICalTools.createCalendar("-//Dawan Planning//iCal4j 1.0//FR");
		String calName = lst.get(0).getUser().getLastName() + lst.get(0).getUser().getFirstName();
		calendar.getProperties().add(new XProperty("X-CALNAME", calName));
		VTimeZone tz = ICalTools.getTimeZone("Europe/Berlin");

		for (Intervention intervention : lst) {
			VEvent event = ICalTools.createVEvent(intervention, tz);
			calendar.getComponents().add(event);
		}

		return calendar;
	}

	/**
	 * Checks whether a newly registered intervention is valid.
	 * 
	 * @param i An object representing an Intervention.
	 * 
	 * @return boolean Returns a boolean to say whether or not the intervention is
	 *         correct.
	 *
	 */

	public boolean checkIntegrity(InterventionDto i) {
		Set<APIError> errors = new HashSet<>();
		String instanceClass = i.getClass().toString();
		String path = "/api/interventions";

		if (i.getDateStart().isAfter(i.getDateEnd()))
			errors.add(
					new APIError(401, instanceClass, "BadDatesSequence", "Start date must be before end date.", path));

		if (i.isMaster() && i.getMasterInterventionId() != 0)
			errors.add(new APIError(402, instanceClass, "MasterInterventionLoop",
					"A master intervention cannot has a master intervention.", path));

		if (!InterventionStatus.contains(i.getType())) {
			String message = "Type: " + i.getType() + " is not a valid type.";
			errors.add(new APIError(403, instanceClass, "UnknownInterventionType", message, path));
		}

		if (i.isMaster()) {
			if (i.getLocationId() != 0) {
				String message = "Location id should be 0 for a master event.";
				errors.add(new APIError(407, instanceClass, "MasterEventLocation", message, path));
			}

			if (i.getCourseId() != 0) {
				String message = "Course id should be 0 for a master event.";
				errors.add(new APIError(407, instanceClass, "MasterEventCourse", message, path));
			}

			if (i.getUserId() != 0) {
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

		// Verify if an intervention from the same user and not a sibling (course
		// different) is not overlapping with the new intervention
		for (Intervention interv : interventionRepository.findFromUserByDateRange(i.getUserId(), i.getDateStart(),
				i.getDateEnd())) {
			if (interv.getId() != i.getId() && interv.getCourse().getId() != i.getCourseId()) {
				String message = "Intervention dates overlap the intervention with id: " + interv.getId() + ".";
				errors.add(new APIError(404, instanceClass, "DateOverlap", message, path));
			}
		}

		if (!errors.isEmpty()) {
			throw new EntityFormatException(errors);
		}

		return true;
	}

	/**
	 * Fetches interventions following the split of an existing intervention.
	 * 
	 * @param interventionId An unique Integer used to identify an intervention.
	 * @param dates          A List of dates referring to the range of dates
	 *                       involved in the split.
	 * 
	 * @return iListDto Returns a list of interventions following a split.
	 *
	 */

	@Override
	public List<InterventionDto> splitIntervention(long interventionId, List<DateRangeDto> dates) {
		Optional<Intervention> toSplit = interventionRepository.findById(interventionId);
		List<Intervention> iList;
		List<InterventionDto> iListDto = new ArrayList<>();
		Intervention masterIntervention;
		Intervention newSplit;

		if (dates == null || dates.isEmpty())
			return iListDto;

		dates.sort(new Comparator<DateRangeDto>() {
			@Override
			public int compare(DateRangeDto d1, DateRangeDto d2) {
				return d1.getDateStart().compareTo(d2.getDateStart());
			}
		});

		checkDatesIntegrity(dates);

		if (toSplit.isPresent()) {
			iList = new ArrayList<>();

			for (DateRangeDto range : dates) {

				if (range.getInterventionId() == interventionId || range.getInterventionId() == 0)
					newSplit = toSplit.get().clone();
				else
					newSplit = interventionRepository.findById(range.getInterventionId()).orElse(null);

				if (newSplit == null) {
					Set<APIError> err = new HashSet<>();
					err.add(new APIError(404, Intervention.class.toString(), "IdDoesNotExists",
							"Intervention with id " + range.getInterventionId() + " Not found.", "/api/interventions"));

					throw new EntityFormatException(err);
				}

				newSplit.setId(range.getInterventionId());
				newSplit.setSlug(String.format("%s-%s", newSplit.getCourse().getTitle(), range.getDateStart()));
				newSplit.setDateStart(range.getDateStart());
				newSplit.setDateEnd(range.getDateEnd());
				newSplit.setTimeStart(range.getTimeStart());
				newSplit.setTimeEnd(range.getTimeEnd());
				iList.add(newSplit);
			}

			if (toSplit.get().getMasterIntervention() == null) {
				masterIntervention = new Intervention();
				masterIntervention.setMaster(true);
				masterIntervention.setValidated(toSplit.get().isValidated());
				masterIntervention
						.setComment(toSplit.get().getCourse().getTitle() + " - " + iList.size() + " Interventions.");
				masterIntervention.setDateStart(dates.get(0).getDateStart());
				masterIntervention.setDateEnd(dates.get(dates.size() - 1).getDateEnd());
				masterIntervention.setType(toSplit.get().getType());

				masterIntervention = interventionRepository.saveAndFlush(masterIntervention);
			} else {
				masterIntervention = toSplit.get().getMasterIntervention();
			}

			final Intervention finalMasterIntervention = masterIntervention;

			iList.forEach(i -> i.setMasterIntervention(finalMasterIntervention));
			iList = interventionRepository.saveAll(iList);
			interventionRepository.flush();

			iList.add(0, masterIntervention);

			iListDto = new ArrayList<>();

			for (Intervention intervention : iList)
				iListDto.add(interventionMapper.interventionToInterventionDto(intervention));

			return iListDto;
		} else {
			return iListDto;
		}
	}

	/**
	 * Checks whether starting and ending dates aren't reversed and don't overlap.
	 * 
	 * @param dates A list of dates from a defined range.
	 * 
	 * @exception EntityFormatException
	 *
	 */

	private void checkDatesIntegrity(List<DateRangeDto> dates) {
		Set<APIError> errs = new HashSet<>();
		LocalDate dateStart;
		LocalDate dateEnd;
		LocalTime timeStart;
		LocalTime timeEnd;
		String message;

		for (DateRangeDto dateRange : dates) {
			dateStart = dateRange.getDateStart();
			dateEnd = dateRange.getDateEnd();
			timeStart = dateRange.getTimeStart();
			timeEnd = dateRange.getTimeEnd();

			if (dateStart.isAfter(dateEnd)) {
				if (dateRange.getInterventionId() > 0)
					message = "Date Start must be prior to End for Intervention #" + dateRange.getInterventionId();
				else
					message = "Date Start must be prior to End for New Intervention Split";

				errs.add(new APIError(400, dateRange.getClass().toString(), "DateStartIsBeforeEnd", message,
						"/api/interventions"));
			}

			if (timeStart.isAfter(timeEnd)) {
				if (dateRange.getInterventionId() > 0)
					message = "Time Start must be prior to End for Intervention #" + dateRange.getInterventionId();
				else
					message = "Time Start must be prior to End for New Intervention Split";

				errs.add(new APIError(400, dateRange.getClass().toString(), "DateStartIsBeforeEnd", message,
						"/api/interventions"));
			}

			for (DateRangeDto rangeToCheck : dates) {
				if (dateRange != rangeToCheck && dateRange.isOverlapping(rangeToCheck)) {

					errs.add(new APIError(400, dateRange.getClass().toString(), "DatesOvelaps",
							"Intervention Splits Must Not Overlap.", "/api/interventions"));
				}
			}
		}

		if (!errs.isEmpty())
			throw new EntityFormatException(errs);
	}

	/**
	 * Fetches all sub-interventions related on a specific master intervention.
	 * 
	 * @param id An unique Integer used to identify a master intervention.
	 * 
	 * @return iListDto Returns a list of interventions.
	 *
	 */

	@Override
	public List<InterventionDto> getSubByMasterId(long id) {
		Optional<Intervention> master = interventionRepository.findById(id);
		List<Intervention> iList;
		List<InterventionDto> iListDto = new ArrayList<>();

		if (master.isPresent() && master.get().isMaster()) {
			iList = interventionRepository.findByMasterInterventionIdOrderByDateStart(id);

			for (Intervention intervention : iList) {
				iListDto.add(interventionMapper.interventionToInterventionDto(intervention));
			}

			return iListDto;
		}

		return iListDto;
	}

	/**
	 * Counts all interventions in the Dawan API, between two specific dates.
	 * 
	 * @param email A String defining a user's email.
	 * @param pwd   A String defining a user's password.
	 * @param start A LocalDate referring to the starting date of an intervention.
	 * @param end   A LocalDate referring to the end date of intervention.
	 * 
	 * @return count Returns the number of interventions of the Dawan API between
	 *         two dates.
	 * 
	 * @exception Exception Returns an exception if the request fails.
	 *
	 */

	@Override
	public int fetchDG2Interventions(String email, String pwd, LocalDate start, LocalDate end) throws Exception {
		int res = fetchDG2InterventionsOnly(false, email, pwd, start, end);

		leavePeriodService.fetchAllDG2LeavePeriods(email, pwd, start.toString(), end.toString());

		// import options
		int res2 = fetchDG2InterventionsOnly(true, email, pwd, start, end);

		return res + res2;

	}

	public int fetchDG2InterventionsOnly(boolean optionsOnly, String email, String pwd, LocalDate start, LocalDate end)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<InterventionDG2Dto> lResJson;
		List<Intervention> interventionsToSave = new ArrayList<>();
		int count = 0;

		String endPoint = (optionsOnly) ? "options" : "interventions";

		URI url = new URI(String.format("https://dawan.org/api2/planning/" + endPoint + "/%s/%s", start.toString(),
				end.toString()));

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + pwd);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
				String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) { // 527ms

			String json = repWs.getBody();
			InterventionDG2Dto[] resArray = objectMapper.readValue(json, InterventionDG2Dto[].class);

			lResJson = Arrays.asList(resArray);
			Set<Long> masterIds = new HashSet<>();

			lResJson.forEach(i -> masterIds.add(i.getMasterInterventionId()));

			lResJson.forEach(i -> {
				if (masterIds.contains(i.getId()))
					i.setMaster(true);

				String type = i.getType().equals("shared") ? "SUR_MESURE" : "INTERN";
				i.setType(type);

				i.setValidated(i.getAttendeesCount() > 0 && !optionsOnly);

				i.setDateStart(i.getDateStart().substring(0, 10));
				i.setDateEnd(i.getDateEnd().substring(0, 10));
			});

			for (InterventionDG2Dto iDG2 : lResJson) {

				Intervention i = interventionMapper.interventionDG2DtoToIntervention(iDG2);
				Optional<Course> c = courseRepository.findByIdDg2(iDG2.getCourseId());

				if (c.isPresent()) {
					i.setCourse(c.get());
					i.setLocation(locationRepository.findByIdDg2(iDG2.getLocationId()).orElse(null));
					User u = userRepository.findByIdDg2(iDG2.getPersonId()).orElse(null);
					if(u == null)
					{
						try {
							u = userRepository.findByIdDg2(-1 * iDG2.getLocationId()).orElse(null);
						} catch (Exception e) {
						}
					}
						
					i.setUser(u);
					
					i.setMasterIntervention(
							interventionRepository.findByIdDg2(iDG2.getMasterInterventionId()).orElse(null));

					Optional<Intervention> alreadyInDb = interventionRepository.findBySlug(i.getSlug());

					if (alreadyInDb.isPresent() && alreadyInDb.get().equals(i)) {
						i.setId(alreadyInDb.get().getId());
						i.setComment(alreadyInDb.get().getComment());
						i.setTimeStart(alreadyInDb.get().getTimeStart());
						i.setTimeEnd(alreadyInDb.get().getTimeEnd());
						i.setVersion(alreadyInDb.get().getVersion());
					}

					count++;
					try {
						interventionsToSave.add(i);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new Exception("ResponseEntity from the webservice WDG2 not correct");
		}

		try {
			interventionsToSave = interventionRepository.saveAll(interventionsToSave);
			caretaker.saveListMemento(email, interventionsToSave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	private String creatInterventionSlug(Intervention intervention) {
		String slug = null;
		List<Intervention> interventions;

		if (intervention != null) {
			slug = intervention.getCourse().getSlug();
			slug += "-" + intervention.getDateStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			interventions = interventionRepository.findAllContainsSlug(slug);

			if (!interventions.isEmpty()) {
				slug += "-" + (interventions.size() + 1);
			}

		}
		return slug;
	}
}
