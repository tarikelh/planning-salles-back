package fr.dawan.calendarproject.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;
import fr.dawan.calendarproject.services.LeavePeriodService;

@RestController
@RequestMapping("/api/leave-periods")
public class LeavePeriodController {

	@Autowired
	private LeavePeriodService leavePeriodService;

	@GetMapping(produces = "application/json")
	public List<LeavePeriodDto> getAll() {
		return leavePeriodService.getAllLeavePeriods();
	}
	
	@GetMapping(value = "/between", produces = "application/json")
	public List<LeavePeriodDto> getBetween(@RequestParam("type") String type,
			@RequestParam("start") String start, @RequestParam("end") String end) {
		return leavePeriodService.getBetween(type, LocalDate.parse(start), LocalDate.parse(end));
	}
	
	@GetMapping(value = "/{employeeId}", produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> getByUserId(@PathVariable("employeeId") long employeeId) {
		List<LeavePeriodDto> lpDto = leavePeriodService.getAllLeavePeriodsByEmployeeId(employeeId);
		if (lpDto != null)
			return ResponseEntity.status(HttpStatus.OK).body(lpDto);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave Period with employee id : " + employeeId + " Not Found");
	}
	
	@GetMapping(value = "/count", produces = "application/json")
	public CountDto countByUserTypeNoMaster(@RequestParam("type") String type) {
		return leavePeriodService.count(type);
	}
	
	@GetMapping(value = "/dg2/{firstDay}/{lastDay}", produces = "application/json")
	public ResponseEntity<String> fetchAllDG2(@RequestHeader Map<String, String> headers, 
											  @PathVariable("firstDay") String firstDay, 
											  @PathVariable("lastDay") String lastDay) {
		String userDG2 = headers.get("x-auth-token");
		String[] splitUserDG2String = userDG2.split(":");

		try {
			leavePeriodService.fetchAllDG2LeavePeriods(splitUserDG2String[0], splitUserDG2String[1], firstDay, lastDay );
			return ResponseEntity.status(HttpStatus.OK).body("Succeed to fetch data from the webservice DG2");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while fetching data from the webservice");
		}
	}
	
}
