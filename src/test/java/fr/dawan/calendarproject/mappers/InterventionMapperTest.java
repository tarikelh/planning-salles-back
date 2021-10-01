package fr.dawan.calendarproject.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.mapper.InterventionMapper;
import fr.dawan.calendarproject.services.InterventionService;

@SpringBootTest
@AutoConfigureMockMvc
class InterventionMapperTest {

	@Autowired
	InterventionMapper interventionMapper;

	@MockBean
	InterventionService interventionService;

	Intervention intervention = new Intervention();
	Location location = new Location();
	Course course = new Course();
	User user = new User();
//	private Set<Skill> skills = new HashSet<>();

//	@BeforeEach
//	void before() {
//		location = new Location(1, "paris", "#32656", 0);
//		course = new Course(1, "C#", 0);
//
//		skills.add(new Skill(1, "sql", null, 0));
//		skills.add(new Skill(2, "c#", null, 0));
//		skills.add(new Skill(3, "java", null, 0));
//
//		user = new User(1, "ahmed", "reda", location, "areda@dawan.fr", "mdpdelux", skills, UserType.ADMINISTRATIF,
//				UserCompany.DAWAN, "./image/img.png", 0);
//
//		intervention = new Intervention(1, "comente", location, course, user, UserType.ADMINISTRATIF, true,
//				LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, null, 0);
//	}
//
//	@Test
//	void should_map_interventionToInterventionDto() {
//		when(interventionService.getById(any(Long.class))).thenReturn(intervention);
//
//		InterventionDto mappedInterventionDto = interventionMapper.interventionToInterventionDto(intervention);
//
//	}
//
//	@Test
//	void should_map_interventionDtoToIntervention() {
//		fail("Not yet implemented");
//	}
}
