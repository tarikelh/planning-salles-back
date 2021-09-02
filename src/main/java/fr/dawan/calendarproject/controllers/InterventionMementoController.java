package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.services.InterventionCaretaker;

@RestController
@RequestMapping("/api/intervention-memento")
public class InterventionMementoController {
	
	@Autowired
	private InterventionCaretaker caretaker;
	
	@GetMapping(value = "/{page}/{size}", produces="application/json")
	public List<InterventionMemento> getAll(@PathVariable("page") int page, @PathVariable("size") int size) {
		return caretaker.getAllMemento(page, size);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> GetById(@PathVariable("id") long id) {
		InterventionMemento iMem = caretaker.getMementoById(id);
		
		if (iMem == null) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Intervention Memento with id " + id + " Not Found");
			
		return ResponseEntity.ok(iMem);
	}

	@GetMapping(value="/count")
	public CountDto count() {
		return caretaker.count();
	}
//	@GetMapping(value = "/search")
//	public ResponseEntity<?> searchMemento(@PathParam("id") long interventionId, @PathParam("dateStart") LocalDate dStart, @PathParam("dateEnd") LocalDate dEnd) {
//		List<InterventionMemento> iMemList = caretaker.getMemento(interventionId, dStart, dEnd);
//		return ResponseEntity.ok(null);
//	}
}