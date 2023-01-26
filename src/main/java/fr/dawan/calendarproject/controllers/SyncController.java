package fr.dawan.calendarproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.LoginDto;
import fr.dawan.calendarproject.services.SyncService;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

	@Autowired
	SyncService syncService;

	// GET
	@PostMapping(produces = {"application/json", "text/plain"}, consumes = "application/json")
	public ResponseEntity<String> SyncPanningWithDG2(@RequestBody LoginDto loginDto) {
		ResponseEntity<String> result;
		try {
			result = ResponseEntity.status(HttpStatus.OK)
					.body(syncService.allDG2Import(loginDto));
		} catch (Exception e) {
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	@PostMapping(produces = {"application/json", "text/plain"}, consumes = "application/json", path = "locations")
	public ResponseEntity<String> SyncLocationsWithDG2(@RequestBody LoginDto loginDto) {
		ResponseEntity<String> result;
		try {
			result = ResponseEntity.status(HttpStatus.OK)
					.body(syncService.locationsDG2Import(loginDto));
		} catch (Exception e) {
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}
}
