package fr.dawan.calendarproject.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.services.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;

	// GET
	@GetMapping(produces = "application/json")
	public List<CourseDto> getAll() {
		return courseService.getAllCourses();
	}

	// GET
	@GetMapping(value = { "/pagination" }, produces = "application/json")
	public List<CourseDto> getAllPagination(
			@RequestParam(value = "page", defaultValue = "-1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "-1", required = false) int size,
			@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return courseService.getAllCourses(page-1, size, search);
	}

	// COUNT
	@GetMapping(value = { "/count" }, produces = "application/json")
	public CountDto countFilter(@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		return courseService.count(search);
	}

	// GET - id
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> getById(@PathVariable("id") long id) {
		CourseDto cDto = courseService.getById(id);
		if (cDto != null)
			return ResponseEntity.status(HttpStatus.OK).body(cDto);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with id " + id + " Not Found");
	}

	// DELETE - supprimer
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable(value = "id") long id) {
		try {
			courseService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Course with id " + id + " deleted");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with id " + id + " Not Found");
		}
	}

	// POST - ajouter (ou modifier)
	@PostMapping(consumes = "application/json", produces = "application/json")
	public CourseDto save(@RequestBody CourseDto course) {
		return courseService.saveOrUpdate(course);
	}

	// PUT - modifier
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> update(@RequestBody CourseDto course) {

		CourseDto response = courseService.saveOrUpdate(course);

		if (response != null)
			return ResponseEntity.status(HttpStatus.OK).body(response);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with id " + course.getId() + " Not Found");
	}

	// FETCH Dawan webservice
	@GetMapping(value = "/dg2", produces = "application/json")
	public ResponseEntity<String> fetchAllDG2(@RequestHeader Map<String, String> headers) {
		String userDG2 = headers.get("x-auth-token");
		String[] splitUserDG2String = userDG2.split(":");

		try {
			courseService.fetchAllDG2Courses(splitUserDG2String[0], splitUserDG2String[1]);
			return ResponseEntity.status(HttpStatus.OK).body("Succeed to fetch data from the webservice DG2");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while fetching data from the webservice");
		}
	}
}
