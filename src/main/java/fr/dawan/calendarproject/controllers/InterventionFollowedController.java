package fr.dawan.calendarproject.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import fr.dawan.calendarproject.dto.InterventionFollowedDto;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.services.InterventionFollowedService;

@RestController
@RequestMapping("/api/interventionsFollowed")
public class InterventionFollowedController {

	@Autowired
	private InterventionFollowedService iFolloService;

	@GetMapping(produces = "application/json")
	public List<InterventionFollowedDto> getAll() {
		return iFolloService.getAllInterventionsFollowed();
	}

	@GetMapping(value = { "/pagination" }, produces = "application/json")
	public List<InterventionFollowedDto> getAllPagination(
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "size", required = false) int size) {
		return iFolloService.getAllInterventionsFollowed(page-1, size);
	}

	@GetMapping(value = "/count", produces = "application/json")
	public CountDto count() {
		return iFolloService.count();
	}
	
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public InterventionFollowedDto getById(@PathVariable("id") long id) {
		return iFolloService.getById(id);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable(value = "id") long id) {
		try {
			iFolloService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Intervention Followed with id " + id + " deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Intervention Followed with id " + id + " Not Found");
		}
	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	public InterventionFollowedDto save(@RequestBody InterventionFollowedDto iFollo) {
		return iFolloService.saveOrUpdate(iFollo);
	}

	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> update(@RequestBody InterventionFollowedDto iFollo) {

		InterventionFollowedDto response = iFolloService.saveOrUpdate(iFollo);

		if (response != null)
			return ResponseEntity.status(HttpStatus.OK).body(response);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Intervention Followed with id " + iFollo.getId() + " Not Found");
	}

	@GetMapping(value = "/userType/{type}", produces = "application/json")
	 List<InterventionFollowedDto> getAllByUserType(@PathVariable("type") UserType type) {
		return iFolloService.findAllByUserType(type);
	}
	
	@GetMapping(value = "/userTypeAndDateRange/{type}/{start}/{end}", produces = "application/json")
	 List<InterventionFollowedDto> getAllByUserTypeAndDateRange(@PathVariable("type") UserType type, @PathVariable("start") String start, @PathVariable("end") String end) {
		return iFolloService.findAllByUserTypeAndDateRange(type, LocalDate.parse(start), LocalDate.parse(end));
	}
	
	@GetMapping(value = "/dateRange/{start}/{end}", produces = "application/json")
	 List<InterventionFollowedDto> getAllByDateRange(@PathVariable("start") String start, @PathVariable("end") String end) {
		return iFolloService.findAllByDateRange(LocalDate.parse(start), LocalDate.parse(end));
	}
	
	@GetMapping(value = "/dg2/{start}/{end}", produces = "application/json")
	public ResponseEntity<String> fetchAllDG2(@PathVariable("start") String start, @PathVariable("end") String end,
			@RequestHeader Map<String, String> headers) {
		String auth = headers.get("x-auth-token");
		try {
			int count = iFolloService.fetchAllDG2InterventionsFollowed(auth.split(":")[0], auth.split(":")[1],
					LocalDate.parse(start), LocalDate.parse(end));

			return ResponseEntity.status(HttpStatus.OK).body(
					"Succeed to fetch data from the webservice DG2. " + count + " interventions imported or updated.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while fetching data from the webservice");
		}
	}
}
