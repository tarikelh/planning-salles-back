package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.mapper.DtoMapper;
import fr.dawan.calendarproject.mapper.DtoMapperImpl;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.CompareGeneric;

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
	
	private DtoMapper mapper = new DtoMapperImpl();
	
	private String messageAction = null;
	
	private String modificationsDone = null;

	public InterventionCaretakerImpl() {
		
	}
	
	@Override
	@Async("taskExecutor")
	public void addMemento(String email, Intervention intervention) throws Exception {
		
		InterventionMemento memento = new InterventionMemento();
		memento.setState(DtoTools.convert(intervention, InterventionMementoDto.class));
		
		if(intMementoRepository.countByInterventionId(memento.getState().getInterventionId()) != 0) {
			if(interventionRepository.existsById(memento.getState().getInterventionId())) {
				messageAction = " has been changed by ";

				// Obtain difference between two interventions
				InterventionMemento mementoBefore = intMementoRepository.getLastInterventionMemento(memento.getState().getInterventionId());
				modificationsDone = CompareGeneric.compareObjects(mementoBefore.getState(),memento.getState());
			} else {
				messageAction = " has been deleted by ";
				modificationsDone = "";
			}
		}
		else {
			messageAction = " has been created by ";
			modificationsDone = "";
		}
			
		memento.setMementoMessage(new MementoMessageDto(memento.getState().getInterventionId(), messageAction, email, modificationsDone));
		
		intMementoRepository.saveAndFlush(memento);
	}
	
	@Override
	public InterventionDto restoreMemento(long mementoId, String email) throws CloneNotSupportedException {
		InterventionMemento iMem = intMementoRepository.findById(mementoId).get();

		Intervention intToRestore = mapper.interventionMementoDtoToIntervention(iMem.getState());

		InterventionMemento newIMem = (InterventionMemento) iMem.clone();
		newIMem.setId(0);
		
		intToRestore.setCourse(courseRepository.findById(iMem.getState().getCourseId()).get());
		intToRestore.setLocation(locationRepository.findById(iMem.getState().getLocationId()).get());
		intToRestore.setUser(userRepository.findById(iMem.getState().getUserId()).get());
		
		if (iMem.getState().getMasterInterventionId() > 0)
			intToRestore.setMasterIntervention(interventionRepository.findById(iMem.getState().getMasterInterventionId()).get());
		else
			intToRestore.setMasterIntervention(null);
		
		interventionService.checkIntegrity(mapper.interventionToInterventionDto(intToRestore));
		
		//récupérer intToRestore
		intToRestore.setVersion(interventionRepository.getOne(intToRestore.getId()).getVersion());
		interventionRepository.saveAndFlush(intToRestore);
		
		newIMem.setMementoMessage(new MementoMessageDto(newIMem.getState().getInterventionId(), " Has been restored ", email, ""));
		
		intMementoRepository.saveAndFlush(newIMem);
		
		InterventionDto intDto = DtoTools.convert(intToRestore, InterventionDto.class);
		
		return intDto;
	}
	
	@Override
	public InterventionMemento getMementoById(long id) {
		Optional<InterventionMemento> i = intMementoRepository.findById(id);
		InterventionMementoDto iMemDto = i.get().getState();
		
		if (i.isPresent()) {
			if(iMemDto.getCourseId() > 0)
				iMemDto.setCourseTitle(courseRepository.findById(iMemDto.getCourseId()).get().getTitle());
			
			if(iMemDto.getUserId() > 0) {
				User u = userRepository.findById(iMemDto.getUserId()).get();
				iMemDto.setUserEmail(u.getEmail());
				iMemDto.setUserFullName(u.getFullname());
			}
			
			if(iMemDto.getLocationId() > 0)
				iMemDto.setLocationCity(locationRepository.findById(iMemDto.getLocationId()).get().getCity());
			
			return i.get();
		}
	
		return null;
	}
	
	@Override
	public List<InterventionMemento> getAllMemento() {
		return intMementoRepository.findAll();
	}
	
	@Override
	public List<InterventionMemento> getAllMemento(int page, int size) {
		return intMementoRepository.findAll(PageRequest.of(page, size))
				.get().collect(Collectors.toList());
	}
	
	@Override
	public List<InterventionMemento> getAllMementoDates(LocalDate dateStart, LocalDate dateEnd) {
		List<InterventionMemento> lstMem = intMementoRepository.findAll();
		List<InterventionMemento> lstMemDates = new ArrayList<InterventionMemento>();

		LocalDate dateStartInclusive = dateStart.minusDays(1);
		LocalDate dateEndInclusive = dateEnd.plusDays(1);
		for (InterventionMemento interventionMemento : lstMem) {
			LocalDate mementoDate = interventionMemento.getDateCreatedState().toLocalDate();
			if(mementoDate.isAfter(dateStartInclusive) && mementoDate.isBefore(dateEndInclusive)) {
				lstMemDates.add(interventionMemento);
			}
		}
		return lstMemDates;
	}
	
	@Override
	public CountDto count() {
		return new CountDto(intMementoRepository.count());
	}
	
}
