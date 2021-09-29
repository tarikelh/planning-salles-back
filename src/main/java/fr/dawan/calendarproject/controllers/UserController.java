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

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	// GET
	@GetMapping(produces = "application/json")
	public List<AdvancedUserDto> getAll() {
		return userService.getAllUsers();
	}

	// GET
	@GetMapping(produces = "application/json")
	public List<AdvancedUserDto> getAll(@RequestParam(value = "page", defaultValue = "-1", required = false) int page, 
										@RequestParam(value = "size", defaultValue = "-1", required = false) int size, 
										@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return userService.getAllUsers(page, size, search);
	}
	
	// COUNT
	@GetMapping(value = "/count", produces = "application/json")
	public CountDto countFilter(@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return userService.count(search);
	}
	
	// GET by Type
	@GetMapping(value = "/search/{type}", produces = "application/json")
	public List<AdvancedUserDto> getAllByType(@PathVariable("type") String type) {
		return userService.getAllUsersByType(type);
	}

	// GET - id
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public ResponseEntity<?> getById(@PathVariable("id") long id) {
		AdvancedUserDto user = userService.getById(id);
		
		if (user != null)
			return ResponseEntity.status(HttpStatus.OK).body(user);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " Not Found");
	}

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") long id) {
		try {
			userService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("User with id " + id + " Deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " Not Found");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public AdvancedUserDto save(@RequestBody AdvancedUserDto user) {
		return userService.saveOrUpdate(user);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> update(@RequestBody AdvancedUserDto user) {
		AdvancedUserDto u = userService.saveOrUpdate(user);
		
		if (u != null)
			return ResponseEntity.status(HttpStatus.OK).body(u);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + user.getId() + " Not Found");
	}
}
