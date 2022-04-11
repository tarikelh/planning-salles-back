package fr.dawan.calendarproject.controllers;

import fr.dawan.calendarproject.dto.CongeDto;
import fr.dawan.calendarproject.services.CongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave-periods")
public class CongeController {

	@Autowired
	private CongeService congeService;

	@GetMapping(produces = "application/json")
	public List<CongeDto> getAll() {
		return congeService.getAllConges();
	}
	
	@GetMapping(value = "/{employeeId}", produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> getByUserId(@PathVariable("employeeId") long employeeId) {
		List<CongeDto> cDto = congeService.getAllCongesByEmployeeId(employeeId);
		if (cDto != null)
			return ResponseEntity.status(HttpStatus.OK).body(cDto);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conge with user id : " + employeeId + " Not Found");
	}
	
	@GetMapping(value = "/dg2", produces = "application/json")
	public ResponseEntity<String> fetchAllDG2(@RequestHeader Map<String, String> headers) {
		String userDG2 = headers.get("x-auth-token");
		String[] splitUserDG2String = userDG2.split(":");

		try {
			congeService.fetchAllDG2Conges(splitUserDG2String[0], splitUserDG2String[1]);
			return ResponseEntity.status(HttpStatus.OK).body("Succeed to fetch data from the webservice DG2");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while fetching data from the webservice");
		}
	}
	
}
