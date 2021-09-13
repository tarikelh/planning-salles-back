package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionMemento;

public interface InterventionCaretaker {

	void addMemento(String email, Intervention intervention) throws Exception;

	InterventionMemento getMementoById(long id);

	List<InterventionMemento> getAllMemento();

	List<InterventionMemento> getAllMemento(int page, int size);

	List<InterventionMemento> getAllMementoDates(LocalDate dateStart, LocalDate dateEnd);

	InterventionDto restoreMemento(long mementoId, String email) throws CloneNotSupportedException;

	void serializeInterventionMementosAsCSV() throws Exception;

	void serializeInterventionMementosAsCSVByDates(LocalDate dateStart, LocalDate dateEnd) throws Exception;

	CountDto count();
	
	List<InterventionMemento> filterMemento(long interventionId, LocalDateTime dateStart, LocalDateTime dateEnd, int page, int size);
	
	CountDto countFilter(long interventionId, LocalDateTime dateStart, LocalDateTime dateEnd);
	
	InterventionMemento getLastBeforeMemento(long interventionId, long interventionMementoId);

}