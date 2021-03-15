package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.CourseDto;

public interface CourseService {

	List<CourseDto> getAllCourses();
	List<CourseDto> getAllCourses(int page, int max);
	CourseDto getById(long id);
	void deleteById(long id);
	CourseDto saveOrUpdate(CourseDto course);
	CourseDto count();
	
}
