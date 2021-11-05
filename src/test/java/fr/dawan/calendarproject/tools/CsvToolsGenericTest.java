package fr.dawan.calendarproject.tools;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import fr.dawan.calendarproject.entities.Course;

class CsvToolsGenericTest {
	
	private List<Course> courses = new ArrayList<Course>();
	
	@Value("${app.storagefolder}")
	private String storageFolder;
	
	@BeforeEach()
	public void beforeEach() throws Exception {	
		courses.add(new Course(1, "Java course for beginners", "5", 0));
		courses.add(new Course(2, "C# course for intermediate level", "1", 0));
		courses.add(new Course(3, "JavaScript course for beginners", "3", 0));
	}

	@Test
	void shouldThrowExceptionIfPathNoCorrect() {
		try {
			CsvToolsGeneric.toCsv("", courses, ";");
		   }
		   catch (Exception e) {
		     fail("Exception " + e);
		   }
	}
}
