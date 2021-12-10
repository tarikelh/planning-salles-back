package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.CourseDG2Dto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.entities.Course;
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

	@MockBean
	RestTemplate restTemplate;

	private List<Course> courses = new ArrayList<Course>();
	private List<CourseDto> cDtos = new ArrayList<CourseDto>();
	private CourseDG2Dto[] resArray = new CourseDG2Dto[3];
	private List<CourseDG2Dto> cDG2Dtos = new ArrayList<CourseDG2Dto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		courses.add(new Course(1, "Java course for beginners", "5", "slug", 0));
		courses.add(new Course(2, "C# course for intermediate level", "slug", "1", 0));
		courses.add(new Course(3, "JavaScript course for beginners", "slug", "3", 0));

		cDtos.add(new CourseDto(1, "Java course for beginners", "5", "slug", 0));
		cDtos.add(new CourseDto(2, "C# course for intermediate level", "1", "slug", 0));
		cDtos.add(new CourseDto(3, "JavaScript course for beginners", "3", "slug", 0));

		resArray[0] = new CourseDG2Dto("Java course for beginners", "5", "java basic", 1);

		resArray[1] = new CourseDG2Dto("C# course for intermediate level", "1", "c# intermediate", 2);

		resArray[2] = new CourseDG2Dto("JavaScript course for beginners", "3", "javascript basic", 13);

		cDG2Dtos.add(new CourseDG2Dto("Java course for beginners", "5", "java basic", 4));

		cDG2Dtos.add(new CourseDG2Dto("C# course for intermediate level", "1", "c# intermediate", 8));

		cDG2Dtos.add(new CourseDG2Dto("JavaScript course for beginners", "3", "javascript basic", 5));

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
		assertEquals(1, page1.size());
		assertEquals(3, page2.size());
		assertEquals(1, page3.size());
		assertEquals(3, page4.size());
		assertEquals(1, page5.size());
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
		Course newCourse = new Course(0, "Vue.js course for beginners", "5", "slug", 0);
		CourseDto newCourseDto = new CourseDto(0, "Vue.js course for beginners", "5", "slug", 0);
		Course savedCourse = new Course(4, "Vue.js course for beginners", "5", "slug", 0);
		CourseDto expectedCourse = new CourseDto(4, "Vue.js course for beginners", "5", "slug", 0);

		when(courseMapper.courseDtoToCouse(newCourseDto)).thenReturn(newCourse);
		when(courseRepository.saveAndFlush(newCourse)).thenReturn(savedCourse);
		when(courseMapper.courseToCourseDto(savedCourse)).thenReturn(expectedCourse);

		CourseDto result = courseService.saveOrUpdate(newCourseDto);

		assertThat(result).isNotNull();
		assertEquals(result, expectedCourse);
		assertEquals(4, result.getId());
	}

	@Test
	void shouldUpdateCourse() throws Exception {
		Course beforeUpdateCourse = new Course(3, "JavaScript course for beginners", "3", "slug", 0);
		Course updatedCourse = new Course(3, "JavaScript course for beginners Updated", "5", "slug", 0);
		CourseDto updatedCourseDto = new CourseDto(3, "JavaScript course for beginners Updated", "5", "slug", 0);
		Course savedCourse = new Course(3, "Vue.js course for beginners", "5", "slug", 1);
		CourseDto expectedCourse = new CourseDto(3, "Vue.js course for beginners", "5", "slug", 1);

		when(courseRepository.findById(updatedCourse.getId())).thenReturn(Optional.of(beforeUpdateCourse));
		when(courseMapper.courseDtoToCouse(updatedCourseDto)).thenReturn(updatedCourse);
		when(courseRepository.saveAndFlush(updatedCourse)).thenReturn(savedCourse);
		when(courseMapper.courseToCourseDto(savedCourse)).thenReturn(expectedCourse);

		CourseDto result = courseService.saveOrUpdate(updatedCourseDto);

		assertThat(result).isNotNull();
		assertEquals(result, expectedCourse);
		assertEquals(1, result.getVersion());
	}

	@Test
	void shouldReturnNullWhenUpdateUnknownId() throws Exception {
		CourseDto courseDto = cDtos.get(2);
		courseDto.setId(4);

		when(courseRepository.existsById(4L)).thenReturn(false);

		assertThat(courseService.saveOrUpdate(courseDto)).isNull();
	}

	@Test
	void shouldReturnCount() throws Exception {
		String search = "Java";
		CountDto expectedCount = new CountDto(1);

		when(courseRepository.countByTitleContaining(search)).thenReturn(1L);

		CountDto result = courseService.count(search);

		assertThat(result).isNotNull();
		assertEquals(expectedCount.getNb(), result.getNb());
	}

	@Test
	void shouldDeleteCourse() throws Exception {

		doNothing().when(courseRepository).deleteById(any(Long.class));

		assertDoesNotThrow(() -> courseService.deleteById(any(Long.class)));
	}

	@Test
	void shouldConnectToCourseDG2() throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		URI url = new URI("https://dawan.org/public/training/");
		ResponseEntity<String> repWs = restTemplate.getForEntity(url, String.class);

		// Verify request succeed
		assertEquals(200, repWs.getStatusCodeValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	void shouldGetCoursesFromDG2() throws Exception {
		String body = "[{\"id\":\"10\",\"duration\":\"3.00\",\"title\":\"HTML\\/CSS : Fondamentaux\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"11\",\"duration\":\"2.00\",\"title\":\"JavaScript\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"12\",\"duration\":\"5.00\",\"title\":\"HTML\\/CSS : Fondamentaux + JavaScript\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"15\",\"duration\":\"2.00\",\"title\":\"Swish\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"18\",\"duration\":\"2.00\",\"title\":\"E-marketing : Fondamentaux\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"19\",\"duration\":\"1.00\",\"title\":\"Référencement naturel \\/ SEO\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"20\",\"duration\":\"3.00\",\"title\":\"XML\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"22\",\"duration\":\"2.00\",\"title\":\"XSLT\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"24\",\"duration\":\"2.00\",\"title\":\"PHP et XML\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"25\",\"duration\":\"3.00\",\"title\":\"Java SE Initiation\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"26\",\"duration\":\"2.00\",\"title\":\"Java SE Approfondissement\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"27\",\"duration\":\"3.00\",\"title\":\"Java et XML\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"28\",\"duration\":\"5.00\",\"title\":\"Java SE Initiation + Approfondissement\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"38\",\"duration\":\"3.00\",\"title\":\"Samba \\/ LDAP\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"39\",\"duration\":\"2.00\",\"title\":\"Cluster Linux\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"40\",\"duration\":\"3.00\",\"title\":\"Sécurité Linux\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"41\",\"duration\":\"3.00\",\"title\":\"MySQL, mise en oeuvre et administration\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"42\",\"duration\":\"3.00\",\"title\":\"MySQL et PostgreSQL\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"43\",\"duration\":\"5.00\",\"title\":\"Linux Administration : Bases + Services\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"44\",\"duration\":\"3.00\",\"title\":\"Linux Administration : Bases\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"45\",\"duration\":\"3.00\",\"title\":\"Linux : Initiation à  lutilisation\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"46\",\"duration\":\"3.00\",\"title\":\"Programmation Shell\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"47\",\"duration\":\"3.00\",\"title\":\"Point Acces à  Internet, mise en oeuvre\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"49\",\"duration\":\"3.00\",\"title\":\"SMTP Linux\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"50\",\"duration\":\"2.00\",\"title\":\"Bind 9\",\"version\":\"0\"},\r\n"
				+ "{\"id\":\"51\",\"duration\":\"3.00\",\"title\":\"Apache\",\"version\":\"0\"}]";
		ResponseEntity<String> res = new ResponseEntity<String>(body, HttpStatus.OK);
		when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
				.thenReturn(res);
		when(courseMapper.courseDG2DtoToCourse(any(CourseDG2Dto.class))).thenReturn(courses.get(0));
		when(courseRepository.findByTitleAndDuration(courses.get(0).getTitle(), courses.get(0).getDuration()))
				.thenReturn(courses.get(0));
		when(courseRepository.saveAndFlush(courses.get(0))).thenReturn(courses.get(0));

		assertDoesNotThrow(() -> courseService.fetchAllDG2Courses("emailDG2", "passwordDG2"));
	}
}
