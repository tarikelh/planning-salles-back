package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.mapper.CourseMapper;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class CourseMapperTest {

	@Autowired
	CourseMapper courseMapper;

	Course course = new Course();
	CourseDto courseDto = new CourseDto();
	CourseDG2Dto courseDG2Dto = new CourseDG2Dto();

	@BeforeEach
	void before() {
		courseDto = new CourseDto(1, 1, "title", 5.00, "slug", 0);
		course = new Course(2, 2, "eltit", 5.00, "slug", 1);
		courseDG2Dto = new CourseDG2Dto("title", "5.00", "slug", 1);

	}

	@Test
	void should_map_courseToCourseDto() {
		// mapping
		CourseDto mappedCourseDto = courseMapper.courseToCourseDto(course);

		// assert
		assertEquals(mappedCourseDto.getId(), course.getId());
		assertEquals(mappedCourseDto.getTitle(), course.getTitle());
		assertEquals(mappedCourseDto.getDuration(), course.getDuration());
		assertEquals(mappedCourseDto.getSlug(), course.getSlug());
		assertEquals(mappedCourseDto.getVersion(), course.getVersion());
	}

	@Test
	void should_map_courseDtoToCouse() {
		// mapping
		Course mappedCourse = courseMapper.courseDtoToCourse(courseDto);

		// assert
		assertEquals(mappedCourse.getId(), courseDto.getId());
		assertEquals(mappedCourse.getTitle(), courseDto.getTitle());
		assertEquals(mappedCourse.getDuration(), courseDto.getDuration());
		assertEquals(mappedCourse.getSlug(), courseDto.getSlug());
		assertEquals(mappedCourse.getVersion(), courseDto.getVersion());
	}

	@Test
	void should_map_courseDG2DtoToCourse() {
		// mapping
		Course mappedCourse = courseMapper.courseDG2DtoToCourse(courseDG2Dto);

		// assert
		assertEquals(mappedCourse.getTitle(), courseDG2Dto.getTitle());
		assertEquals(mappedCourse.getDuration(), Double.parseDouble(courseDG2Dto.getDuration()));
		assertEquals(mappedCourse.getSlug(), courseDto.getSlug());
	}

}
