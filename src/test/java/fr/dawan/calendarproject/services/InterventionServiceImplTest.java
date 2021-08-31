package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionCaretaker;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@SpringBootTest
class InterventionServiceImplTest {

	@SpyBean
	private InterventionService interventionService;

	@MockBean
	private InterventionRepository interventionRepository;

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

	private List<Intervention> interventions = new ArrayList<Intervention>();
	private List<InterventionDto> iDtos = new ArrayList<InterventionDto>();

	private MockedStatic<DtoTools> mDtoTools;

	@BeforeEach()
	public void beforeEach() throws Exception {
		mDtoTools = Mockito.mockStatic(DtoTools.class);

		Location mockedLoc = Mockito.mock(Location.class);
		Course mockedCourse = Mockito.mock(Course.class);
		User mockedUser = Mockito.mock(User.class);

		interventions.add(new Intervention(1, "I am lambda Intervention", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0));

		Intervention masterDummy = new Intervention(2, "I am a master Intervention", mockedLoc, mockedCourse,
				mockedUser, InterventionStatus.INTERN, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), true, null, 0);
		interventions.add(masterDummy);

		Intervention slaveDummy = new Intervention(3, "I am a slave Intervention", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.INTERN, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, masterDummy, 0);
		interventions.add(slaveDummy);

		iDtos.add(new InterventionDto(1, "I am lambda Intervention", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		iDtos.add(new InterventionDto(2, "I am a master Intervention", 2, 2, 2, "INTERN", true, LocalDate.now(),
				LocalDate.now().plusDays(2), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, true, 0));
		iDtos.add(new InterventionDto(3, "I am a slave Intervention", 3, 3, 3, "INTERN", true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), 2, false, 0));
	}

	@AfterEach
	public void afterEach() throws Exception {
		if (!mDtoTools.isClosed())
			mDtoTools.close();
	}

	@Test
	void contextLoads() {
		assertThat(interventionService).isNotNull();
	}

	@Test
	void shouldGetInterventionsAndReturnDtos() {
		when(interventionRepository.findAll()).thenReturn(interventions);

		mDtoTools.when(() -> DtoTools.convert(interventions.get(0), InterventionDto.class)).thenReturn(iDtos.get(0));
		mDtoTools.when(() -> DtoTools.convert(interventions.get(1), InterventionDto.class)).thenReturn(iDtos.get(1));
		mDtoTools.when(() -> DtoTools.convert(interventions.get(2), InterventionDto.class)).thenReturn(iDtos.get(2));

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

		mDtoTools.when(() -> DtoTools.convert(interventions.get(0), InterventionDto.class)).thenReturn(iDtos.get(0));
		mDtoTools.when(() -> DtoTools.convert(interventions.get(1), InterventionDto.class)).thenReturn(iDtos.get(1));
		mDtoTools.when(() -> DtoTools.convert(interventions.get(2), InterventionDto.class)).thenReturn(iDtos.get(2));

		List<InterventionDto> page1 = interventionService.getAllInterventions(page, size);
		List<InterventionDto> page2 = interventionService.getAllInterventions(page + 1, size);

		assertThat(page1).isNotNull();
		assertThat(page2).isNotNull();
		assertEquals(page1.size(), 2);
		assertEquals(page2.size(), 1);
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
	void shouldGetInterventionById() {
		long intervId = 2;
		InterventionDto expected = iDtos.get(1);

		when(interventionRepository.findById(intervId)).thenReturn(Optional.of(interventions.get(1)));
		mDtoTools.when(() -> DtoTools.convert(interventions.get(1), InterventionDto.class)).thenReturn(iDtos.get(1));

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

		InterventionDto newIntervDto = new InterventionDto(
				0, "I am a New Intervention", 3, 3, 3, "INTERN", true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0);

		Intervention newInterv = new Intervention(
				0, "I am a New Intervention", null, null, null,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0);

		Intervention savedInterv = new Intervention(
				5, "I am a New Intervention", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0);

		when(interventionService.checkIntegrity(newIntervDto)).thenReturn(true);
//		Mockito.doReturn(true).when(interventionService).checkIntegrity(newIntervDto);

		mDtoTools.when(() -> DtoTools.convert(newIntervDto, Intervention.class)).thenReturn(newInterv);

		when(locationRepository.getOne(3L)).thenReturn(mockedLoc);
		when(courseRepository.getOne(3L)).thenReturn(mockedCourse);
		when(userRepository.getOne(3L)).thenReturn(mockedUser);

		when(interventionRepository.saveAndFlush(newInterv)).thenReturn(savedInterv);

		when(interventionService.saveMemento(newInterv)).thenReturn(null);

		InterventionDto result = interventionService.saveOrUpdate(newIntervDto);

		assertThat(result).isNotNull();
		assertEquals(result, newIntervDto);
		assertEquals(result.getId(), 5L);
	}

	@Test
	void shouldSaveMasterIntervention() throws Exception {
		InterventionDto newIntervDto = new InterventionDto(
				0, "I am a New Intervention", 3, 3, 3, "INTERN", true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0);

		Intervention newInterv = new Intervention(
				0, "I am a New Intervention", null, null, null,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0);

		Intervention savedInterv = new Intervention(
				5, "I am a New Intervention", null, null, null,
				InterventionStatus.INTERN, true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0);


		InterventionDto savedIntervDto = new InterventionDto(
				5, "I am a New Master Intervention", 0, 0, 0, "INTERN",
				true, LocalDate.now().plusDays(7), LocalDate.now().plusDays(10),
				LocalTime.of(9, 0), LocalTime.of(17, 0), 0, true, 0);

		when(interventionService.checkIntegrity(newIntervDto)).thenReturn(true);

		mDtoTools.when(() -> DtoTools.convert(newIntervDto, Intervention.class)).thenReturn(newInterv);

		when(interventionRepository.saveAndFlush(newInterv)).thenReturn(savedInterv);

		when(interventionService.saveMemento(newInterv)).thenReturn(null);

		mDtoTools.when(() -> DtoTools.convert(savedInterv, InterventionDto.class)).thenReturn(savedIntervDto);

		InterventionDto result = interventionService.saveOrUpdate(newIntervDto);

		assertThat(result).isNotNull();
		assertEquals(result, savedIntervDto);
		assertEquals(result.getId(), 5L);
		assertEquals(result.isMaster(), true);
		assertEquals(result.getUserId(), 0);
		assertEquals(result.getCourseId(), 0);
		assertEquals(result.getLocationId(), 0);
	}

	@Test
	void shouldReturnNullWhenUpdateUnknownId() throws Exception {
		InterventionDto interv = iDtos.get(2);
		interv.setId(4);

		when(interventionRepository.existsById(4L)).thenReturn(false);

		assertThat(interventionService.saveOrUpdate(interv)).isNull();
	}

	@Test
	void testGetFromUserByDateRange() {
		when(interventionService.getFromUserByDateRange(0, null, null));
	}

	@Test
	void testGetAllByDateRange() {
		fail("Not yet implemented");
	}

	@Test
	void shouldReturnCountByUserType() throws Exception {
		String userType = "ADMINISTRATIF";
		CountDto expectedCount = new CountDto(interventions.size());
		
		when(interventionRepository.countByUserTypeNoMaster(UserType.valueOf(userType)))
				.thenReturn(3L);

		CountDto result = interventionService.count(userType);
		
		assertThat(result).isNotNull();
		assertEquals(expectedCount, result);
	}
	
	@Test
	void shouldReturnNullWhenCountByWrongUserType() {
		String userType = "BADUSERTYPE";
		assertThat(interventionService.count(userType)).isNull();
	}

	@Test
	void testGetMasterIntervention() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSubInterventions() {
		fail("Not yet implemented");
	}

	@Test
	void testExportCalendarAsICal() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckIntegrity() {
		fail("Not yet implemented");
	}

}
