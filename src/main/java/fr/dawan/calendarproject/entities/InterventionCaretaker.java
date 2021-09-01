package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.tools.CompareGeneric;

@Component
public class InterventionCaretaker {
	
	@Autowired
	private InterventionMementoRepository intMementoRepository;
	
	@Autowired
	private InterventionRepository interventionRepository;
	
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
				modificationsDone = "";
			}
		}
		else {
			messageAction = " has been created by ";
			modificationsDone = "";
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
	
	public List<InterventionMemento> getAllMemento() {
		return intMementoRepository.findAll();
	}
	
	public List<InterventionMementoDto> getAllMemento(int page, int size) {
		List<InterventionMemento> iMemList = intMementoRepository.findAll(PageRequest.of(page, size))
				.get().collect(Collectors.toList());
		List<InterventionMementoDto> iMemDtoList = new ArrayList<InterventionMementoDto>();
		
		for (InterventionMemento interventionMemento : iMemList) {
			iMemDtoList.add(DtoTools.convert(interventionMemento, InterventionMementoDto.class));
		}
		
		return iMemDtoList;
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
