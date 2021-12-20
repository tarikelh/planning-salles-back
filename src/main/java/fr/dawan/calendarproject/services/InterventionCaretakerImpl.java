package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.mapper.InterventionMapper;
import fr.dawan.calendarproject.mapper.InterventionMementoMapper;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.CompareGeneric;
import fr.dawan.calendarproject.tools.CsvToolsGeneric;

@Component
public class InterventionCaretakerImpl implements InterventionCaretaker {

	@Autowired
	private InterventionService interventionService;

	@Autowired
	private InterventionMementoRepository intMementoRepository;

	@Autowired
	private InterventionRepository interventionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private InterventionMapper interventionMapper;

	@Autowired
	private InterventionMementoMapper interventionMementoMapper;

	@Value("${app.storagefolder}")
	private String storageFolder;

	/**
	 * Create an InterventionMemento, which is an intervention history.
	 * 
	 * @param email        from the user applying modification on an intervention
	 * @param intervention object modified by the user (added, edited or deleted)
	 * 
	 * @return InterventionMemento intervention history created
	 * 
	 * @throws Exception handle exception 'IllegalAccessException' when cannot get
	 *                   value of fields in compareObjects method
	 */
	@Override
	public InterventionMemento addMemento(String email, Intervention intervention) throws Exception {
		String messageAction = "";
		String modificationsDone = "";
		InterventionMemento memento = new InterventionMemento();
		InterventionMementoDto state = interventionMementoMapper.interventionToInterventionMementoDto(intervention);
		// copy of the intervention
		memento.setState(state);

		// check if mementos with this intervention already exist in database
		if (intMementoRepository.countByInterventionId(memento.getState().getInterventionId()) != 0) {
			// check if intervention still exists or not in database
			if (interventionRepository.existsById(memento.getState().getInterventionId())) {
				messageAction = " has been changed by ";

				// Obtain difference between two interventions
				InterventionMemento mementoBefore = intMementoRepository
						.getLastInterventionMemento(memento.getState().getInterventionId());
				modificationsDone = CompareGeneric.compareObjects(mementoBefore.getState(), memento.getState());
			} else {
				messageAction = " has been deleted by ";
				modificationsDone = "";
			}
		} else {
			messageAction = " has been created by ";
			modificationsDone = "";
		}

		memento.setMementoMessage(
				new MementoMessageDto(memento.getState().getInterventionId(), messageAction, email, modificationsDone));

		return intMementoRepository.saveAndFlush(memento);
	}
	
	/**
	 * Create historic for a list of intervention
	 * 
	 * @param List<Intervention> that we want to create a historic for each element in the list
	 * @param email		from the user applying modification on interventions
	 * 
	 * @return List<InterventionMemento> historic created
	 * 
	 * @throws Exception can be throw by the method addMemento
	 */
	@Override
	public List<InterventionMemento> saveListMemento(String email, List<Intervention> intervention) throws Exception {		
		List<InterventionMemento> listMemento = new ArrayList<>();
		for (Intervention i : intervention) {
			listMemento.add(addMemento(email, i));
		}
		return listMemento;
	}

	/**
	 * Restore an Intervention from an intervention history (InterventionMemento).
	 * 
	 * @param mementoId id of the intervention history we want to restore
	 * @param email     from the user applying modification on an intervention
	 * 
	 * @return InterventionDto intervention restored
	 * 
	 * @throws CloneNotSupportedException handle exception when cloning
	 *                                    InterventionMemento
	 */
	@Override
	public InterventionDto restoreMemento(long mementoId, String email) throws CloneNotSupportedException {
		InterventionMemento iMem = intMementoRepository.findById(mementoId).orElse(null);
		Intervention intToRestore = new Intervention();
		InterventionMemento newIMem = new InterventionMemento();

		if (iMem != null) {
			intToRestore = interventionMementoMapper.interventionMementoDtoToIntervention(iMem.getState());
			newIMem = (InterventionMemento) iMem.clone();

			intToRestore.setCourse(courseRepository.findById(iMem.getState().getCourseId()).orElse(null));
			intToRestore.setLocation(locationRepository.findById(iMem.getState().getLocationId()).orElse(null));
			intToRestore.setUser(userRepository.findById(iMem.getState().getUserId()).orElse(null));

			if (iMem.getState().getMasterInterventionId() > 0)
				intToRestore.setMasterIntervention(
						interventionRepository.findById(iMem.getState().getMasterInterventionId()).orElse(null));
			else
				intToRestore.setMasterIntervention(null);
		}

		interventionService.checkIntegrity(interventionMapper.interventionToInterventionDto(intToRestore));

		//Check only when update and not delete
		if(interventionRepository.existsById(intToRestore.getId()))
			intToRestore.setVersion(interventionRepository.getOne(intToRestore.getId()).getVersion());
		
		interventionRepository.saveAndFlush(intToRestore);

		newIMem.setId(0);
		newIMem.setDateCreatedState(LocalDateTime.now());
		newIMem.setMementoMessage(
				new MementoMessageDto(newIMem.getState().getInterventionId(), " Has been restored ", email, ""));

		intMementoRepository.saveAndFlush(newIMem);

		return interventionMapper.interventionToInterventionDto(intToRestore);
	}

	/**
	 * Get the intervention history (InterventionMemento) with its Id.
	 * 
	 * @param id intervention history (InterventionMemento) Id
	 * 
	 * @return InterventionMemento called by the user
	 */
	@Override
	public InterventionMemento getMementoById(long id) {
		Optional<InterventionMemento> i = intMementoRepository.findById(id);

		if (i.isPresent()) {
			InterventionMementoDto iMemDto = i.get().getState();

			if (iMemDto != null) {
				if (iMemDto.getCourseId() > 0) {
					Course course = courseRepository.findById(iMemDto.getCourseId()).orElse(null);
					if (course != null)
						iMemDto.setCourseTitle(course.getTitle());
				}

				if (iMemDto.getUserId() > 0) {
					User u = userRepository.findById(iMemDto.getUserId()).orElse(null);
					if (u != null)
						iMemDto.setUserFullName(u.getFullname());
				}

				if (iMemDto.getLocationId() > 0) {

					Location location = locationRepository.findById(iMemDto.getLocationId()).orElse(null);
					if (location != null)
						iMemDto.setLocationCity(location.getCity());
				}
				return i.get();
			}
		}
		return null;
	}

	/**
	 * Get list of historic saved in the database
	 * 
	 * @return List<InterventionMemento> list of memento (historic)
	 */
	@Override
	public List<InterventionMemento> getAllMemento() {
		return intMementoRepository.findAll();
	}

	/**
	 * Get list of memento (historic) saved in the database depending of the page and size selected
	 * 
	 * @param page An integer representing the current page displaying memento.
	 * @param size An integer defining the number of memento displayed by page.
	 * 
	 * @return List<InterventionMemento> list of memento (historic)
	 */
	@Override
	public List<InterventionMemento> getAllMemento(int page, int size) {
		return intMementoRepository.findAllByOrderByIdDesc(PageRequest.of(page, size));
	}

	/**
	 * Get list of memento (historic) between the date start and the date end selected
	 * 
	 * @param dateStart LocalDate to know when to start to retrieve memento.
	 * @param dateEnd LocalDate to know when to finish to retrieve memento.
	 * 
	 * @return List<InterventionMemento> list of memento (historic)
	 */
	@Override
	public List<InterventionMemento> getAllMementoDates(LocalDate dateStart, LocalDate dateEnd) {
		return intMementoRepository.findAllByDateCreatedStateBetween(dateStart.atStartOfDay(),
				dateEnd.plusDays(1).atStartOfDay());
	}

	/**
	 * Create a csv file with the list of memento (historic) saved in the database
	 */
	public void serializeInterventionMementosAsCSV() throws Exception {
		String fileName = "interventionMementoDates.csv";
		String path = storageFolder + "/" + fileName;
		CsvToolsGeneric.toCsv(path, getAllMemento(), ";");
	}

	/**
	 * Create a csv file with the list of memento (historic) between the date start and the date end selected
	 * 
	 * @param dateStart LocalDate to know when to start to retrieve memento.
	 * @param dateEnd LocalDate to know when to finish to retrieve memento.
	 * 
	 * @return List<InterventionMemento> list of memento (historic)
	 */
	public void serializeInterventionMementosAsCSVByDates(LocalDate dateStart, LocalDate dateEnd) throws Exception {
		String fileName = "interventionMementoDates.csv";
		String path = storageFolder + "/" + fileName;
		CsvToolsGeneric.toCsv(path, getAllMementoDates(dateStart, dateEnd), ";");
	}

	/**
	 * Counts the number of memento (historic).
	 * 
	 * @return CountDto Returns an object with the number of memento
	 */
	@Override
	public CountDto count() {
		return new CountDto(intMementoRepository.count());
	}

	/**
	 * Filter memento (historic) depending of the event selected & between the date start and the date end choosen.
	 * 
	 * @param interventionId Long representing the id of the event selected by the client
	 * @param dateStart LocalDate to know when to start to retrieve memento.
	 * @param dateEnd LocalDate to know when to finish to retrieve memento.
	 * 
	 * @return List<InterventionMemento> list of memento (historic)
	 *
	 */
	@Override
	public List<InterventionMemento> filterMemento(long interventionId, LocalDateTime dateStart, LocalDateTime dateEnd,
			int page, int size) {
		return intMementoRepository.filterByIntIdAndDates(interventionId, dateStart, dateEnd,
				PageRequest.of(page, size));
	}

	/**
	 * Counts the number of memento depending of the event selected & between the date start and the date end choosen.
	 * 
	 * @param interventionId Long representing the id of the event selected by the client
	 * @param dateStart LocalDate to know when to start to retrieve memento.
	 * @param dateEnd LocalDate to know when to finish to retrieve memento.
	 * 
	 * @return CountDto Returns an object with the number of memento
	 *
	 */
	@Override
	public CountDto countFilter(long interventionId, LocalDateTime dateStart, LocalDateTime dateEnd) {
		return new CountDto(intMementoRepository.countFilter(interventionId, dateStart, dateEnd));
	}

	/**
	 * Get the intervention history (InterventionMemento) before the one called by
	 * the user.
	 * 
	 * @param interventionId        id of the intervention the client is interested
	 *                              by
	 * @param interventionMementoId intervention history the client selected
	 * 
	 * @return InterventionMemento created before the one selected
	 */
	@Override
	public InterventionMemento getLastBeforeMemento(long interventionId, long interventionMementoId) {
		return intMementoRepository.getLastBeforeIntMemento(interventionId, interventionMementoId);
	}
}
