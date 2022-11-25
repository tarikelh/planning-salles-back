package fr.dawan.calendarproject.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.SyncPlanningDto;
import fr.dawan.calendarproject.services.SyncService;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

	@Autowired
	SyncService syncService;

	// POST
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> SyncPanningWithDG2(@RequestBody SyncPlanningDto syncPlanningDto) {
		ResponseEntity<String> result;
		try {
			result = ResponseEntity.status(HttpStatus.OK).body(syncService.allDG2Import(syncPlanningDto.getEmail(), syncPlanningDto.getPassword(),
					LocalDate.parse(syncPlanningDto.getStart()), LocalDate.parse(syncPlanningDto.getEnd())));
		} catch (Exception e) {
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}
}
