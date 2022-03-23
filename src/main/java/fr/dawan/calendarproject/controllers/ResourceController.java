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
import fr.dawan.calendarproject.dto.ResourceDto;
import fr.dawan.calendarproject.services.ResourceService;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	// GET
	@GetMapping(produces = "application/json")
	public List<ResourceDto> getAll() {
		return resourceService.getAllResources();
	}

	// GET
	@GetMapping(value = { "/pagination" }, produces = "application/json")
	public List<ResourceDto> getAllPagination(
			@RequestParam(value = "page", defaultValue = "-1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "-1", required = false) int size,
			@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return resourceService.getAllResources(page, size, search);
	}

	// GET
	@GetMapping(value = { "/availability" }, produces = "application/json")
	public List<ResourceDto> getByAvailability(
			@RequestParam(value = "availability", defaultValue = "true", required = false) boolean availability) {
		return resourceService.getResourceByRoomAvailability(availability);
	}

	// GET
	@GetMapping(value = { "/quantity" }, produces = "application/json")
	public List<ResourceDto> getByQuantity(@RequestParam(value = "quantity", required = true) int quantity) {
		return resourceService.getResourceByQuantity(quantity);
	}

	// GET
	@GetMapping(value = { "/quantityRange" }, produces = "application/json")
	public List<ResourceDto> getByQuantityRange(
			@RequestParam(value = "value1", required = true) int value1,
			@RequestParam(value = "value2", required = true) int value2) {
		return resourceService.getResourceByQuantityRange(value1, value2);
	}


	// COUNT
	@GetMapping(value = { "/count" }, produces = "application/json")
	public CountDto countFilter(@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return resourceService.count(search);
	}

	// GET - id
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> getById(@PathVariable("id") long id) {
		ResourceDto rDto = resourceService.getById(id);
		if (rDto != null)
			return ResponseEntity.status(HttpStatus.OK).body(rDto);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with id " + id + " Not Found");
	}

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable(value = "id") long id) {
		try {
			resourceService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Resource with id " + id + " deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with id " + id + " Not Found");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResourceDto save(@RequestBody ResourceDto resource) {
		return resourceService.saveOrUpdate(resource);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> update(@RequestBody ResourceDto resource) {

		ResourceDto response = resourceService.saveOrUpdate(resource);

		if (response != null)
			return ResponseEntity.status(HttpStatus.OK).body(response);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with id " + resource.getId() + " Not Found");
	}
}
