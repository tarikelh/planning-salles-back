package fr.dawan.calendarproject.controllers;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DateRangeDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.MailingListDto;
import fr.dawan.calendarproject.services.EmailService;
import fr.dawan.calendarproject.services.InterventionService;
import fr.dawan.calendarproject.tools.ICalTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;
import net.fortuna.ical4j.model.Calendar;

@RestController
@RequestMapping("/api/interventions")
public class InterventionController {

	@Autowired
	private InterventionService interventionService;
	

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

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

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") long id, @RequestHeader(value = "Authorization") String token) {
		String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));
		try {
			interventionService.deleteById(id, email);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Intervention with id " + id + " Deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Intervention with id " + id + " Not Found");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public InterventionDto save(@Valid @RequestBody InterventionDto intervention, @RequestHeader(value = "Authorization") String token) throws Exception {
		String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));
		return interventionService.saveOrUpdate(intervention, email);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> update(@Valid @RequestBody InterventionDto intervention, @RequestHeader(value = "Authorization") String token) throws Exception {
	
		String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));

		InterventionDto i = interventionService.saveOrUpdate(intervention, email);
		
		if (i != null)
			return ResponseEntity.ok(i);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Intervention with Id " + intervention.getId() + " Not Found");
	}

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
	
	//@RequestBody int[] usersId,
	@PostMapping(value = "/email/employees", produces = "application/json")
	public ResponseEntity<?> sendCalendarToEmployees(@Valid @RequestBody MailingListDto mailingList) {
		try {
			emailService.sendCalendarToSelectedEmployees(mailingList.getUsersId(), LocalDate.parse(mailingList.getDateStart()), LocalDate.parse(mailingList.getDateEnd()));
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("E-mail(s) sent");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while sending e-mail(s)");
		}
	}
	
	@PostMapping(value = "/split/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> splitIntervention(@PathVariable("id") long interventionId ,@RequestBody List<DateRangeDto> dates) {
		List<InterventionDto> iDtoList = interventionService.splitIntervention(interventionId, dates);
		
		if (iDtoList != null) {
			return ResponseEntity.ok(iDtoList);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Intervention with id " + interventionId + " Not Found");
		}
	}

	
}
