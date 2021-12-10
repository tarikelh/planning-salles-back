package fr.dawan.calendarproject.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.CourseMapper;
import fr.dawan.calendarproject.repositories.CourseRepository;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * Fetches all of the existing courses.
	 * 
	 * @return result Returns a list of courses.
	 *
	 */

	@Override
	public List<CourseDto> getAllCourses() {
		List<Course> courses = courseRepository.findAll();

		List<CourseDto> result = new ArrayList<>();
		for (Course c : courses) {
			result.add(courseMapper.courseToCourseDto(c));
		}

		return result;
	}

	/**
	 * Fetches all of the existing courses, with a pagination system.
	 * 
	 * @param page   An integer representing the current page displaying the
	 *               courses.
	 * @param size   An integer defining the number of courses displayed by page.
	 * @param search A String representing the admin's input to search for a
	 *               specific course.
	 * 
	 * @return result Returns a list of courses, according to the pagination
	 *         criteria.
	 *
	 */

	@Override
	public List<CourseDto> getAllCourses(int page, int size, String search) {
		Pageable pagination = null;

		if (page > -1 && size > 0)
			pagination = PageRequest.of(page, size);
		else
			pagination = Pageable.unpaged();

		List<Course> courses = courseRepository.findAllByTitleContaining(search, pagination).get()
				.collect(Collectors.toList());

		List<CourseDto> result = new ArrayList<>();
		for (Course c : courses) {
			result.add(courseMapper.courseToCourseDto(c));
		}

		return result;
	}

	/**
	 * Counts the number of courses.
	 * 
	 * @param search A String representing the admin's input to search for a
	 *               specific course.
	 * 
	 * @return CountDto Returns the number of courses, according to the search
	 *         criteria.
	 *
	 */

	@Override
	public CountDto count(String search) {
		return new CountDto(courseRepository.countByTitleContaining(search));
	}

	/**
	 * Fetches a single course, according to its id.
	 * 
	 * @param id An unique Integer used to identify each course.
	 * 
	 * @return CourseDto Returns a single course.
	 *
	 */

	@Override
	public CourseDto getById(long id) {
		Optional<Course> c = courseRepository.findById(id);
		if (c.isPresent())
			return courseMapper.courseToCourseDto(c.get());
		return null;
	}

	/**
	 * Delete a single course, according to its id.
	 * 
	 * @param id An unique Integer used to identify each course.
	 *
	 */

	@Override
	public void deleteById(long id) {
		courseRepository.deleteById(id);
	}

	/**
	 * Adds a new course or update an existing one.
	 * 
	 * @param courseDto An object representing a Course.
	 * 
	 * @return CourseDto Returns the newly created course or an updated one.
	 *
	 */

	@Override
	public CourseDto saveOrUpdate(CourseDto courseDto) {
		checkUniqness(courseDto);

		if (courseDto.getId() > 0 && !courseRepository.findById(courseDto.getId()).isPresent())
			return null;

		Course c = courseMapper.courseDtoToCouse(courseDto);

		c = courseRepository.saveAndFlush(c);
		return courseMapper.courseToCourseDto(c);
	}

	/**
	 * Verify if the course doesn't already exist in the database
	 * 
	 * @param CourseDto course that has to be verify with what already exists in the
	 *                  database
	 * @return boolean return true if course title doesn't exist in the database.
	 *         Otherwise send an Exception.
	 */
	@Override
	public boolean checkUniqness(CourseDto course) {
		Course duplicate = courseRepository.findByTitle(course.getId(), course.getTitle());

		if (duplicate != null) {
			Set<APIError> errors = new HashSet<>();
			String instanceClass = duplicate.getClass().toString();
			errors.add(new APIError(505, instanceClass, "Title Not Unique",
					"Course with title " + course.getTitle() + " already exists", "/api/courses"));

			throw new EntityFormatException(errors);
		}

		return true;
	}

	/**
	 * Fetches all courses in the Dawan API.
	 * 
	 * @param email A String defining a user's email.
	 * @param pwd A String defining a user's password.
	 * 
	 * @exception Exception Returns an exception if the request fails.
	 *
	 */
	
	@Override
	public void fetchAllDG2Courses(String email, String password) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<CourseDG2Dto> lResJson;

		String uri = "https://dawan.org/api2/planning/trainings";
		URI url = new URI(uri);

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + password);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
			String json = repWs.getBody();
			CourseDG2Dto[] resArray = objectMapper.readValue(json, CourseDG2Dto[].class);
			lResJson = Arrays.asList(resArray);
			for (CourseDG2Dto cDG2 : lResJson) {
				Course courseImport = courseMapper.courseDG2DtoToCourse(cDG2);
				Course course;
				Optional<Course> optCourse = courseRepository.findById(courseImport.getId());

				if (optCourse.isPresent() && optCourse.get().equals(courseImport)) {
					course = courseImport;
					courseRepository.saveAndFlush(course);
				}
			}
		}
	}

}
