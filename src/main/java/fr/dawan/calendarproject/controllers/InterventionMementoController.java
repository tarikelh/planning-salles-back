package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.services.InterventionCaretaker;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@RestController
@RequestMapping("/api/intervention-memento")
public class InterventionMementoController {
	
	@Autowired
	private InterventionCaretaker caretaker;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
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
	
	@GetMapping(value="/restore/{id}")
	public ResponseEntity<?> restore(@PathVariable("id") long mementoId, @RequestHeader(value = "Authorization") String token) {
		String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));
		
		try {
			InterventionDto intDto = caretaker.restoreMemento(mementoId, email);
			//return (ResponseEntity.ok("Memento with Id " + mementoId + " restored!"));
			return ResponseEntity.status(HttpStatus.OK).body(intDto);
		} catch (CloneNotSupportedException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured while restoring the memento!");
		}
		
	}
	
//	@GetMapping(value = "/search")
//	public ResponseEntity<?> searchMemento(@PathParam("id") long interventionId, @PathParam("dateStart") LocalDate dStart, @PathParam("dateEnd") LocalDate dEnd) {
//		List<InterventionMemento> iMemList = caretaker.getMemento(interventionId, dStart, dEnd);
//		return ResponseEntity.ok(null);
//	}
}