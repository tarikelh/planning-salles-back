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
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.EmployeeDto;
import fr.dawan.calendarproject.services.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	// GET
	@GetMapping(produces = "application/json")
	public List<EmployeeDto> getAll() {
		return employeeService.getAllEmployees();
	}
	
	//GET - id
    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml"})
    public EmployeeDto getById(@PathVariable("id") long id) {
        return employeeService.getById(id);
    }

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") long id) {
		try {
			System.out.println("inside delete... ");
			employeeService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("suppression effectuée");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("suppression non réalisée");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public EmployeeDto save(@RequestBody EmployeeDto course) {
		return employeeService.saveOrUpdate(course);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public EmployeeDto update(@RequestBody EmployeeDto course) {
		return employeeService.saveOrUpdate(course);
	}
}
