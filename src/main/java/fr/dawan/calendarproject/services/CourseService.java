package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;

public interface CourseService {

	List<CourseDto> getAllCourses();
	List<CourseDto> getAllCourses(int page, int max);
	CourseDto getById(long id);
	void deleteById(long id);
	CourseDto saveOrUpdate(CourseDto course);
	boolean checkUniqness(CourseDto course);
	
	Course getEntityById(long id);
}
