package fr.dawan.calendarproject.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.dawan.calendarproject.repositories.InterventionMementoRepository;

@Component
public class InterventionCaretaker {
	
	@Autowired
	private InterventionMementoRepository intMementoRepository;
	
	//add comment with a String or remove it???
	private Map<Long, Map<String, InterventionMemento>> interventionsHistory;

	public InterventionCaretaker() {
		interventionsHistory = new HashMap<Long, Map<String, InterventionMemento>>();
	}
	
	public void addMemento(long interventionId, String mementoMessage, InterventionMemento memento) {
		if(mementoMessage!=null && !mementoMessage.trim().isEmpty() && memento!=null) {
			Map<String, InterventionMemento> mapForOfferId= interventionsHistory.get(interventionId);
			if(mapForOfferId==null) {
				mapForOfferId = new HashMap<String, InterventionMemento>();
				interventionsHistory.put(interventionId, mapForOfferId);
			}
			mapForOfferId.put(mementoMessage, memento);
		}
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
	
	public List<InterventionMemento> getAllMementoDates(Date dateStart, Date dateEnd) {
		List<InterventionMemento> lstMem = intMementoRepository.findAll();
		List<InterventionMemento> lstMemDates = new ArrayList<InterventionMemento>();
		//Can improve the comparaison between two dates with a CompareTo > check with the group what they prefer
		Date dateStartInclusive = DateUtils.addDays(dateStart, -1);
		Date dateEndInclusive = DateUtils.addDays(dateEnd, +1);
		for (InterventionMemento interventionMemento : lstMem) {
			if(interventionMemento.getDateCreatedState().after(dateStartInclusive) && interventionMemento.getDateCreatedState().before(dateEndInclusive)) {
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
