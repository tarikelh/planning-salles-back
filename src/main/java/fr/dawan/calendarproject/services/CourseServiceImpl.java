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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.APIError;
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

	@Override
	public List<CourseDto> getAllCourses() {
		List<Course> courses = courseRepository.findAll();

		List<CourseDto> result = new ArrayList<CourseDto>();
		for (Course c : courses) {
			result.add(courseMapper.courseToCourseDto(c));
		}

		return result;
	}

	@Override
	public List<CourseDto> getAllCourses(int page, int max) {
		List<Course> courses = courseRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<CourseDto> result = new ArrayList<CourseDto>();
		for (Course c : courses) {
			result.add(courseMapper.courseToCourseDto(c));
		}
		return result;
	}

	@Override
	public CourseDto getById(long id) {
		Optional<Course> c = courseRepository.findById(id);
		if (c.isPresent())
			return courseMapper.courseToCourseDto(c.get());
		return null;
	}

	@Override
	public void deleteById(long id) {
		courseRepository.deleteById(id);
	}

	@Override
	public CourseDto saveOrUpdate(CourseDto courseDto) {
		checkUniqness(courseDto);

		if (courseDto.getId() > 0 && !courseRepository.findById(courseDto.getId()).isPresent())
			return null;

		Course c = courseMapper.courseDtoToCouse(courseDto);

		c = courseRepository.saveAndFlush(c);
		return courseMapper.courseToCourseDto(c);
	}

	@Override
	public CourseDto count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkUniqness(CourseDto course) {
		Course duplicate = courseRepository.findByTitle(course.getId(), course.getTitle());

		if (duplicate != null) {
			Set<APIError> errors = new HashSet<APIError>();
			String instanceClass = duplicate.getClass().toString();
			String path = "/api/courses";
			errors.add(new APIError(505, instanceClass, "Title Not Unique",
					"Course with title " + course.getTitle() + " already exists", path));

			throw new EntityFormatException(errors);
		}

		return true;
	}

	@Override
	public List<CourseDto> fetchAllDG2Courses() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		List<CourseDG2Dto> lResJson = new ArrayList<CourseDG2Dto>();
		
		URI url = new URI("https://dawan.org/public/training/");
		ResponseEntity<String> repWs = restTemplate.getForEntity(url, String.class);
		
		if(repWs.getStatusCode()==HttpStatus.OK) {
			String json = repWs.getBody();
			CourseDG2Dto[] resArray = objectMapper.readValue(json, CourseDG2Dto[].class);
			lResJson = Arrays.asList(resArray);
			for (CourseDG2Dto cDG2 : lResJson) {
				Course c = courseMapper.courseDG2DtoToCourse(cDG2);
				Course foundC = courseRepository.findByTitleAndDuration(c.getTitle(), c.getDuration());
				if(foundC != null) {
					c.setId(foundC.getId());
				}
				courseRepository.saveAndFlush(c);
			}
		}
		// To improve with Pagination
		return getAllCourses();
	}

}
