package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.CourseService;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CourseController courseController;

	@MockBean
	private CourseService courseService;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	private List<CourseDto> courses = new ArrayList<CourseDto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		courses.add(new CourseDto(1, 1, "Java", 5.00, "slug", 0));
		courses.add(new CourseDto(2, 2, ".Net", 5.00, "slug", 0));
		courses.add(new CourseDto(3, 3, "Android", 5.00, "slug", 0));
	}

	@Test
	void contextLoads() {
		assertThat(courseController).isNotNull();
	}

	@Test
	void shouldFetchAllCourses() throws Exception {
		when(courseService.getAllCourses()).thenReturn(courses);

		mockMvc.perform(get("/api/courses").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(courses.size()))).andExpect(jsonPath("$[0].title", is("Java")));
	}

	@Test
	void shouldFetchAllCoursesPagination() throws Exception {
		when(courseService.getAllCourses(any(int.class), any(int.class), any(String.class))).thenReturn(courses);

		mockMvc.perform(get("/api/courses/pagination?page=0&size=0&search=").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(courses.size())))
				.andExpect(jsonPath("$[0].title", is("Java")));
	}

	@Test
	void shouldFetchCourseById() throws Exception {
		final long courseId = 2;
		when(courseService.getById(courseId)).thenReturn(courses.get(1));

		mockMvc.perform(get("/api/courses/{id}", courseId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title", is(".Net")));
	}

	@Test
	void shouldReturn404WhenGetById() throws Exception {
		final long courseId = 10;
		when(courseService.getById(courseId)).thenReturn(null);

		mockMvc.perform(get("/api/courses/{id}", courseId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldDeleteCourseById() throws Exception {
		final long courseId = 1;
		doNothing().when(courseService).deleteById(courseId);

		String res = mockMvc.perform(delete("/api/courses/{id}", courseId)).andExpect(status().isAccepted()).andReturn()
				.getResponse().getContentAsString();

		assertEquals("Course with id " + courseId + " deleted", res);
	}

	@Test
	void shouldReturn404WhenDeleteById() throws Exception {
		final long courseId = 10;
		doThrow(IllegalArgumentException.class).when(courseService).deleteById(courseId);

		String res = mockMvc.perform(delete("/api/courses/{id}", courseId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Course with id " + courseId + " Not Found");
	}

	@Test
	void shouldCreateNewCourse() throws Exception {

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		CourseDto newCourse = new CourseDto(0, 0, "Photoshop", 5.00, "slug", 0);
		CourseDto resMock = new CourseDto(4, 4, "Photoshop", 5.00, "slug", 0);
		String newCourseJson = objectMapper.writeValueAsString(newCourse);

		when(courseService.saveOrUpdate(any(CourseDto.class))).thenReturn(resMock);

		mockMvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(newCourseJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(4))).andExpect(jsonPath("$.title", is(newCourse.getTitle())))
				.andExpect(jsonPath("$.version", is(newCourse.getVersion())));
	}

	@Test
	void shouldUpdateCourse() throws Exception {

		CourseDto updated = new CourseDto(courses.get(0).getId(), courses.get(0).getIdDg2(), courses.get(0).getTitle(),
				courses.get(0).getDuration(), courses.get(0).getSlug(), courses.get(0).getVersion());

		updated.setTitle("Java EE");

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String updatedJson = objectMapper.writeValueAsString(updated);

		when(courseService.saveOrUpdate(any(CourseDto.class))).thenReturn(updated);

		mockMvc.perform(put("/api/courses").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(updatedJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.title", is(updated.getTitle())))
				.andExpect(jsonPath("$.title", not(courses.get(0).getTitle())));
	}

	@Test
	void shouldReturn404WhenUpdateWithWrongId() throws Exception {

		CourseDto newCourse = new CourseDto(120, 120, "Photoshop", 5.00, "slug", 0);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String newCourseJson = objectMapper.writeValueAsString(newCourse);

		when(courseService.saveOrUpdate(newCourse)).thenReturn(null);

		String res = mockMvc.perform(put("/api/courses").contentType(MediaType.APPLICATION_JSON).content(newCourseJson))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Course with id " + newCourse.getId() + " Not Found");
	}
}
