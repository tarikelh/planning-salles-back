package fr.dawan.calendarproject.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.services.InterventionService;
import fr.dawan.calendarproject.tools.ICalTools;
import net.fortuna.ical4j.model.Calendar;

@RestController
@RequestMapping("/api/interventions")
public class InterventionController {

	@Autowired
	private InterventionService interventionService;

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
	
	// GET - Masters Interventions
	@GetMapping(value = "/masters",produces = "application/json")
	public List<InterventionDto> getMasterIntervention() {
		return interventionService.getMasterIntervention();
	}
	
	// GET - NO Masters Interventions && verify UserType && between two dates
	@GetMapping(value = "/sub",produces = "application/json")
	public List<InterventionDto> getSubInterventions(@RequestParam("type") String type, @RequestParam("start") String start, 
			@RequestParam("end") String end) {
		return interventionService.getSubInterventions(type, LocalDate.parse(start), LocalDate.parse(end));
	}
	

	// GET Memento >> Implemented for the CSV test
	// Need to create a InterventionMemento controller (for now only this method)??
	@GetMapping(value = "/memento", produces = "text/csv")
	public ResponseEntity<?> getAllMementoCSV() {
		try {
			// Create CSV
			interventionService.getAllIntMementoCSV();

			// Return CSV
			// change "interventionMemento.csv" for a parameter (in .properties?) here
			// and in InterventionCaretaker
			String filename = "interventionMemento.csv";
			File file = new File(filename);
			Path path = Paths.get(file.getAbsolutePath());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("text/csv")).body(new FileSystemResource(path));
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error could not create Memento");
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
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("text/csv")).body(new FileSystemResource(path));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("création csv non réalisée");
		}
	}

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") long id) {
		try {
			interventionService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Intervention with id " + id + " Deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Intervention with id " + id + " Not Found");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public InterventionDto save(@Valid @RequestBody InterventionDto intervention) throws Exception {
		return interventionService.saveOrUpdate(intervention);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> update(@Valid @RequestBody InterventionDto intervention, BindingResult br) throws Exception {
		InterventionDto i = interventionService.saveOrUpdate(intervention);
		
		if (i != null)
			return ResponseEntity.ok(i);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Intervention with Id " + intervention.getId() + " Not Found");
	}

//	// Search
//	@GetMapping(value = "/search", produces = "application/json")
//	public List<InterventionDto> searchByCourse(@RequestParam("formation") String formation) {
//		if (!formation.isEmpty()) {
//			if (StringUtils.isNumeric(formation))
//				return interventionService.getByCourseId(Long.parseLong(formation));
//			else
//				return interventionService.getByCourseTitle(formation);
//		}
//		return null;
//	}
//	
//	@GetMapping(value = "/interval/{userId}", produces="application/json")
//	public List<InterventionDto> getFromUserByDateRange(@PathVariable("userId") long userId, 
//			@RequestParam("start") String start, 
//			@RequestParam("end") String end) {
//		return interventionService.getFromUserByDateRange(userId, LocalDate.parse(start), LocalDate.parse(end));
//	}
//	
//	@GetMapping(value = "/interval", produces="application/json")
//	public List<InterventionDto> getAllByDateRange(@RequestParam("start") String start, @RequestParam("end") String end) {
//		return interventionService.getAllByDateRange(LocalDate.parse(start), LocalDate.parse(end));
//	}
	
	@GetMapping(value = "/ical/{userId}")
	public ResponseEntity<?> exportUserInteventions(@PathVariable("userId")long userId) {
		Calendar calendar = interventionService.exportCalendarAsICal(userId);
		if (calendar != null) {
			String fileName = calendar.getProperty("X-CALNAME").getValue() + ".ics";
			File f = new File(fileName);
			ByteArrayResource resource;
			try {
				resource = ICalTools.generateICSFile(calendar, fileName, f);
				
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
				headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
				headers.add("Pragma", "no-cache");
				headers.add("Expires", "0");
				
				return ResponseEntity.ok().headers(headers).contentLength(f.length())
						.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Creating Calendar");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calendar For user ID " + userId + " is empty or the user ID does not exist");
		
	}

	//Count
	@GetMapping(value = "/count", produces =  "application/json")
	public CountDto countByUserTypeNoMaster(@RequestParam("type") String type) {
		return interventionService.count(type);
	}
	
}
