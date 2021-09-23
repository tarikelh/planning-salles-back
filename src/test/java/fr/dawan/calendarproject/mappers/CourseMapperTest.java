package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.mapper.CourseMapper;

@SpringBootTest
@AutoConfigureMockMvc
class CourseMapperTest {

	@Autowired
	CourseMapper courseMapper;

	Course course = new Course();
	CourseDto courseDto = new CourseDto();

	@BeforeEach
	void before() {
		courseDto = new CourseDto(1, "title", "5", 3);
		course = new Course(3, "eltit", "5", 1);
	}

	@Test
	void should_map_courseToCourseDto() {
		// mapping
		CourseDto mappedCourseDto = courseMapper.courseToCourseDto(course);

		// assert
		assertEquals(mappedCourseDto.getId(), course.getId());
		assertEquals(mappedCourseDto.getTitle(), course.getTitle());
		assertEquals(mappedCourseDto.getVersion(), course.getVersion());
	}

	@Test
	void should_map_courseDtoToCouse() {
		// mapping
		Course mappedCourse = courseMapper.courseDtoToCouse(courseDto);

		// assert
		assertEquals(mappedCourse.getId(), courseDto.getId());
		assertEquals(mappedCourse.getTitle(), courseDto.getTitle());
		assertEquals(mappedCourse.getVersion(), courseDto.getVersion());
	}

}
