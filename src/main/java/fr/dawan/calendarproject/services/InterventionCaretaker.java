package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.CompareGeneric;

@Component
public class InterventionCaretaker {
	
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
	
	private String messageAction = null;
	
	private String modificationsDone = null;
	
	//add comment with a String or remove it???
	private Map<Long, Map<String, InterventionMemento>> interventionsHistory;

	public InterventionCaretaker() {
		interventionsHistory = new HashMap<Long, Map<String, InterventionMemento>>();
	}
	
	@Async("taskExecutor")
	public void addMemento(String email, InterventionMemento mementoAfter) throws Exception {
		/*
		if(mementoMessage!=null && !mementoMessage.trim().isEmpty() && memento!=null) {
			Map<String, InterventionMemento> mapForOfferId= interventionsHistory.get(interventionId);
			if(mapForOfferId==null) {
				mapForOfferId = new HashMap<String, InterventionMemento>();
				interventionsHistory.put(interventionId, mapForOfferId);
			}
			mapForOfferId.put(mementoMessage, memento);
		}
		*/
		
		if(intMementoRepository.countByInterventionId(mementoAfter.getState().getInterventionId()) != 0) {
			if(interventionRepository.existsById(mementoAfter.getState().getInterventionId())) {
				messageAction = " has been changed by ";
				
				// Obtain difference between two interventions
				InterventionMemento mementoBefore = intMementoRepository.getLastInterventionMemento(mementoAfter.getState().getInterventionId());
				modificationsDone = CompareGeneric.compareObjects(mementoBefore.getState(),mementoAfter.getState());
			} else {
				messageAction = " has been deleted by ";
				modificationsDone = "n/a";
			}
		}
		else {
			messageAction = " has been created by ";
			modificationsDone = "n/a";
		}
			
		mementoAfter.setMementoMessage(new MementoMessageDto(mementoAfter.getState().getInterventionId(), messageAction, email, modificationsDone));
		
		intMementoRepository.saveAndFlush(mementoAfter);
	}
	
	public Map<String,InterventionMemento> getMemento(long offerId) {
			return interventionsHistory.get(offerId);
	}
	

	public InterventionMemento getMemento(long offerId, String mementoMessage) {
		return getMemento(offerId).get(mementoMessage);
	}
	
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
	
	public List<InterventionMemento> getAllMemento() {
		return intMementoRepository.findAll();
	}
	
	public List<InterventionMemento> getAllMemento(int page, int size) {
		return intMementoRepository.findAll(PageRequest.of(page, size))
				.get().collect(Collectors.toList());
	}
	
	public List<InterventionMemento> getAllMementoDates(LocalDate dateStart, LocalDate dateEnd) {
		List<InterventionMemento> lstMem = intMementoRepository.findAll();
		List<InterventionMemento> lstMemDates = new ArrayList<InterventionMemento>();
		//Can improve the comparison between two dates with a CompareTo > check with the group what they prefer
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
	
	public CountDto count() {
		return new CountDto(intMementoRepository.count());
	}
	
	//Be Careful => special To String because of the MAP
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (long interventionId : interventionsHistory.keySet()) {
			sb.append("Intervention : " + interventionId + " : ").append("\n");
			Map<String, InterventionMemento> historyForInterventionId = interventionsHistory.get(interventionId);
			for (String msg : historyForInterventionId.keySet()) {
				sb.append(msg + " : " + historyForInterventionId.get(msg)).append("\n");
			}
			sb.append("---------------").append("\n");
		}
		return sb.toString();
	}
}
