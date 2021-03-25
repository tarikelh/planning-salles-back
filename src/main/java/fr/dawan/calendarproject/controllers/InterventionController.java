package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.InterventionCaretaker;
import fr.dawan.calendarproject.services.InterventionService;

@RestController
@RequestMapping("/api/interventions")
public class InterventionController {

	@Autowired
	private InterventionService interventionService;
	
	@Autowired
	private InterventionCaretaker caretaker;
	
	//private InterventionMemento interventionMemento;
	
	
	// GET
	@GetMapping(produces = "application/json")
	public List<InterventionDto> getAll() {
		return interventionService.getAllInterventions();
	}

	// GET - id
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public InterventionDto getById(@PathVariable("id") long id) {
		return interventionService.getById(id);
	}
	
	// GET Memento >> Implemented for the CSV test
	//Need to create a InterventionMemento controller (for now only this method)??
	@GetMapping(value = "/memento")
	public ResponseEntity<?> getAllMementoCSV() {
		try {
			interventionService.getAllIntMementoCSV();
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("création csv InterventionMemento effectuée");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("création non réalisée");
		}
	}

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") long id) {
		try {
			System.out.println("inside delete... ");
			interventionService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("suppression effectuée");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("suppression non réalisée");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public InterventionDto save(@RequestBody InterventionDto intervention) {
		try {
			//TO CHECK
			return interventionService.saveOrUpdate(intervention);
		} catch (Exception e) {
			e.printStackTrace(); //Pb lors de la création
			return null;
		}
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public InterventionDto update(@RequestBody InterventionDto intervention) {
		try {
			return interventionService.saveOrUpdate(intervention);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//throw e > ec
			e.printStackTrace();
			return null;
		}
	}

	// Search
	@GetMapping(value = "/search", produces = "application/json")
	public List<InterventionDto> searchByCourse(@RequestParam("formation") String formation) {
		if (!formation.isEmpty()) {
			if (StringUtils.isNumeric(formation))
				return interventionService.getByCourseId(Long.parseLong(formation));
			else
				return interventionService.getByCourseTitle(formation);
		}
		return null;
	}

}