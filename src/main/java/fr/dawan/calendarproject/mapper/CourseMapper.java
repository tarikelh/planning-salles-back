package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
	CourseDto courseToCourseDto(Course course);

	@Mapping(target = "slug", ignore = true)
	Course courseDtoToCouse(CourseDto courseDto);

	@Mapping(target = "id", source = "id")
	@Mapping(target = "title", source = "title")
	@Mapping(target = "duration", source = "duration")
	@Mapping(target = "version", ignore = true)
	Course courseDG2DtoToCourse(CourseDG2Dto courseDG2Dto);
}
