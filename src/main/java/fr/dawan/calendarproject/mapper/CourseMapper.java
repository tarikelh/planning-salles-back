package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
	CourseDto courseToCourseDto(Course course);

	Course courseDtoToCouse(CourseDto courseDto);
	
	@Mappings({ @Mapping(target = "title", source = "title"), 
				@Mapping(target = "id", ignore = true),
				@Mapping(target = "version", ignore = true)})
	Course courseDG2DtoToCourse(CourseDG2Dto courseDG2Dto);
}
