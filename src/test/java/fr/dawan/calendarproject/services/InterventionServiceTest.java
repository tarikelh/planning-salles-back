package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.dawan.calendarproject.dto.*;
import fr.dawan.calendarproject.enums.UserCompany;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.InterventionMapper;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionCustomRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.ICalTools;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class InterventionServiceTest {

	@Autowired
	private InterventionService interventionService;

	@Autowired
	private LeavePeriodService leavePeriodService;

	@MockBean
	private InterventionRepository interventionRepository;

	@MockBean
	private InterventionCustomRepository interventionCustomRepository;

	@MockBean
	private LocationRepository locationRepository;

	@MockBean
	private CourseRepository courseRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private InterventionCaretaker caretaker;

	@MockBean
	private InterventionMementoRepository interventionMementoRepository;

	@MockBean
	private InterventionMapper interventionMapper;

	@MockBean
	private RestTemplate restTemplate;

	private List<Intervention> interventions = new ArrayList<Intervention>();
	private List<InterventionDto> iDtos = new ArrayList<InterventionDto>();
	private List<InterventionMementoDto> intMementoDtos = new ArrayList<InterventionMementoDto>();
	private List<InterventionMemento> interventionMementos = new ArrayList<InterventionMemento>();
	private List<AdvancedInterventionDto> advInterventions = new ArrayList<AdvancedInterventionDto>();
	private MockedStatic<ICalTools> mICalTools;
	private Course course;
	
	LocalDate date = LocalDate.now();

	Location location = new Location(1, 0, "Paris", "FR", "red", false, 0);
	LocationDto locationDto = new LocationDto(1, 1, "Paris", "FR", "red", false, 0);
	CourseDto courseDto = new CourseDto(1, 1, "Java course for beginners", "5", "slug", 0);
	UserDto userDto = new UserDto(1, 1, 1,"Daniel", "Balavoine",1,"dbalavoine@dawan.fr", "testPassword",
			UserType.ADMINISTRATIF.toString(), UserCompany.DAWAN.toString(), "",date.toString(),0);

	private String email = "admin@dawan.fr";
	private String pwd = "testPassword";
	private String start = "2012-05-01";
	private String end = "2012-05-02";

	@BeforeEach
	void beforeEach() throws Exception {
		mICalTools = Mockito.mockStatic(ICalTools.class);

		LocalDate date = LocalDate.now();

		Location mockedLoc = new Location(1, 0, "Paris", "red", "FR", false, 0);
		Course mockedCourse = new Course(1, 1, "Java course for beginners", "5", "slug", 0);
		User mockedUser = new User(1, 1, 1, "Daniel", "Balavoine", mockedLoc, "dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", date, null, 0);
		course = new Course(2, 548, "C#", "5", "slug", 0);
		
		InterventionDto interventionDto = Mockito.mock(InterventionDto.class);
		
		interventions.add(new Intervention(1, 1, "lambdaSlug", "I am lambda Intervention", email, mockedLoc, mockedCourse,
				mockedUser, 1, InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0, email, null, 0));

		Intervention masterDummy = new Intervention(2, 2, "masterSlug", "I am a master Intervention", email, mockedLoc,
				mockedCourse, mockedUser, 1, InterventionStatus.INTERN, true, LocalDate.now().plusDays(7),
				LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), null, true, 0, email, null, 0);
		interventions.add(masterDummy);

		Intervention slaveDummy = new Intervention(3, 3, "slaveSlug", "I am a slave Intervention", email, mockedLoc,
				mockedCourse, mockedUser, 1, InterventionStatus.INTERN, true, LocalDate.now().plusDays(7),
				LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), masterDummy, false, 0, email, null, 0);
		interventions.add(slaveDummy);

		iDtos.add(new InterventionDto(1, 1, "lambdaSlug", "I am lambda Intervention", 1, 1, 1, 1, 1, 1, "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false,
				email, 0));

		iDtos.add(new InterventionDto(2, 2, "masterSlug", "I am a master Intervention", 1, 1, 1, 1, 1, 1, "INTERN",
				true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, true, email, 0));

		iDtos.add(new InterventionDto(3, 3, "slaveSlug", "I am a slave Intervention", 1, 1, 1, 1, 1, 1, "INTERN", true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), 2,
				false, email, 0));

		intMementoDtos.add(new InterventionMementoDto(0, 0, "slug-0", "I am a new Intervention", 1, "Paris", 1, 1,
				"Java for intermediate level", 1, 1, "Admin Fullname", 10, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));

		interventionMementos.add(new InterventionMemento(1, intMementoDtos.get(0),
				new MementoMessageDto(1, " has been created by ", email, ""), 0));
		
		advInterventions.add(new AdvancedInterventionDto(1,1,"advInter slug","advInter comment",email, locationDto,
				courseDto, userDto,1,"INTERN",true, LocalDate.now(),LocalDate.now().plusDays(1L),
				LocalTime.now(),LocalTime.now().plusHours(7L), interventionDto,true,email, 1));

		advInterventions.add(new AdvancedInterventionDto(1,1,"advInter slug","advInter comment",email, locationDto,
				courseDto, userDto,1,"INTERN",true, LocalDate.now(),LocalDate.now().plusDays(1L),
				LocalTime.now(),LocalTime.now().plusHours(7L), interventionDto,true,email, 1));

	}

	@AfterEach
	public void afterEach() throws Exception {
		if (!mICalTools.isClosed())
			mICalTools.close();
	}

	@Test
	void contextLoads() {
		assertThat(interventionService).isNotNull();
	}

	@Test
	void shouldGetInterventionsAndReturnDtos() {
		when(interventionRepository.findAll()).thenReturn(interventions);
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(0),
				iDtos.get(1), iDtos.get(2));

		List<InterventionDto> result = interventionService.getAllInterventions();

		assertThat(result).isNotNull();
		assertEquals(interventions.size(), result.size());
		assertEquals(iDtos.size(), result.size());
		assertEquals(iDtos, result);
	}

	@Test
	void shouldGetPaginatedInterventionsAndReturnDtos() {
		int page = 0;
		int size = 2;

		Page<Intervention> p1 = new PageImpl<Intervention>(interventions.subList(0, 2));
		Page<Intervention> p2 = new PageImpl<Intervention>(interventions.subList(2, 3));

		when(interventionRepository.findAll(PageRequest.of(page, size))).thenReturn(p1);
		when(interventionRepository.findAll(PageRequest.of(page + 1, size))).thenReturn(p2);
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(0),
				iDtos.get(1), iDtos.get(2));

		List<InterventionDto> page1 = interventionService.getAllInterventions(page, size);
		List<InterventionDto> page2 = interventionService.getAllInterventions(page + 1, size);

		assertThat(page1).isNotNull();
		assertThat(page2).isNotNull();
		assertEquals(2, page1.size());
		assertEquals(1, page2.size());
		assertEquals(page1, iDtos.subList(0, 2));
		assertEquals(page2, iDtos.subList(2, 3));
	}

	@Test
	void shouldThrowIllegalArgumentWhenPageIsNegative() {
		assertThrows(IllegalArgumentException.class, () -> {
			interventionService.getAllInterventions(-1, 1);
		});
	}

	@Test
	void shouldThrowIllegalArgumentWhenSizeIsLessThanOne() {
		assertThrows(IllegalArgumentException.class, () -> {
			interventionService.getAllInterventions(0, 0);
		});
	}

	@Test
	void shouldGetAllInterventionByUserIdAndReturnDtos() {
		when(interventionRepository.getAllByUserId(0)).thenReturn(interventions);
		when(interventionMapper.listInterventionToListInterventionDto(interventions)).thenReturn(iDtos);

		List<InterventionDto> result = interventionService.getAllByUserId(0);

		assertThat(result).isNotNull();
		assertEquals(interventions.size(), result.size());
		assertEquals(iDtos.get(0), result.get(0));
		assertEquals(iDtos.get(1), result.get(1));
		assertEquals(iDtos.get(2), result.get(2));
	}

	@Test
	void shouldGetInterventionById() {
		long intervId = 2;
		InterventionDto expected = iDtos.get(1);

		when(interventionRepository.findById(intervId)).thenReturn(Optional.of(interventions.get(1)));
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(1));

		InterventionDto result = interventionService.getById(intervId);

		assertThat(result).isNotNull();
		assertEquals(result, expected);
		assertEquals(result.getId(), intervId);
	}

	@Test
	void shouldReturnNullWhenGetByIdWhenWrongIdProvided() {
		long intervId = 0;

		when(interventionRepository.findById(intervId)).thenReturn(Optional.empty());

		assertThat(interventionService.getById(intervId)).isNull();
	}

	@Test
	void shouldSaveNewIntervention() throws Exception {
		Location mockedLoc = Mockito.mock(Location.class);
		Course mockedCourse = Mockito.mock(Course.class);
		User mockedUser = Mockito.mock(User.class);

		InterventionDto newIntervDto = new InterventionDto(0, 0, "newSlug", "I am a New Intervention", 0, 0, 0, 0, 0, 1,
				"INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, false, email, 0);

		Intervention newInterv = new Intervention(0, 0, "newSlug", "I am a New Intervention", null, null, null, mockedUser, 1,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0, email, null, 0);

		Intervention savedInterv = new Intervention(5, 5, "newSlug", "I am a New Intervention", email, mockedLoc, mockedCourse,
				mockedUser, 1, InterventionStatus.INTERN, true, LocalDate.now().plusDays(7),
				LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0, email, null, 0);

		InterventionDto expectedInterv = new InterventionDto(5, 5, "newSlug", "I am a New Intervention", 0, 0, 0, 0, 0,
				1, "INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, false, email, 0);

		InterventionMemento mementoInterv = new InterventionMemento(1,
				new InterventionMementoDto(5, 5, "slug-5", "I am a New Intervention", 0, "", 0, 0, "", 0, 0, "", 10,
						"INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
						LocalTime.of(17, 0), 0, false),
				new MementoMessageDto(1, " has been created by ", "admin@dawan.fr", ""), 0);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedUser));
		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedCourse));
		when(interventionRepository.findFromUserByDateRange(any(Long.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(new ArrayList<Intervention>());
		when(interventionRepository.findSibblings(any(Long.class), any(LocalDate.class), any(LocalDate.class), any(Long.class), any(Long.class))).thenReturn(interventions);
		when(interventionMapper.listInterventionToListAdvInterventionDto(interventions)).thenReturn(advInterventions);

		when(interventionMapper.interventionDtoToIntervention(any(InterventionDto.class))).thenReturn(newInterv);

		when(locationRepository.getOne(3L)).thenReturn(mockedLoc);
		when(courseRepository.getOne(3L)).thenReturn(mockedCourse);
		when(userRepository.getOne(3L)).thenReturn(mockedUser);

		when(interventionRepository.saveAndFlush(newInterv)).thenReturn(savedInterv);

		when(caretaker.addMemento("admin@dawan.fr", savedInterv)).thenReturn(mementoInterv);

		when(interventionMapper.interventionToInterventionDto(savedInterv)).thenReturn(expectedInterv);

		AdvancedInterventionDto2 result = interventionService.saveOrUpdate(newIntervDto, "admin@dawan.fr");

		assertThat(result).isNotNull();
		assertEquals(result, expectedInterv);
		assertEquals(5L, result.getId());
	}

	@Test
	void shouldSaveMasterIntervention() throws Exception {

		InterventionDto newIntervDto = new InterventionDto(0, 0, "newSlug", "I am a New Intervention", 0, 0, 0, 0, 0, 1,
				"INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, true, email, 0);

		Intervention newInterv = new Intervention(0, 0, "newSlug", "I am a New Intervention", null, null, null, null, 1,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), null, true, 0, email, null, 0);

		Intervention savedInterv = new Intervention(5, 5, "newSlug", "I am a New Intervention", null, null, null, null, 1,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), null, true, 0, email, null, 0);

		InterventionDto expectedInterv = new InterventionDto(5, 5, "newSlug", "I am a New Intervention", 0, 0, 0, 0, 0,
				1, "INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, true, email, 0);

		InterventionMemento mementoInterv = new InterventionMemento(1,
				new InterventionMementoDto(5, 5, "slug-5", "I am a New Intervention", 0, "", 0, 0, "", 0, 0, "", 10,
						"INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
						LocalTime.of(17, 0), 0, true),
				new MementoMessageDto(1, " has been created by ", "admin@dawan.fr", ""), 0);

		when(interventionRepository.findFromUserByDateRange(any(Long.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(new ArrayList<Intervention>());

		when(interventionMapper.interventionDtoToIntervention(any(InterventionDto.class))).thenReturn(newInterv);

		when(interventionRepository.saveAndFlush(newInterv)).thenReturn(savedInterv);

		when(caretaker.addMemento("admin@dawan.fr", savedInterv)).thenReturn(mementoInterv);

		when(interventionMapper.interventionToInterventionDto(savedInterv)).thenReturn(expectedInterv);

		AdvancedInterventionDto2 result = interventionService.saveOrUpdate(newIntervDto, "admin@dawan.fr");
		// System.out.println(result.toString());

		assertThat(result).isNotNull();
		assertEquals(expectedInterv, result);
		assertEquals(5L, result.getId());
		assertEquals(true, result.isMaster());
		assertEquals(0, result.getUser().getId());
		assertEquals(0, result.getCourse().getId());
		assertEquals(0, result.getLocation().getId());
	}

	@Test
	void shouldSaveSubIntervention() throws Exception {
		Location mockedLoc = Mockito.mock(Location.class);
		Course mockedCourse = Mockito.mock(Course.class);
		User mockedUser = Mockito.mock(User.class);

		InterventionDto newIntervDto = new InterventionDto(0, 0, "newSlug", "I am a New Intervention", 0, 0, 0, 0, 0, 1,
				"INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 2, false, email, 0);

		Intervention newInterv = new Intervention(0, 0, "newSlug", "I am a New Intervention", null, null, null, mockedUser, 1,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), interventions.get(1), false, 0, email, null, 0);

		Intervention savedInterv = new Intervention(5, 5, "newSlug", "I am a New Intervention", email, mockedLoc, mockedCourse,
				mockedUser, 1, InterventionStatus.INTERN, true, LocalDate.now().plusDays(7),
				LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), interventions.get(1), false, 0, email, null, 0);

		InterventionDto expectedInterv = new InterventionDto(5, 5, "newSlug", "I am a New Intervention", 0, 0, 0, 0, 0,
				1, "INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 2, false, email, 0);

		InterventionMemento mementoInterv = new InterventionMemento(1,
				new InterventionMementoDto(5, 5, "slug-5", "I am a New Intervention", 0, "", 0, 0, "", 0, 0, "", 10,
						"INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
						LocalTime.of(17, 0), 0, false),
				new MementoMessageDto(1, " has been created by ", "admin@dawan.fr", ""), 0);

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedUser));
		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedCourse));
		when(interventionRepository.findFromUserByDateRange(any(Long.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(new ArrayList<Intervention>());

		when(interventionMapper.interventionDtoToIntervention(any(InterventionDto.class))).thenReturn(newInterv);

		when(locationRepository.getOne(3L)).thenReturn(mockedLoc);
		when(courseRepository.getOne(3L)).thenReturn(mockedCourse);
		when(userRepository.getOne(3L)).thenReturn(mockedUser);

		when(interventionRepository.saveAndFlush(newInterv)).thenReturn(savedInterv);

		when(caretaker.addMemento("admin@dawan.fr", savedInterv)).thenReturn(mementoInterv);

		when(interventionMapper.interventionToInterventionDto(savedInterv)).thenReturn(expectedInterv);

		AdvancedInterventionDto2 result = interventionService.saveOrUpdate(newIntervDto, "admin@dawan.fr");
		
		assertThat(result).isNotNull();
		assertEquals(expectedInterv, result);
		assertEquals(5L, result.getId());
		assertEquals(false, result.isMaster());
		assertEquals(2L, result.getMasterIntervention().getId());
	}

	@Test
	void shouldReturnNullWhenUpdateUnknownId() throws Exception {
		InterventionDto interv = iDtos.get(2);
		interv.setId(4);

		when(interventionRepository.existsById(4L)).thenReturn(false);

		assertThat(interventionService.saveOrUpdate(interv, "admin@dawan.fr")).isNull();
	}

	@Test
	void shouldGetFromUserByDateRange() {
		when(interventionRepository.findFromUserByDateRange(any(Long.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(interventions);

		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(0),
				iDtos.get(1), iDtos.get(2));

		List<InterventionDto> result = interventionService.getFromUserByDateRange(1, LocalDate.now(),
				LocalDate.now().plusDays(5));

		assertThat(result).isNotNull();
		assertEquals(interventions.size(), result.size());
		assertEquals(iDtos.size(), result.size());
		assertEquals(iDtos, result);
	}

	@Test
	void shouldGetAllByDateRange() {
		when(interventionRepository.findAllByDateRange(any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(interventions);

		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(0),
				iDtos.get(1), iDtos.get(2));

		List<InterventionDto> result = interventionService.getAllByDateRange(LocalDate.now(),
				LocalDate.now().plusDays(5));

		assertThat(result).isNotNull();
		assertEquals(interventions.size(), result.size());
		assertEquals(iDtos.size(), result.size());
		assertEquals(iDtos, result);
	}

	@Test
	void shouldReturnCountByUserType() throws Exception {
		String userType = "ADMINISTRATIF";
		CountDto expectedCount = new CountDto(interventions.size());

		when(interventionRepository.countByUserTypeNoMaster(UserType.valueOf(userType))).thenReturn(3L);

		CountDto result = interventionService.count(userType);

		assertThat(result).isNotNull();
		assertEquals(expectedCount.getNb(), result.getNb());
	}

	@Test
	void shouldReturnNullWhenCountByWrongUserType() {
		String userType = "BADUSERTYPE";
		assertThat(interventionService.count(userType)).isNull();
	}

	@Test
	void shouldGetMasterIntervention() {
		when(interventionRepository.getMasterIntervention()).thenReturn(interventions.subList(1, 2));
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(1));

		List<InterventionDto> result = interventionService.getMasterIntervention();

		assertThat(result).isNotNull();
		assertEquals(iDtos.subList(1, 2), result);
		assertEquals(1, result.size());
	}

	@Test
	void shouldGetSubInterventions() {

		when(interventionRepository.getAllChildrenByUserTypeAndDates(any(UserType.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(interventions.subList(2, 3));

		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(2));

		List<InterventionDto> result = interventionService.getSubInterventions("FORMATEUR",
				Mockito.mock(LocalDate.class), Mockito.mock(LocalDate.class));

		assertThat(result).isNotNull();
		assertEquals(iDtos.subList(2, 3), result);
		assertEquals(1, result.size());
	}

	@Test
	void shouldReturnEmptyListWhenGetSubInterventionsWithWrongUserType() {
		assertThat(interventionService.getSubInterventions("BAD_USER_TYPE", Mockito.mock(LocalDate.class),
				Mockito.mock(LocalDate.class))).isEmpty();
	}

	@Test
	void shouldReturnCalendarFromUser()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		long userId = 1;

		VTimeZone tz = Mockito.mock(VTimeZone.class);
		Calendar cal = new Calendar();
		cal.getProperties().add(new ProdId("-//Dawan Calendar//iCal4j 1.0//FR"));
		cal.getProperties().add(Version.VERSION_2_0);

		when(interventionRepository.findByUserId(userId)).thenReturn(interventions);
		mICalTools.when(() -> ICalTools.createVEvent(any(Intervention.class), any(VTimeZone.class)))
				.thenReturn(new VEvent());
		mICalTools.when(() -> ICalTools.getTimeZone(any(String.class))).thenReturn(tz);
		mICalTools.when(() -> ICalTools.createCalendar(any(String.class))).thenReturn(cal);

		Calendar calendar = interventionService.exportCalendarAsICal(userId);

		assertThat(calendar).isNotNull();
		assertEquals("-//Dawan Calendar//iCal4j 1.0//FR", calendar.getProductId().getValue());
		assertEquals("BalavoineDaniel", calendar.getProperty("X-CALNAME").getValue());
		assertEquals(3, calendar.getComponents("VEVENT").size());

	}

	@Test
	void shouldReturnNullWhenUserIdDoesNotExistOrHasNoIntervention() {
		long badUserId = 15454;
		when(interventionRepository.findByUserId(badUserId)).thenReturn(null);

		assertThat(interventionService.exportCalendarAsICal(badUserId)).isNull();
	}

	@Test
	void shouldReturnTrueWhenInterventionHasNoError() {
		LocalDate date = LocalDate.now();

		Location mockedLoc = new Location(1, 0, "Paris", "FR", "red", false, 0);
		Course mockedCourse = new Course(1, 1, "Java course for beginners", "5", "slug", 0);
		User mockedUser = new User(1, 1, 1, "Daniel", "Balavoine", mockedLoc, "dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", date, null, 0);

		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedUser));

		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedCourse));

		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));

		when(interventionRepository.findFromUserByDateRange(any(Long.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(interventions);

		when(interventionMapper.interventionToInterventionDto(interventions.get(0))).thenReturn(iDtos.get(0));

		Assertions.assertTrue(interventionService.checkIntegrity(iDtos.get(0)));
	}

	@Test
	void shouldThrowWhenInterventionHasError() {
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(interventionRepository.findFromUserByDateRange(any(Long.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(interventions.subList(1, 3));

		assertThrows(EntityFormatException.class, () -> {
			InterventionDto i = iDtos.get(0);
			i.setLocationId(1);
			i.setUserId(1);
			i.setCourseId(1);
			i.setDateEnd(LocalDate.now().minusDays(5));
			i.setType("BAD_TYPE");

			interventionService.checkIntegrity(i);
		});
	}

	@Test
	void shouldThrowWhenMasterInterventionHasError() {
		when(interventionRepository.findFromUserByDateRange(any(Long.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(interventions.subList(1, 3));

		assertThrows(EntityFormatException.class, () -> {
			InterventionDto i = iDtos.get(1);
			i.setLocationId(1);
			i.setUserId(1);
			i.setCourseId(1);
			i.setDateEnd(LocalDate.now().minusDays(5));
			i.setMaster(true);
			i.setMasterInterventionId(1);

			interventionService.checkIntegrity(i);
		});
	}

	@Test
	void shouldGetInterventionByCourseIdAndReturnDtos() {
		when(interventionRepository.findByCourseId(0)).thenReturn(interventions);
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(0),
				iDtos.get(1), iDtos.get(2));

		List<InterventionDto> result = interventionService.getByCourseId(0);

		assertThat(result).isNotNull();
		assertEquals(iDtos.size(), result.size());
	}

	@Test
	void shouldGetInterventionByCourseTitleAndReturnDtos() {
		when(interventionRepository.findByCourseTitle(any(String.class))).thenReturn(interventions);
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(0),
				iDtos.get(1), iDtos.get(2));

		List<InterventionDto> result = interventionService.getByCourseTitle("test");

		assertThat(result).isNotNull();
		assertEquals(iDtos.size(), result.size());
	}

	@Test
	void shouldGetInterventionsByUserIdAndSearchKeywords() {
		Map<String, String[]> paramsMap = new HashMap<String, String[]>();
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(Mockito.mock(User.class)));
		when(interventionCustomRepository.searchBy(1, paramsMap)).thenReturn(interventions);
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(0),
				iDtos.get(1), iDtos.get(2));

		List<InterventionDto> result = interventionService.searchBy(1, paramsMap);

		assertThat(result).isNotNull();
		assertEquals(interventions.size(), result.size());
	}

	@Test
	void shouldReturnEmptyListWhenSearchByWithWrongUserId() {
		Map<String, String[]> paramsMap = new HashMap<String, String[]>();
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		List<InterventionDto> result = interventionService.searchBy(651651, paramsMap);

		assertThat(result).isEmpty();
	}

//	@Test
//	void shouldGetSubInterventionWithMasterId() {
//		when(interventionRepository.findByMasterId(interventions.get(1).getId()))
//				.thenReturn(Optional.of(interventions.get(2)));
//		when(interventionRepository.findByMasterInterventionIdOrderByDateStart(interventions.get(1).getId()))
//				.thenReturn();
//		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(iDtos.get(2));
//
//		List<InterventionDto> result = interventionService.getSubByMasterId(2);
//
//		assertThat(result).isNotNull();
//		assertEquals(iDtos.get(2), result.get(0));
//	}

	@Test
	void shouldReturnEmptyListWhenInterventionIsNotMaster() {
		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.of(interventions.get(0)));

		List<InterventionDto> result = interventionService.getSubByMasterId(2);

		assertThat(result).isEmpty();
	}

	@Test
	void shouldReturnEmptyListWhenMasterIdIsWrong() {
		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		List<InterventionDto> result = interventionService.getSubByMasterId(2);

		assertThat(result).isEmpty();
	}

	@Test
	void shouldSplitInterventionCreateMasterAndReturnListOfDto() {
		List<Intervention> saveAllReturn = new ArrayList<Intervention>();
		List<InterventionDto> expected = new ArrayList<InterventionDto>();
		List<DateRangeDto> dates = new ArrayList<DateRangeDto>();

		Intervention masterIntervention = new Intervention(4, 4, "masterSlug", "", null, null, null, null, 0,
				InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0), null, true, 0, email, null, 0);

		InterventionDto masterInterventionDto = new InterventionDto(4, 4, "masterSlug", "", 0, 0, 0, 0, 0, 0,
				"SUR_MESURE", true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, true, email, 0);

		saveAllReturn.add(new Intervention(1, 1, "newSlug", "I am lambda Intervention", email, Mockito.mock(Location.class),
				Mockito.mock(Course.class), Mockito.mock(User.class), 1, InterventionStatus.SUR_MESURE, true,
				LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0, email, null, 0));

		saveAllReturn.add(new Intervention(1, 1, "newSlug", "I am lambda Intervention", email, Mockito.mock(Location.class),
				Mockito.mock(Course.class), Mockito.mock(User.class), 1, InterventionStatus.SUR_MESURE, true,
				LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), null,
				false, 0, email, null, 0));

		expected.add(masterInterventionDto);
		expected.add(new InterventionDto(1, 1, "newSlug", "I am lambda Intervention", 0, 0, 0, 0, 0, 1, "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false,
				email, 0));
		expected.add(new InterventionDto(4, 4, "newSlug", "I am lambda Intervention", 0, 0, 0, 0, 0, 1, "SUR_MESURE",
				true, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0),
				0, false, email, 0));

		dates.add(new DateRangeDto(1, LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));
		dates.add(new DateRangeDto(0, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));

		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.of(interventions.get(0)));

		when(interventionRepository.saveAndFlush(any(Intervention.class))).thenReturn(masterIntervention);
		when(interventionRepository.saveAll(any())).thenReturn(saveAllReturn);
		doNothing().when(interventionRepository).flush();
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(expected.get(0),
				expected.get(1), expected.get(2));

		List<InterventionDto> result = interventionService.splitIntervention(1, dates);

		assertThat(result).isNotNull();
		assertEquals(expected.size(), result.size());
		assertTrue(result.get(0).isMaster());
		assertEquals(expected.get(1), result.get(1));
		assertEquals(masterInterventionDto, result.get(0));
		assertEquals(dates.get(0).getDateStart(), result.get(1).getDateStart());
		assertEquals(dates.get(0).getDateEnd(), result.get(1).getDateEnd());
		assertEquals(dates.get(1).getDateStart(), result.get(2).getDateStart());
		assertEquals(dates.get(1).getDateEnd(), result.get(2).getDateEnd());
		assertEquals(dates.get(1).getTimeStart(), result.get(2).getTimeStart());
		assertEquals(dates.get(1).getTimeEnd(), result.get(2).getTimeEnd());
	}

	@Test
	void shouldSplitInterventionMasterAndReturnListOfDto() {
		List<DateRangeDto> dates = new ArrayList<DateRangeDto>();
		List<InterventionDto> expected = new ArrayList<InterventionDto>();
		InterventionDto master = new InterventionDto(2, 2, "masterSlug", "I am a master Intervention", 0, 0, 0, 0, 0, 0,
				"INTERN", true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(13), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, true, email, 0);

		expected.add(master);
		expected.add(iDtos.get(2));
		expected.add(new InterventionDto(4, 4, "masterSlug", "I am a master Intervention", 0, 0, 0, 0, 0, 0, "INTERN",
				true, LocalDate.now().plusDays(11), LocalDate.now().plusDays(13), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, true, email, 0));

		dates.add(new DateRangeDto(3, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));
		dates.add(new DateRangeDto(0, LocalDate.now().plusDays(11), LocalDate.now().plusDays(13), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));

		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.of(interventions.get(2)));

		when(interventionRepository.saveAll(any())).thenReturn(interventions.subList(1, 3));
		doNothing().when(interventionRepository).flush();
		when(interventionMapper.interventionToInterventionDto(any(Intervention.class))).thenReturn(expected.get(0),
				expected.get(1), expected.get(2));

		List<InterventionDto> result = interventionService.splitIntervention(3, dates);

		assertThat(result).isNotNull();
		assertEquals(expected.size(), result.size());
		assertTrue(result.get(0).isMaster());
		assertEquals(expected.get(1), result.get(1));
		assertEquals(dates.get(0).getDateStart(), result.get(1).getDateStart());
		assertEquals(dates.get(0).getDateEnd(), result.get(1).getDateEnd());
		assertEquals(dates.get(1).getDateStart(), result.get(2).getDateStart());
		assertEquals(dates.get(1).getDateEnd(), result.get(2).getDateEnd());
		assertEquals(dates.get(1).getTimeStart(), result.get(2).getTimeStart());
		assertEquals(dates.get(1).getTimeEnd(), result.get(2).getTimeEnd());
	}

	@Test
	void shouldReturnEmptyListWhenInterventionIdIsWrong() {
		List<DateRangeDto> dates = new ArrayList<DateRangeDto>();
		dates.add(new DateRangeDto(1, LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));

		dates.add(new DateRangeDto(1, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));

		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		List<InterventionDto> result = interventionService.splitIntervention(1231321321, dates);

		assertThat(result).isEmpty();
	}

	@Test
	void shouldReturnEmptyListWhenDateListIsNullOrEmpty() {
		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

		List<InterventionDto> result = interventionService.splitIntervention(1231321321, null);

		assertThat(result).isEmpty();
	}

	@Test
	void shouldThrowWhenDateRangeContainsWrongInterventionId() {
		List<DateRangeDto> dates = new ArrayList<DateRangeDto>();
		dates.add(new DateRangeDto(1, LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));

		dates.add(new DateRangeDto(151515, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));

		when(interventionRepository.findById(1L)).thenReturn(Optional.of(interventions.get(0)));
		when(interventionRepository.findById(151515L)).thenReturn(Optional.empty());

		assertThrows(EntityFormatException.class, () -> {
			interventionService.splitIntervention(1, dates);
		});
	}

	@Test
	void shouldThrowWhenDatesOrTimeOverlapOrInWrongOrder() throws NoSuchMethodException, SecurityException {
		List<DateRangeDto> dates = new ArrayList<DateRangeDto>();
		dates.add(new DateRangeDto(1, LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0),
				LocalTime.of(8, 0)));

		dates.add(new DateRangeDto(151515, LocalDate.now().plusDays(1), LocalDate.now(), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));

		dates.add(new DateRangeDto(0, LocalDate.now().plusDays(4), LocalDate.now().plusDays(3), LocalTime.of(19, 0),
				LocalTime.of(17, 0)));

		when(interventionRepository.findById(1L)).thenReturn(Optional.of(interventions.get(0)));

		EntityFormatException resultException = assertThrows(EntityFormatException.class, () -> {
			interventionService.splitIntervention(1, dates);
		});

		Object[] array = resultException.getErrors().toArray();
		APIError result = (APIError) array[2];

		assertEquals(5, resultException.getErrors().size());
		assertEquals("/api/interventions", result.getPath());
	}

	@SuppressWarnings("unchecked")
	@Test
	void shouldGetInterventionsFromDG2() throws Exception {
		String body = "[{\"id\":551256,\"locationId\":1,\"dateStart\":\"2021-01-04T00:00:00+00:00\",\"dateEnd\":\"2021-01-04T00:00:00+00:00\",\"slug\":\"word-initiation-04-01-2021\",\"courseId\":1143,\"type\":\"shared\",\"masterInterventionId\":551255,\"personId\":null,\"nbParticipants\":1}]";

		ResponseEntity<String> res = new ResponseEntity<>(body,HttpStatus.OK);

		when(courseRepository.findByIdDg2(any(Long.class))).thenReturn(Optional.of(course));
		when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
				.thenReturn(res);
		when(interventionMapper.interventionDG2DtoToIntervention(any(InterventionDG2Dto.class)))
				.thenReturn(Mockito.mock(Intervention.class));
		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(interventionRepository.saveAndFlush(any(Intervention.class))).thenReturn(Mockito.mock(Intervention.class));

		assertDoesNotThrow(() -> interventionService.fetchDG2InterventionsOnly(true,email,pwd,LocalDate.parse(start),LocalDate.parse(end)));
		assertDoesNotThrow(() -> interventionService.fetchDG2InterventionsOnly(false,email,pwd,LocalDate.parse(start),LocalDate.parse(end)));
	}

	//TODO: shouldFetchUpdateInterventionFromDg2
	/*@SuppressWarnings("unchecked")
	@Test
	void shouldGetUpdateInterventionsFromDG2() throws Exception {
		Location mockedLoc = Mockito.mock(Location.class);
		Course mockedCourse = Mockito.mock(Course.class);
		User mockedUser = Mockito.mock(User.class);

		String body = "[{\"id\":551256,\"locationId\":1,\"dateStart\":\"2021-01-04T00:00:00+00:00\",\"dateEnd\":\"2021-01-04T00:00:00+00:00\",\"slug\":\"word-initiation-04-01-2021\",\"courseId\":1143,\"type\":\"shared\",\"masterInterventionId\":551255,\"personId\":null,\"nbParticipants\":1}]";
		Intervention fromDg2 = new Intervention(551256, 551256, "", "I am lambda Intervention", mockedLoc, mockedCourse,
				mockedUser, 1, InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5), null,
				null, null, false, 0);

		Intervention inDb = new Intervention(551256, 551256, "lambdaSlug", "I am lambda Intervention", mockedLoc,
				mockedCourse, mockedUser, 1, InterventionStatus.SUR_MESURE, true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0);

		ResponseEntity<String> res = new ResponseEntity<>(body,HttpStatus.OK);

		when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
				.thenReturn(res);
		when(interventionMapper.interventionDG2DtoToIntervention(any(InterventionDG2Dto.class)))
				.thenReturn(fromDg2);
		when(interventionRepository.findById(551256L)).thenReturn(Optional.of(inDb));
		when(interventionRepository.saveAndFlush(any(Intervention.class))).thenReturn(Mockito.mock(Intervention.class));

		assertDoesNotThrow(() -> interventionService.fetchDG2InterventionsOnly(true,email,pwd,LocalDate.parse(start),LocalDate.parse(end)));
		assertDoesNotThrow(() -> interventionService.fetchDG2InterventionsOnly(false,email,pwd,LocalDate.parse(start),LocalDate.parse(end)));
	}

	@Test
	void shouldImportUpdateInterventionsFromDG2() throws Exception {
		Location mockedLoc = Mockito.mock(Location.class);
		Course mockedCourse = Mockito.mock(Course.class);
		User mockedUser = Mockito.mock(User.class);

		String email = "test@dawan.fr";
		String pwd = "testPassword";
		String start = "2012-05-01";
		String end = "2012-05-02";
		String body = "[{\"id\":551256,\"locationId\":1,\"dateStart\":\"2021-01-04T00:00:00+00:00\",\"dateEnd\":\"2021-01-04T00:00:00+00:00\",\"slug\":\"word-initiation-04-01-2021\",\"courseId\":1143,\"type\":\"shared\",\"masterInterventionId\":551255,\"personId\":null,\"nbParticipants\":1},{\"id\":551268,\"locationId\":17,\"dateStart\":\"2021-01-04T00:00:00+00:00\",\"dateEnd\":\"2021-01-04T00:00:00+00:00\",\"slug\":\"agile-scrum-initiation-04-01-2021\",\"courseId\":100281,\"type\":\"shared\",\"masterInterventionId\":551267,\"personId\":null,\"nbParticipants\":1},{\"id\":545368,\"locationId\":3,\"dateStart\":\"2021-01-04T00:00:00+00:00\",\"dateEnd\":\"2021-01-05T00:00:00+00:00\",\"slug\":\"informatique-pour-les-debutants-office-04-01-2021\",\"courseId\":100137,\"type\":\"shared\",\"masterInterventionId\":545367,\"personId\":null,\"nbParticipants\":1}]";

		URI url = new URI(String.format("https://dawan.org/api2/planning/interventions/%s/%s", start, end));

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + pwd);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		Intervention fromDg2 = new Intervention(551256, 551256, "", "I am lambda Intervention", mockedLoc, mockedCourse,
				mockedUser, 1, InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5), null,
				null, null, false, 0);

		Intervention inDb = new Intervention(551256, 551256, "lambdaSlug", "I am lambda Intervention", mockedLoc,
				mockedCourse, mockedUser, 1, InterventionStatus.SUR_MESURE, true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), null, false, 0);

		when(courseRepository.findByIdDg2(any(Long.class))).thenReturn(Optional.of(course));
		when(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body(body));
		when(interventionMapper.interventionDG2DtoToIntervention(any(InterventionDG2Dto.class))).thenReturn(fromDg2);
		when(interventionRepository.findById(551256L)).thenReturn(Optional.of(inDb));
		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(interventionRepository.saveAndFlush(any(Intervention.class))).thenReturn(Mockito.mock(Intervention.class));

		int result = interventionService.fetchDG2Interventions(email, pwd, LocalDate.parse(start),
				LocalDate.parse(end));

		assertThat(result).isEqualTo(3);
	}*/

	@Test
	void shouldThrowWhenDG2StatusIsNotOk() throws Exception {
		String email = "test@dawan.fr";
		String pwd = "testPassword";
		String start = "2012-05-01";
		String end = "2012-05-02";

		URI url = new URI(String.format("https://dawan.org/api2/planning/interventions/%s/%s", start, end));

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + pwd);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		when(restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class))
				.thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));

		assertThrows(Exception.class, () -> interventionService.fetchDG2Interventions(email, pwd,
				LocalDate.parse(start), LocalDate.parse(end)));
	}

	@Test
	void shouldDoNothingWhenInterventionDeleted() throws Exception {
		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.of(interventions.get(0)));

		doNothing().when(interventionRepository).deleteById(any(Long.class));

		when(caretaker.addMemento(any(String.class), any(Intervention.class))).thenReturn(interventionMementos.get(0));

		assertDoesNotThrow(() -> interventionService.deleteById(any(Long.class), email));
/*
		assertThrows(Exception.class, () -> interventionService.deleteById(interventions.get(0).getId(), email));*/
	}
	
}
