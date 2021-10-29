package fr.dawan.calendarproject.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@Value("${app.storagefolder}")
	private String storageFolder;

	@GetMapping(value = "/{page}/{size}", produces = "application/json")
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

	@GetMapping(value = "/count")
	public CountDto count() {
		return caretaker.count();
	}

	@GetMapping(value = "filter/{interventionId}")
	public ResponseEntity<?> filterMemento(@PathVariable("interventionId") long interventionId,
			@RequestParam("start") String start, @RequestParam("end") String end, @RequestParam("page") int page,
			@RequestParam("size") int size) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTimeStart = LocalDateTime.parse(start, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(end, formatter);
		List<InterventionMemento> iMem = caretaker.filterMemento(interventionId, dateTimeStart, dateTimeEnd, page,
				size);

		if (iMem.size() == 0)
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Intervention Memento Dto with id " + interventionId + " Not Found");

		return ResponseEntity.ok(iMem);
	}

	@GetMapping(value = "/count-filter")
	public CountDto countFilter(@RequestParam("interventionId") long interventionId,
			@RequestParam("start") String start, @RequestParam("end") String end) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTimeStart = LocalDateTime.parse(start, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(end, formatter);
		return caretaker.countFilter(interventionId, dateTimeStart, dateTimeEnd);
	}

	@GetMapping(value = "/restore/{id}")
	public ResponseEntity<?> restore(@PathVariable("id") long mementoId,
			@RequestHeader(value = "Authorization") String token) {
		String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));

		try {
			InterventionDto intDto = caretaker.restoreMemento(mementoId, email);
			return ResponseEntity.status(HttpStatus.OK).body(intDto);
		} catch (CloneNotSupportedException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occured while restoring the memento!");
		}
	}

	// get the "last before" intervention memento to visualize modifications that
	// were done
	@GetMapping(value = "/last-before")
	public ResponseEntity<?> lastBeforeMemento(@RequestParam("interventionId") long interventionId,
			@RequestParam("interventionMementoId") long interventionMementoId) {

		InterventionMemento iMem = caretaker.getLastBeforeMemento(interventionId, interventionMementoId);

		if (iMem == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Intervention Memento with id " + interventionId + " Not Found");

		return ResponseEntity.ok(iMem);
	}

	// Export Intervention Mementos as CSV
	@GetMapping(value = "/export-csv", produces = "text/csv")
	public ResponseEntity<?> exportInterventionMementoAsCSV() {
		try {
			caretaker.serializeInterventionMementosAsCSV();

			String filename = "interventionMemento.csv";
			String filePath = storageFolder + "/" + filename;
			File file = new File(filePath);
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

	// Export Intervention Mementos as CSV between 2 dates
	@GetMapping(value = "/export-csv-dates", produces = "text/csv")
	public ResponseEntity<?> exportInterventionMementoAsCSVByDates(
			@RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
			@RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd) {
		try {

			caretaker.serializeInterventionMementosAsCSVByDates(dateStart, dateEnd);

			String filename = "interventionMementoDates.csv";
			String filePath = storageFolder + "/" + filename;
			File file = new File(filePath);
			Path path = Paths.get(file.getAbsolutePath());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.parseMediaType("text/csv")).body(new FileSystemResource(path));

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("création csv non réalisée");
		}
	}
}