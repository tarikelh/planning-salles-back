package fr.dawan.calendarproject.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.dto.InterventionDG2Dto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.InterventionMapper;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class InterventionMapperTest {

	@Autowired
	private InterventionMapper interventionMapper;

	@MockBean
	CourseRepository courseRepository;

	@MockBean
	LocationRepository locationRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	InterventionRepository interventionRepository;

	private Intervention intervention = new Intervention();
	private Intervention masterIntervention = new Intervention();
	private InterventionDto interventionDto = new InterventionDto();
	private InterventionDG2Dto interventionDG2Dto = new InterventionDG2Dto();
	private Intervention intervention2 = new Intervention();
	private InterventionDto interventionDto2 = new InterventionDto();
	private Location location = new Location();
	private Course course = new Course();
	private User user = new User();
	private Set<Skill> skills = new HashSet<Skill>();
	private List<Intervention> interventionList = new ArrayList<Intervention>();
	private List<InterventionDto> interventionDtoList = new ArrayList<InterventionDto>();

	@BeforeEach
	void before() {
		location = new Location(1, "paris", "#32656", 1);
		course = new Course(1, 1, "C#", "5", "slug", 2);

		skills.add(new Skill(1, "sql", null, 3));
		skills.add(new Skill(2, "c#", null, 4));
		skills.add(new Skill(3, "java", null, 5));

		user = new User(1, 1, "firstname", "lastname", location, "areda@dawan.fr", "mdpdelux", skills,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "./image/img.png", 0);

		masterIntervention = new Intervention(5, 5, "slug-3", "com", location, course, user, 0, InterventionStatus.INTERN,
				true, LocalDate.now(), LocalDate.now().plusDays(6), LocalTime.of(9, 0), LocalTime.of(17, 0), null, true,
				0);

		intervention = new Intervention(1, 1, "slug-1", "com", location, course, user, 0, InterventionStatus.INTERN, true,
				LocalDate.now(), LocalDate.now().plusDays(4), LocalTime.of(9, 0), LocalTime.of(17, 0),
				masterIntervention, false, 0);

		intervention2 = new Intervention(7, 7, "slug-7", "com7", location, course, user, 0, InterventionStatus.SUR_MESURE,
				false, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0),
				masterIntervention, false, 0);

		interventionDto = new InterventionDto(5, 5, "slug-5", "coms5", location.getId(), course.getId(), user.getId(), 0,
				"SUR_MESURE", true, LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, false, 0);

		interventionDto2 = new InterventionDto(2, 2, "slug-2", "coms", location.getId(), course.getId(), user.getId(), 0,
				"INTERN", true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0),
				0, false, 0);

		interventionDG2Dto = new InterventionDG2Dto(35, location.getId(), course.getId(), user.getId(),
				LocalDate.now().toString(), LocalDate.now().plusDays(4).toString(), "slug-1", "INTERN", true,
				masterIntervention.getId(), false, 5);

		interventionList.add(intervention);
		interventionList.add(intervention2);
		interventionDtoList.add(interventionDto);
		interventionDtoList.add(interventionDto2);
	}

	@Test
	void should_map_interventionToInterventionDto() {
		// mapping
		InterventionDto mappedInterventionDto = interventionMapper.interventionToInterventionDto(intervention);

		// assert
		assertEquals(mappedInterventionDto.getId(), intervention.getId());
		assertEquals(mappedInterventionDto.getComment(), intervention.getComment());
		assertEquals(mappedInterventionDto.getLocationId(), intervention.getLocation().getId());
		assertEquals(mappedInterventionDto.getCourseId(), intervention.getCourse().getId());
		assertEquals(mappedInterventionDto.getUserId(), intervention.getUser().getId());
		assertEquals(mappedInterventionDto.getType(), intervention.getType().toString());
		assertEquals(mappedInterventionDto.isValidated(), intervention.isValidated());
		assertEquals(mappedInterventionDto.getDateStart(), intervention.getDateStart());
		assertEquals(mappedInterventionDto.getDateEnd(), intervention.getDateEnd());
		assertEquals(mappedInterventionDto.getTimeStart(), intervention.getTimeStart());
		assertEquals(mappedInterventionDto.getTimeEnd(), intervention.getTimeEnd());
		assertEquals(mappedInterventionDto.isMaster(), intervention.isMaster());
		assertEquals(mappedInterventionDto.getMasterInterventionId(), intervention.getMasterIntervention().getId());
	}

	@Test
	void should_map_interventionDtoToIntervention() {
		// mocking
		when(courseRepository.getOne(course.getId())).thenReturn(course);
		when(locationRepository.getOne(location.getId())).thenReturn(location);
		when(userRepository.getOne(user.getId())).thenReturn(user);
		when(interventionRepository.getOne(interventionDto2.getId())).thenReturn(intervention2);
		when(interventionRepository.getOne(0L)).thenReturn(null);
		// mapping
		Intervention mappedIntervention = interventionMapper.interventionDtoToIntervention(interventionDto);

		// assert
		assertEquals(mappedIntervention.getId(), interventionDto.getId());
		assertEquals(mappedIntervention.getComment(), interventionDto.getComment());
		assertEquals(mappedIntervention.getLocation().getId(), interventionDto.getLocationId());
		assertEquals(mappedIntervention.getCourse().getId(), interventionDto.getCourseId());
		assertEquals(mappedIntervention.getUser().getId(), interventionDto.getUserId());
		assertEquals(mappedIntervention.getType().toString(), interventionDto.getType().toString());
		assertEquals(mappedIntervention.isValidated(), interventionDto.isValidated());
		assertEquals(mappedIntervention.getDateStart(), interventionDto.getDateStart());
		assertEquals(mappedIntervention.getDateEnd(), interventionDto.getDateEnd());
		assertEquals(mappedIntervention.getTimeStart(), interventionDto.getTimeStart());
		assertEquals(mappedIntervention.getTimeEnd(), interventionDto.getTimeEnd());
		assertEquals(mappedIntervention.isMaster(), interventionDto.isMaster());
		assertEquals(null, mappedIntervention.getMasterIntervention());
	}

	@Test
	void should_map_listInterventionToListInterventionDto() {
		// mapping
		List<InterventionDto> mappedInterventionDtoList = interventionMapper
				.listInterventionToListInterventionDto(interventionList);

		// assert
		assertEquals(mappedInterventionDtoList.size(), interventionList.size());
		assertThat(mappedInterventionDtoList.contains(interventionDto)).isFalse();
		assertThat(mappedInterventionDtoList.contains(interventionDto2)).isFalse();
	}

	@Test
	void should_map_listInterventionDtoToListIntervention() {
		// mapping
		List<Intervention> mappedInterventionList = interventionMapper
				.listInterventionDtoToListIntervention(interventionDtoList);

		// assert
		assertEquals(mappedInterventionList.size(), interventionDtoList.size());
		assertThat(mappedInterventionList.contains(intervention)).isFalse();
		assertThat(mappedInterventionList.contains(intervention2)).isFalse();
	}

	@Test
	void should_map_interventionDG2DtoToIntervention() {
		// mocking
		when(courseRepository.getOne(course.getId())).thenReturn(course);
		when(locationRepository.getOne(location.getId())).thenReturn(location);
		when(userRepository.getOne(user.getId())).thenReturn(user);
		when(interventionRepository.getOne(interventionDto2.getId())).thenReturn(intervention2);
		when(interventionRepository.getOne(0L)).thenReturn(null);

		// mapping
		Intervention mappedIntervention = interventionMapper.interventionDG2DtoToIntervention(interventionDG2Dto);

		// assert
		assertEquals(mappedIntervention.getId(), interventionDG2Dto.getId());
//		assertEquals(mappedIntervention.getLocation().getId(), interventionDG2Dto.getLocationId());
//		assertEquals(mappedIntervention.getCourse().getId(), interventionDG2Dto.getCourseId());
//		assertEquals(mappedIntervention.getUser().getId(), interventionDG2Dto.getUserId());
		assertEquals(mappedIntervention.getType().toString(), interventionDG2Dto.getType());
		assertEquals(mappedIntervention.isValidated(), interventionDG2Dto.isValidated());
		assertEquals(mappedIntervention.getDateStart().toString(), interventionDG2Dto.getDateStart());
		assertEquals(mappedIntervention.getDateEnd().toString(), interventionDG2Dto.getDateEnd());
		assertEquals(mappedIntervention.isMaster(), interventionDG2Dto.isMaster());
		assertEquals(null, mappedIntervention.getMasterIntervention());
	}
}
