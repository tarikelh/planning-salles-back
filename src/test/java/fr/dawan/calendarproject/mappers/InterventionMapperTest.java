package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.InterventionMementoMapper;

@SpringBootTest
@AutoConfigureMockMvc
class InterventionMapperTest {

	@Autowired
	private InterventionMementoMapper interventionMementoMapper;

	private Intervention intervention = new Intervention();
	private Intervention masterIntervention = new Intervention();
	private InterventionMementoDto interventionMementoDto = new InterventionMementoDto();
	private Location location = new Location();
	private Course course = new Course();
	private User user = new User();
	private Set<Skill> skills = new HashSet<Skill>();

	@BeforeEach
	void before() {
		location = new Location(1, "paris", "#32656", 1);
		course = new Course(1, "C#", "5", 2);

		skills.add(new Skill(1, "sql", null, 3));
		skills.add(new Skill(2, "c#", null, 4));
		skills.add(new Skill(3, "java", null, 5));

		user = new User(1, "firstname", "lastname", location, "areda@dawan.fr", "mdpdelux", skills,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "./image/img.png", 0);

		masterIntervention = new Intervention(3, "slug-3", "com", location, course, user, 0, InterventionStatus.INTERN,
				true, LocalDate.now(), LocalDate.now().plusDays(6), LocalTime.of(9, 0), LocalTime.of(17, 0), null, true,
				0);

		intervention = new Intervention(1, "slug-1", "com", location, course, user, 0, InterventionStatus.INTERN, true,
				LocalDate.now(), LocalDate.now().plusDays(4), LocalTime.of(9, 0), LocalTime.of(17, 0),
				masterIntervention, false, 0);

		interventionMementoDto = new InterventionMementoDto(0, "I am a new Intervention", 1, "Bordeaux", 1,
				"Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false);
	}

	@Test
	void should_map_interventionToInterventionMementoDto() {
		// mapping
		InterventionMementoDto mappedInterventionDto = interventionMementoMapper
				.interventionToInterventionMementoDto(intervention);

		// assert
		assertEquals(mappedInterventionDto.getInterventionId(), intervention.getId());
		assertEquals(mappedInterventionDto.getComment(), intervention.getComment());
		assertEquals(mappedInterventionDto.getLocationId(), intervention.getLocation().getId());
		assertEquals(mappedInterventionDto.getLocationCity(), intervention.getLocation().getCity());
		assertEquals(mappedInterventionDto.getCourseId(), intervention.getCourse().getId());
		assertEquals(mappedInterventionDto.getUserId(), intervention.getUser().getId());
		assertEquals(mappedInterventionDto.getUserFullName(), intervention.getUser().getFullname());
		assertEquals(mappedInterventionDto.getType(), intervention.getType().toString());
		assertEquals(mappedInterventionDto.getValidated(), intervention.isValidated());
		assertEquals(mappedInterventionDto.getDateStart(), intervention.getDateStart());
		assertEquals(mappedInterventionDto.getDateEnd(), intervention.getDateEnd());
		assertEquals(mappedInterventionDto.getTimeStart(), intervention.getTimeStart());
		assertEquals(mappedInterventionDto.getTimeEnd(), intervention.getTimeEnd());
		assertEquals(mappedInterventionDto.isMaster(), intervention.isMaster());
		assertEquals(mappedInterventionDto.getMasterInterventionId(), intervention.getMasterIntervention().getId());
	}

	@Test
	void should_map_interventionMementoDtoToIntervention() {
		// mapping
		Intervention mappedIntervention = interventionMementoMapper
				.interventionMementoDtoToIntervention(interventionMementoDto);

		// assert
		assertEquals(mappedIntervention.getId(), interventionMementoDto.getInterventionId());
		assertEquals(mappedIntervention.getComment(), interventionMementoDto.getComment());
		assertEquals(mappedIntervention.getLocation().getId(), interventionMementoDto.getLocationId());
		assertEquals(mappedIntervention.getCourse().getId(), interventionMementoDto.getCourseId());
		assertEquals(mappedIntervention.getUser().getId(), interventionMementoDto.getUserId());
		assertEquals(mappedIntervention.getType().toString(), interventionMementoDto.getType());
		assertEquals(mappedIntervention.isValidated(), interventionMementoDto.getValidated());
		assertEquals(mappedIntervention.getDateStart(), interventionMementoDto.getDateStart());
		assertEquals(mappedIntervention.getDateEnd(), interventionMementoDto.getDateEnd());
		assertEquals(mappedIntervention.getTimeStart(), interventionMementoDto.getTimeStart());
		assertEquals(mappedIntervention.getTimeEnd(), interventionMementoDto.getTimeEnd());
		assertEquals(mappedIntervention.isMaster(), interventionMementoDto.isMaster());
		assertEquals(mappedIntervention.getMasterIntervention().getId(),
				interventionMementoDto.getMasterInterventionId());
	}
}
