package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionMemento;

public interface InterventionCaretaker {

	void addMemento(String email, Intervention intervention) throws Exception;

	InterventionMemento getMementoById(long id);

	List<InterventionMemento> getAllMemento();

	List<InterventionMemento> getAllMemento(int page, int size);

	List<InterventionMemento> getAllMementoDates(LocalDate dateStart, LocalDate dateEnd);
	
	void restoreMemento(long mementoId, String email) throws CloneNotSupportedException;

	CountDto count();

}