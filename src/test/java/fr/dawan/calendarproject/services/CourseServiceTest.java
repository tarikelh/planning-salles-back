package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.CourseMapper;
import fr.dawan.calendarproject.repositories.CourseRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class CourseServiceTest {
	
	@Autowired
	private CourseService courseService;
	
	@MockBean
	private CourseMapper courseMapper;
	
	@MockBean
	private CourseRepository courseRepository;

	private List<Course> courses = new ArrayList<Course>();
	private List<CourseDto> cDtos = new ArrayList<CourseDto>();
	private CourseDG2Dto[] resArray = new CourseDG2Dto[3];
	private List<CourseDG2Dto> cDG2Dtos = new ArrayList<CourseDG2Dto>();
	
	@BeforeEach()
	public void beforeEach() throws Exception {	
		courses.add(new Course(1, "Java course for beginners", "5", 0));
		courses.add(new Course(2, "C# course for intermediate level", "1", 0));
		courses.add(new Course(3, "JavaScript course for beginners", "3", 0));
		
		cDtos.add(new CourseDto(1, "Java course for beginners", "5", 0));
		cDtos.add(new CourseDto(2, "C# course for intermediate level", "1", 0));
		cDtos.add(new CourseDto(3, "JavaScript course for beginners", "3", 0));
		
		resArray[0] = new CourseDG2Dto("Java course for beginners", "5", "java basic", "java-beginners", "java-beginners", "/formations/jee/java/Java-Beginners",
				1495, 875, 75, 1495, "Pouvoir réaliser des applications en Java - Savoir choisir les technologies adaptées et mettre en place des interfaces efficaces",
				"Notions de programmation");
		
		resArray[1] = new CourseDG2Dto("C# course for intermediate level", "1", "c# intermediate", "csharp-intermediate", "csharp-intermediate", "/formations/csharp/.net/Csharp-Intermediate",
				1685, 900, 90, 1685, "Pouvoir réaliser des applications en C# - Savoir choisir les technologies adaptées et mettre en place des interfaces efficaces",
				"Notions de programmation");
		
		resArray[2] = new CourseDG2Dto("JavaScript course for beginners", "3", "javascript basic", "javascript-intermediate", "javascript-intermediate", "/formations/web/js/Javascript-Intermediate",
				1329, 732, 60, 1329, "Pouvoir réaliser des applications web en javascript - Savoir choisir les technologies adaptées et mettre en place des interfaces efficaces",
				"Aucune");
		
		cDG2Dtos.add(new CourseDG2Dto("Java course for beginners", "5", "java basic", "java-beginners", "java-beginners", "/formations/jee/java/Java-Beginners",
				1495, 875, 75, 1495, "Pouvoir réaliser des applications en Java - Savoir choisir les technologies adaptées et mettre en place des interfaces efficaces",
				"Notions de programmation"));
		
		cDG2Dtos.add(new CourseDG2Dto("C# course for intermediate level", "1", "c# intermediate", "csharp-intermediate", "csharp-intermediate", "/formations/csharp/.net/Csharp-Intermediate",
				1685, 900, 90, 1685, "Pouvoir réaliser des applications en C# - Savoir choisir les technologies adaptées et mettre en place des interfaces efficaces",
				"Notions de programmation"));
		
		cDG2Dtos.add(new CourseDG2Dto("JavaScript course for beginners", "3", "javascript basic", "javascript-intermediate", "javascript-intermediate", "/formations/web/js/Javascript-Intermediate",
				1329, 732, 60, 1329, "Pouvoir réaliser des applications web en javascript - Savoir choisir les technologies adaptées et mettre en place des interfaces efficaces",
				"Aucune"));
		
	}
	
	@AfterEach
	public void afterEach() throws Exception {
		
	}
	
	@Test
	void contextLoads() {
		assertThat(courseService).isNotNull();
	}
	
	@Test
	void shouldGetCoursesAndReturnDtos() { 
		when(courseRepository.findAll()).thenReturn(courses);
		when(courseMapper.courseToCourseDto(courses.get(0))).thenReturn(cDtos.get(0));
		when(courseMapper.courseToCourseDto(courses.get(1))).thenReturn(cDtos.get(1));
		when(courseMapper.courseToCourseDto(courses.get(2))).thenReturn(cDtos.get(2));
		
		List<CourseDto> result = courseService.getAllCourses();

		assertThat(result).isNotNull();
		assertEquals(courses.size(), result.size());
		assertEquals(cDtos.size(), result.size());
		assertEquals(cDtos, result);
	}
	
	@Test
	void shouldGetPaginatedCoursesAndReturnDtos() {
		Page<Course> p1 = new PageImpl<Course>(courses.subList(0, 1));
		Page<Course> p2 = new PageImpl<Course>(courses.subList(0, 3));
		Page<Course> p3 = new PageImpl<Course>(courses.subList(1, 2));

		when(courseRepository.findAllByTitleContaining("Java", PageRequest.of(0, 3))).thenReturn(p1);
		when(courseRepository.findAllByTitleContaining("course", PageRequest.of(0, 3))).thenReturn(p2);
		when(courseRepository.findAllByTitleContaining("", PageRequest.of(1, 1))).thenReturn(p3);
		when(courseRepository.findAllByTitleContaining("", Pageable.unpaged())).thenReturn(p2);
		when(courseRepository.findAllByTitleContaining("Java", Pageable.unpaged())).thenReturn(p1);
		
		when(courseMapper.courseToCourseDto(courses.get(0))).thenReturn(cDtos.get(0));
		when(courseMapper.courseToCourseDto(courses.get(1))).thenReturn(cDtos.get(1));
		when(courseMapper.courseToCourseDto(courses.get(2))).thenReturn(cDtos.get(2));

		List<CourseDto> page1 = courseService.getAllCourses(0, 3, "Java");
		List<CourseDto> page2 = courseService.getAllCourses(0, 3, "course");
		List<CourseDto> page3 = courseService.getAllCourses(1, 1, "");
		List<CourseDto> page4 = courseService.getAllCourses(-1, -1, "");
		List<CourseDto> page5 = courseService.getAllCourses(-1, -1, "Java");

		assertThat(page1).isNotNull();
		assertThat(page2).isNotNull();
		assertThat(page3).isNotNull();
		assertThat(page4).isNotNull();
		assertThat(page5).isNotNull();
		assertEquals(page1.size(), 1);
		assertEquals(page2.size(), 3);
		assertEquals(page3.size(), 1);
		assertEquals(page4.size(), 3);
		assertEquals(page5.size(), 1);
		assertEquals(page1, cDtos.subList(0, 1));
		assertEquals(page2, cDtos.subList(0, 3));
		assertEquals(page3, cDtos.subList(1, 2));
		assertEquals(page4, cDtos.subList(0, 3));
		assertEquals(page5, cDtos.subList(0, 1));
	}
	
	@Test
	void shouldGetInterventionById() {
		long intervId = 2;
		
		CourseDto expected = cDtos.get(1);

		when(courseRepository.findById(intervId)).thenReturn(Optional.of(courses.get(1)));
		when(courseMapper.courseToCourseDto(courses.get(1))).thenReturn(cDtos.get(1));
		
		CourseDto result = courseService.getById(intervId);

		assertThat(result).isNotNull();
		assertEquals(result, expected);
		assertEquals(result.getId(), intervId);
	}
	
	@Test
	void shouldReturnNullWhenGetByIdWhenWrongIdProvided() {
		long intervId = -15;

		when(courseRepository.findById(intervId)).thenReturn(Optional.empty());

		assertThat(courseService.getById(intervId)).isNull();
	}
	
	@Test
	void shouldSaveNewCourse() throws Exception {
		Course newCourse = new Course(0, "Vue.js course for beginners", "5", 0);
		CourseDto newCourseDto = new CourseDto(0, "Vue.js course for beginners", "5", 0);
		Course savedCourse = new Course(4, "Vue.js course for beginners", "5", 0);
		CourseDto expectedCourse = new CourseDto(4, "Vue.js course for beginners", "5", 0);

		when(courseMapper.courseDtoToCouse(newCourseDto)).thenReturn(newCourse);
		when(courseRepository.saveAndFlush(newCourse)).thenReturn(savedCourse);
		when(courseMapper.courseToCourseDto(savedCourse)).thenReturn(expectedCourse);
		
		
		CourseDto result = courseService.saveOrUpdate(newCourseDto);

		assertThat(result).isNotNull();
		assertEquals(result, expectedCourse);
		assertEquals(result.getId(), 4);
	}
	
	@Test
	void shouldUpdateCourse() throws Exception {
		Course beforeUpdateCourse = new Course(3, "JavaScript course for beginners", "3", 0);
		Course updatedCourse = new Course(3, "JavaScript course for beginners Updated", "5", 0);
		CourseDto updatedCourseDto = new CourseDto(3, "JavaScript course for beginners Updated", "5", 0);
		Course savedCourse = new Course(3, "Vue.js course for beginners", "5", 1);
		CourseDto expectedCourse = new CourseDto(3, "Vue.js course for beginners", "5", 1);

		when(courseRepository.findById(updatedCourse.getId())).thenReturn(Optional.of(beforeUpdateCourse));
		when(courseMapper.courseDtoToCouse(updatedCourseDto)).thenReturn(updatedCourse);
		when(courseRepository.saveAndFlush(updatedCourse)).thenReturn(savedCourse);
		when(courseMapper.courseToCourseDto(savedCourse)).thenReturn(expectedCourse);
		
		CourseDto result = courseService.saveOrUpdate(updatedCourseDto);

		assertThat(result).isNotNull();
		assertEquals(result, expectedCourse);
		assertEquals(result.getVersion(), 1);
	}
	
	@Test
	void shouldReturnNullWhenUpdateUnknownId() throws Exception {
		CourseDto courseDto = cDtos.get(2);
		courseDto.setId(4);

		when(courseRepository.existsById(4L)).thenReturn(false);

		assertThat(courseService.saveOrUpdate(courseDto)).isNull();
	}
	
	@Test
	void shouldReturnExceptionWhenCourseExisting() throws Exception {
		Course course = courses.get(0);
		CourseDto newCourseDto = new CourseDto(1, "Java course for beginners", "5", 0);

		when(courseRepository.findByTitle(newCourseDto.getId(), newCourseDto.getTitle())).thenReturn(course);

		assertThrows(EntityFormatException.class, () -> {
			courseService.checkUniqness(newCourseDto);
		});
	}
	
	@Test
	void shouldReturnCount() throws Exception {
		String search = "Java";
		CountDto expectedCount = new CountDto(1);
		
		when(courseRepository.countByTitleContaining(search))
				.thenReturn(1L);

		CountDto result = courseService.count(search);
		
		assertThat(result).isNotNull();
		assertEquals(expectedCount.getNb(), result.getNb());
	}
	
	@Test
	void shouldDeleteCourse() throws Exception {

		doNothing().when(courseRepository).deleteById(any(Long.class));
		
		assertDoesNotThrow( () -> courseService.deleteById(any(Long.class)));	
	}
	
	@Test
	void shouldConnectToCourseDG2() throws Exception { 
		RestTemplate restTemplate = new RestTemplate();
		
		URI url = new URI("https://dawan.org/public/training/");
		ResponseEntity<String> repWs = restTemplate.getForEntity(url, String.class);
		
		//Verify request succeed
	    assertEquals(200, repWs.getStatusCodeValue());
	}
	
	@Test
	void shouldGetCoursesFromDG2() throws Exception {
		
		when(courseMapper.courseDG2DtoToCourse(any(CourseDG2Dto.class))).thenReturn(courses.get(0));
		when(courseRepository.findByTitleAndDuration(courses.get(0).getTitle(), courses.get(0).getDuration())).thenReturn(courses.get(0));
		when(courseRepository.saveAndFlush(courses.get(0))).thenReturn(courses.get(0));
		
		assertDoesNotThrow( () -> courseService.fetchAllDG2Courses());
	}
}
