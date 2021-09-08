package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;

import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
	CourseDto courseToCourseDto(Course course);

	Course courseDtoToCouse(CourseDto courseDto);
}
