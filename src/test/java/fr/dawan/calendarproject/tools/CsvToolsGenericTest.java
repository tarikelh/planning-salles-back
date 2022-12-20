package fr.dawan.calendarproject.tools;

import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.mapper.CourseMapper;

class CsvToolsGenericTest {

	private List<Course> courses = new ArrayList<Course>();

	@MockBean
	private CourseMapper courseMapper;

	private String pathDoesNotExist;

	@BeforeEach()
	public void beforeEach() throws Exception {
		courses.add(new Course(1, 1, "Java course for beginners", 5.00, "slug", 0));
		courses.add(new Course(2, 2, "C# course for intermediate level", 1.00, "slug", 0));
		courses.add(new Course(3, 3, "JavaScript course for beginners", 3.00, "slug", 0));

		pathDoesNotExist = "";
	}

	@Test
	void shouldThrowExceptionIfPathNoCorrect() {

		assertThrows(Exception.class, () -> {
			CsvToolsGeneric.toCsv(pathDoesNotExist, courses, ";");
		});
	}
}
