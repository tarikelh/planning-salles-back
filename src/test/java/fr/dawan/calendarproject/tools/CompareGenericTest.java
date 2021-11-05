package fr.dawan.calendarproject.tools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;

class CompareGenericTest {
	
	private List<Course> courses = new ArrayList<Course>();
	
	@BeforeEach()
	public void beforeEach() throws Exception {	
		courses.add(new Course(1, "Java course for beginners", "5", 0));
		courses.add(new Course(2, "C# course for intermediate level", "1", 0));
		courses.add(new Course(3, "JavaScript course for beginners", "3", 0));	
	}

	@Test
	void shouldCompareTwoObjects() throws Exception {
		Course newCourse1 = new Course(1, "Java course for beginners Updated", "5", 0);
		Course newCourse2 = new Course(1, "Java course for beginners", "3", 0);
		Course newCourse3 = new Course(1, "Java course for beginners Updated", "3", 0);
		
		String result1 = CompareGeneric.compareObjects(newCourse1, courses.get(0));
		String result2 = CompareGeneric.compareObjects(newCourse2, courses.get(0));
		String result3 = CompareGeneric.compareObjects(newCourse3, courses.get(0));
		
		assertEquals(result1, "title / ");
		assertEquals(result2, "duration / ");
		assertEquals(result3, "title / duration / ");
	}
	
	@Test
	void shouldReturnNullIfNotSameObject() throws Exception {
		Location newLocation = new Location(1L, "Bordeaux", "#FFFFF", 0);
		
		String result = CompareGeneric.compareObjects(newLocation, courses.get(0));
		
		assertThat(result).isNull();
	}

}
