package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.services.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;
	
	// GET
	@GetMapping(produces = "application/json")
	public List<LocationDto> getAll() {
		return locationService.getAllLocations();
	}

	// GET
	@GetMapping(value = {"/pagination"}, produces = "application/json")
	public List<LocationDto> getAllPagination(@RequestParam(value = "page", defaultValue = "-1", required = false) int page, 
									@RequestParam(value = "size", defaultValue = "-1", required = false) int size, 
									@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return locationService.getAllLocations(page, size, search);
	}
	
	// COUNT
	@GetMapping(value = "/count", produces = "application/json")
	public CountDto countFilter(@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return locationService.count(search);
	}

	// GET - id
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public ResponseEntity<?> getById(@PathVariable("id") long id) {
		LocationDto loc = locationService.getById(id);
		if (loc != null)
			return ResponseEntity.status(HttpStatus.OK).body(loc);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location with id " + id + "Not Found");
	}

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") long id) {
		try {
			locationService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Location with id " + id + " Deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location with id " + id + " Not Found");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public LocationDto save(@RequestBody LocationDto location) {
		return locationService.saveOrUpdate(location);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> update(@RequestBody LocationDto location) {
		LocationDto loc = locationService.saveOrUpdate(location);
		if (loc != null)
			return ResponseEntity.status(HttpStatus.OK).body(loc);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location with id " + location.getId() + " Not Found");
	
	}
	
	//FETCH Dawan webservice
	@GetMapping(value = "/dg2", produces="application/json")
	public ResponseEntity<?> fetchAllDG2() {
		try {
			locationService.fetchAllDG2Locations();
			return ResponseEntity.status(HttpStatus.OK).body("Succeed to fetch data from the webservice DG2");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching data from the webservice DG2");
		}
	}
}
