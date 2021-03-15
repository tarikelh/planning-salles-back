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

import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.services.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	//GET
	@GetMapping(produces = "application/json")
	public List<CourseDto> getAll() {
		return courseService.getAllCourses();
	}
	
	//GET - id
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml"})
	public CourseDto getById(@PathVariable("id") long id) {
		return courseService.getById(id);
	}
	
	//DELETE - supprimer
	@DeleteMapping(value="/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id" ) long id) {
		try {
			System.out.println("inside delete... ");
			courseService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("suppression effectuée");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("suppression non réalisée");
		}
	}
	
	//POST - ajouter (ou modifier)
	@PostMapping(consumes="application/json", produces = "application/json")
	public CourseDto save(@RequestBody CourseDto course) {
		return courseService.saveOrUpdate(course);
	}
	
	//PUT - modifier
	@PutMapping(consumes="application/json", produces = "application/json")
	public CourseDto update(@RequestBody CourseDto course) {
		return courseService.saveOrUpdate(course);
	}

}
