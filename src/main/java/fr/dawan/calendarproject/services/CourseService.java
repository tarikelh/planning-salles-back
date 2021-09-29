package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.CourseDto;

public interface CourseService {

	List<CourseDto> getAllCourses();
	List<CourseDto> getAllCourses(int page, int max, String search);
	CountDto count(String search);
	CourseDto getById(long id);
	void deleteById(long id);
	CourseDto saveOrUpdate(CourseDto course);
	boolean checkUniqness(CourseDto course);
	void fetchAllDG2Courses() throws Exception;
}
