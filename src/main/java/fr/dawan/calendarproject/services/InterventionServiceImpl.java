package fr.dawan.calendarproject.services;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
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
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DateRangeDto;
import fr.dawan.calendarproject.dto.InterventionDG2Dto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
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
	private RestTemplate restTemplate;

	@Override
	public List<InterventionDto> getAllInterventions() {
		List<Intervention> interventions = interventionRepository.findAll();
		List<InterventionDto> interventionsDto = new ArrayList<>();

		for (Intervention intervention : interventions) {
			interventionsDto.add(interventionMapper.interventionToInterventionDto(intervention));
		}

		return interventionsDto;
	}

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

	@Override
	public List<InterventionDto> getAllByUserId(long userId) {
		return interventionMapper.listInterventionToListInterventionDto(interventionRepository.getAllByUserId(userId));
	}

	// NB : method used for mobile application
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

	@Override
	public InterventionDto getById(long id) {
		Optional<Intervention> intervention = interventionRepository.findById(id);
		if (intervention.isPresent())
			return interventionMapper.interventionToInterventionDto(intervention.get());
		return null;
	}

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

	@Override
	public InterventionDto saveOrUpdate(InterventionDto intervention, String email) throws Exception {
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

		interv = interventionRepository.saveAndFlush(interv);

		caretaker.addMemento(email, interv);

		return interventionMapper.interventionToInterventionDto(interv);
	}

	// Search
	@Override
	public List<InterventionDto> getByCourseId(long id) {
		List<Intervention> interventions = interventionRepository.findByCourseId(id);
		List<InterventionDto> iDtos = new ArrayList<>();

		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));

		return iDtos;
	}

	// Search
	@Override
	public List<InterventionDto> getByCourseTitle(String title) {
		List<Intervention> interventions = interventionRepository.findByCourseTitle(title);
		List<InterventionDto> iDtos = new ArrayList<>();
		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));

		return iDtos;
	}

	@Override
	public List<InterventionDto> getFromUserByDateRange(long userId, LocalDate start, LocalDate end) {
		List<Intervention> interventions = interventionRepository.findFromUserByDateRange(userId, start, end);
		List<InterventionDto> iDtos = new ArrayList<>();
		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));
		return iDtos;
	}

	public List<InterventionDto> getAllByDateRange(LocalDate start, LocalDate end) {
		List<Intervention> interventions = interventionRepository.findAllByDateRange(start, end);
		List<InterventionDto> iDtos = new ArrayList<>();
		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));
		return iDtos;
	}

	@Override
	public CountDto count(String type) {
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			return new CountDto(interventionRepository.countByUserTypeNoMaster(userType));
		}

		return null; // Exception
	}

	@Override
	public List<InterventionDto> getMasterIntervention() {
		List<Intervention> interventions = interventionRepository.getMasterIntervention();
		List<InterventionDto> iDtos = new ArrayList<>();

		for (Intervention i : interventions)
			iDtos.add(interventionMapper.interventionToInterventionDto(i));

		return iDtos;
	}

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

	@Override
	public int fetchDG2Interventions(String email, String pwd, LocalDate start, LocalDate end) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<InterventionDG2Dto> lResJson;
		int count = 0;

		URI url = new URI(
				String.format("https://dawan.org/api2/planning/interventions/%s/%s", start.toString(), end.toString()));

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + pwd);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
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

				i.setValidated(i.getAttendeesCount() > 0);

				i.setDateStart(i.getDateStart().substring(0, 10));
				i.setDateEnd(i.getDateEnd().substring(0, 10));
			});

			for (InterventionDG2Dto iDG2 : lResJson) {
				Intervention i = interventionMapper.interventionDG2DtoToIntervention(iDG2);
				i.setCourse(courseRepository.findById(iDG2.getCourseId()).orElse(null));
				i.setLocation(locationRepository.findById(iDG2.getLocationId()).orElse(null));
				i.setUser(userRepository.findById(iDG2.getUserId()).orElse(null));
				i.setMasterIntervention(interventionRepository.findById(iDG2.getMasterInterventionId()).orElse(null));
				
				Optional<Intervention> alreadyInDb = interventionRepository.findById(i.getId());

				if (alreadyInDb.isPresent() && alreadyInDb.get().equalsDG2(i))
					continue;
				else if (alreadyInDb.isPresent() && !alreadyInDb.get().equalsDG2(i)) {
					i.setComment(alreadyInDb.get().getComment());
					i.setTimeStart(alreadyInDb.get().getTimeStart());
					i.setTimeEnd(alreadyInDb.get().getTimeEnd());
					i.setVersion(alreadyInDb.get().getVersion());
				}

				count++;
				try {
					interventionRepository.saveAndFlush(i);
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} else {
			throw new Exception("ResponseEntity from the webservice WDG2 not correct");
		}

		return count;
	}
}
