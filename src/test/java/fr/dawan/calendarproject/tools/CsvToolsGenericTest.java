package fr.dawan.calendarproject.tools;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.mapper.CourseMapper;

class CsvToolsGenericTest {

	private List<Course> courses = new ArrayList<Course>();

	@MockBean
	private CourseMapper courseMapper;
	
	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	private String pathExist;
	private String pathDoesNotExist;
	
	private BufferedWriter bw;

	@BeforeEach()
	public void beforeEach() throws Exception {
		courses.add(new Course(1, "Java course for beginners", "5", 0));
		courses.add(new Course(2, "C# course for intermediate level", "1", 0));
		courses.add(new Course(3, "JavaScript course for beginners", "3", 0));

		pathExist = "C:/Users/Admin-Stagiaire/Documents/Dawan/DawanPlanning/api/DaCalendar/coursesCsvTest.csv";
		pathDoesNotExist = "";
	}

	@Test
	void shouldThrowExceptionIfPathNoCorrect() {
		
		assertThrows(Exception.class, () -> {
			CsvToolsGeneric.toCsv(pathDoesNotExist, courses, ";");
		});
	}
}
