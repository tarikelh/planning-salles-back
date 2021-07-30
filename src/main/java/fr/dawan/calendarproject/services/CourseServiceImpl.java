package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.repositories.CourseRepository;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;

	@Override
	public List<CourseDto> getAllCourses() {
		List<Course> courses = courseRepository.findAll();

		// Solution 1 à la mano - conversion vers Dto
		List<CourseDto> result = new ArrayList<CourseDto>();
		for (Course c : courses) {
			result.add(DtoTools.convert(c, CourseDto.class));
		}

		return result;
	}

	@Override
	public List<CourseDto> getAllCourses(int page, int max) {
		List<Course> courses = courseRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<CourseDto> result = new ArrayList<CourseDto>();
		for (Course c : courses) {
			result.add(DtoTools.convert(c, CourseDto.class));
		}
		return result;
	}

	@Override
	public CourseDto getById(long id) {
		Optional<Course> c = courseRepository.findById(id);
		if (c.isPresent())
			return DtoTools.convert(c.get(), CourseDto.class);

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
		
		Course c = DtoTools.convert(courseDto, Course.class);

		c = courseRepository.saveAndFlush(c);
		return DtoTools.convert(c, CourseDto.class);
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
			errors.add(new APIError(505, instanceClass, "Title Not Unique", "Course with title " + course.getTitle() + " already exists", path));
			
			throw new EntityFormatException(errors);
		}
		
		return true;
	}

}
