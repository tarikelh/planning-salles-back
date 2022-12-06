package fr.dawan.calendarproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.services.SyncService;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

	@Autowired
	SyncService syncService;

	// GET
	@GetMapping(produces = {"application/json", "text/plain"})
	public ResponseEntity<String> SyncPanningWithDG2() {
		ResponseEntity<String> result;
		try {
			result = ResponseEntity.status(HttpStatus.OK)
					.body(syncService.allDG2Import());
		} catch (Exception e) {
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}
}
