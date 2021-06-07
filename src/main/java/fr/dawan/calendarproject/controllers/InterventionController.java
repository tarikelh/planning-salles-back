package fr.dawan.calendarproject.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

	@Value("${app.storagefolder}")
	private String storagefolder;

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
	// Need to create a InterventionMemento controller (for now only this method)??
	@GetMapping(value = "/memento", produces = "text/csv")
	public ResponseEntity<?> getAllMementoCSV() {
		try {
			// Create CSV
			interventionService.getAllIntMementoCSV();

			// Return CSV
			// change "interventionMementoDates.csv" for a parameter (in .properties?) here
			// and in InterventionCaretaker
			String filename = "interventionMemento.csv";
			File file = new File(filename);
			Path path = Paths.get(file.getAbsolutePath());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("text/csv")).body(new FileSystemResource(path));
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("création non réalisée");
		}
	}

	// GET Memento between 2 dates >> Implemented for the CSV test
	// Need to create a InterventionMemento controller (for now only this method)??
	@GetMapping(value = "/memento-dates", produces = "text/csv")
	public ResponseEntity<?> getAllMementoCSVDates(
			@RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
			@RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd) {
		try {
			// Create CSV
			interventionService.getAllIntMementoCSVDates(dateStart, dateEnd);
			// Return CSV
			// change "interventionMementoDates.csv" for a parameter (in .properties?) here
			// and in InterventionCaretaker
			String filename = "interventionMementoDates.csv";
			File file = new File(filename);
			Path path = Paths.get(file.getAbsolutePath());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("text/csv")).body(new FileSystemResource(path));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("création csv non réalisée");
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
	public InterventionDto save(@Valid @RequestBody InterventionDto intervention) throws Exception {
		return interventionService.saveOrUpdate(intervention);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public InterventionDto update(@Valid @RequestBody InterventionDto intervention, BindingResult br) throws Exception {
		return interventionService.saveOrUpdate(intervention);
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
	
	@GetMapping(value = "/interval/{userId}/{page}/{size}", produces="application/json")
	public List<InterventionDto> getFromUserByDateRange(@PathVariable("userId") long userId, 
			@RequestParam("start") String start, 
			@RequestParam("end") String end, 
			@PathVariable("page") int page,
			@PathVariable("size") int size) {
		return interventionService.getFromUserByDateRange(userId, LocalDate.parse(start), LocalDate.parse(end), page, size);
	}
	
	@GetMapping(value = "/interval/{page}/{size}", produces="application/json")
	public List<InterventionDto> getAllByDateRange(@RequestParam("start") String start, 
			@RequestParam("end") String end, 
			@PathVariable("page") int page,
			@PathVariable("size") int size) {
		return interventionService.getAllByDateRange(LocalDate.parse(start), LocalDate.parse(end), page, size);
	}
	
//	public List<InterventionDto> getUserIcalFile(long userId) {
//		
//	}

}