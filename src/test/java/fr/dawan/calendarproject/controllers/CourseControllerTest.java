package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.services.CourseService;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	CourseController courseController;

	@MockBean
	CourseService courseService;
	
	@Autowired
	private ObjectMapper objectMapper;

	private List<CourseDto> courses = new ArrayList<CourseDto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		courses.add(new CourseDto(1, "Java", 0));
		courses.add(new CourseDto(2, ".Net", 0));
		courses.add(new CourseDto(3, "Android", 0));
	}

	@Test
	void contextLoads() {
		assertThat(courseController).isNotNull();
	}

	@Test
	void shouldFetchAllCourses() throws Exception {
		when(courseService.getAllCourses()).thenReturn(courses);

		mockMvc.perform(get("/api/courses")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(courses.size())))
				.andExpect(jsonPath("$[0].title", is("Java")));
	}

	@Test
	void shouldFetchCourseById() throws Exception {
		final long courseId = 2;
		when(courseService.getById(courseId)).thenReturn(courses.get(1));

		mockMvc.perform(get("/api/courses/{id}", courseId)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title", is(".Net")));
	}
	
	@Test
	void shouldReturn404WhenGetById() throws Exception {
		final long courseId = 10;
		when(courseService.getById(courseId)).thenReturn(null);

		mockMvc.perform(get("/api/courses/{id}", courseId)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldDeleteCourseById() throws Exception {
		final long courseId = 1;
		doNothing().when(courseService).deleteById(courseId);
		
		String res =  mockMvc.perform(delete("/api/courses/{id}", courseId))
		.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

		assertEquals("Course with id "+ courseId +" deleted", res);
	}
	
	@Test
	void shouldReturn404WhenDeleteById() throws Exception {
		final long courseId = 10;
		doThrow(IllegalArgumentException.class)
				.when(courseService).deleteById(courseId);

		String res = mockMvc.perform(delete("/api/courses/{id}", courseId)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals(res, "Course with id " + courseId + " Not Found");
	}

	@Test
	void shouldCreateNewUser() throws Exception {
		
		CourseDto newCourse = new CourseDto(0, "Photoshop", 0);
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String newCourseJson = objectMapper.writeValueAsString(newCourse);
		
		when(courseService.saveOrUpdate(newCourse)).thenReturn(new CourseDto(4, "Photoshop", 0));
		mockMvc.perform(post("/api/users")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
			.content(newCourseJson));
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}
}
