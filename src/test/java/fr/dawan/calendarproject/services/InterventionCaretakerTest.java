package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.mapper.InterventionMapper;
import fr.dawan.calendarproject.mapper.InterventionMementoMapper;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class InterventionCaretakerTest {
	
	@Autowired
	private InterventionCaretaker caretaker;
	
	@MockBean
	private InterventionMapper interventionMapper;
	
	@MockBean
	private InterventionMementoMapper interventionMementoMapper;
	
	@MockBean
	private InterventionService interventionService;

	@MockBean
	private InterventionMementoRepository intMementoRepository;

	@MockBean
	private InterventionRepository interventionRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private LocationRepository locationRepository;

	@MockBean
	private CourseRepository courseRepository;
	
	private String email = "admin@dawan.fr";
	private Location mockedLoc;
	private Course mockedCourse;
	private User mockedUser;
	
	private List<Intervention> interventions = new ArrayList<Intervention>();
	private List<InterventionDto> interventionsDtos = new ArrayList<InterventionDto>();
	private List<InterventionMementoDto> intMementoDtos = new ArrayList<InterventionMementoDto>();
	private List<InterventionMemento> interventionMementos = new ArrayList<InterventionMemento>();
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		mockedLoc = Mockito.mock(Location.class);
		mockedCourse = Mockito.mock(Course.class);
		mockedUser = Mockito.mock(User.class);
		
		interventions.add(new Intervention(0, "I am a new Intervention", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0));
		interventions.add(new Intervention(1, "I am a lamba Intervention", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0));
		interventions.add(new Intervention(1, "I am a lamba Intervention updated", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, null, 0));
		interventions.add(new Intervention(2, "I am a lamba Intervention with Master Intervention", mockedLoc, mockedCourse, mockedUser,
				InterventionStatus.SUR_MESURE, true, LocalDate.now(), LocalDate.now().plusDays(5),
				LocalTime.of(9, 0), LocalTime.of(17, 0), false, interventions.get(1), 0));
		
		interventionsDtos.add(new InterventionDto(0, "I am a new Intervention", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		interventionsDtos.add(new InterventionDto(1, "I am a lamba Intervention", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		interventionsDtos.add(new InterventionDto(1, "I am a lamba Intervention updated", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		interventionsDtos.add(new InterventionDto(2, "I am a lamba Intervention with Master Intervention", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), interventions.get(1).getId(), false, 0));
		
		intMementoDtos.add(new InterventionMementoDto(0, "I am a new Intervention", 1, "Bordeaux", 1, "Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));
		intMementoDtos.add(new InterventionMementoDto(0, "I am a lamba Intervention", 1, "Bordeaux", 1, "Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));
		intMementoDtos.add(new InterventionMementoDto(1, "I am a lamba Intervention updated", 1, "Bordeaux", 1, "Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));
		intMementoDtos.add(new InterventionMementoDto(2, "I am a lamba Intervention  with Master Intervention", 1, "Bordeaux", 1, "Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), interventions.get(1).getId(), false));
		intMementoDtos.add(new InterventionMementoDto(3, "I am a lamba Intervention with no location, user and course", 0, "", 0, "", 0, "", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));
	
		interventionMementos.add(new InterventionMemento(1, intMementoDtos.get(0), new MementoMessageDto(1, " has been created by ", email, ""), 0));
		interventionMementos.add(new InterventionMemento(2, intMementoDtos.get(1), new MementoMessageDto(2, " has been created by ", email, ""), 0));
		interventionMementos.add(new InterventionMemento(3, intMementoDtos.get(2), new MementoMessageDto(3, " has been changed by ", email, ""), 0));
		interventionMementos.add(new InterventionMemento(4, intMementoDtos.get(1), new MementoMessageDto(4, " Has been restored ", email, ""), 0));
		interventionMementos.add(new InterventionMemento(5, intMementoDtos.get(1), new MementoMessageDto(5, " has been deleted by ", email, ""), 0));
		interventionMementos.add(new InterventionMemento(6, intMementoDtos.get(3), new MementoMessageDto(6, " has been created by ", email, ""), 0));
		interventionMementos.add(new InterventionMemento(7, intMementoDtos.get(4), new MementoMessageDto(7, " has been created by ", email, ""), 0));
	}
	
	@Test
	void contextLoads() {
		assertThat(caretaker).isNotNull();
	}

	@Test
	void shouldAddMementoForANewIntervention() throws Exception {	
		when(interventionMementoMapper.interventionToInterventionMementoDto(interventions.get(0))).thenReturn(intMementoDtos.get(0));
		when(intMementoRepository.countByInterventionId((interventionMementos.get(0)).getState().getInterventionId())).thenReturn(0L);
		when(intMementoRepository.saveAndFlush(any(InterventionMemento.class))).thenReturn(interventionMementos.get(0));
		
		InterventionMemento result = caretaker.addMemento(email, interventions.get(0));
		
		assertEquals(" has been created by ", result.getMementoMessage().getMessageAction());
	}
	
	@Test
	void shouldAddMementoForAnEditIntervention() throws Exception {	
		when(interventionMementoMapper.interventionToInterventionMementoDto(interventions.get(2))).thenReturn(intMementoDtos.get(2));
		when(intMementoRepository.countByInterventionId((interventionMementos.get(2)).getState().getInterventionId())).thenReturn(3L);
		when(interventionRepository.existsById((interventionMementos.get(2)).getState().getInterventionId())).thenReturn(true);
		when(intMementoRepository.getLastInterventionMemento(interventionMementos.get(2).getState().getInterventionId())).thenReturn(interventionMementos.get(1));
		when(intMementoRepository.saveAndFlush(any(InterventionMemento.class))).thenReturn(interventionMementos.get(2));
		
		InterventionMemento result = caretaker.addMemento(email, interventions.get(2));
		
		assertEquals(" has been changed by ", result.getMementoMessage().getMessageAction());
	}
	
	@Test
	void shouldAddMementoForADeleteIntervention() throws Exception {	
		when(interventionMementoMapper.interventionToInterventionMementoDto(interventions.get(1))).thenReturn(intMementoDtos.get(1));
		when(intMementoRepository.countByInterventionId((interventionMementos.get(1)).getState().getInterventionId())).thenReturn(1L);
		when(interventionRepository.existsById((interventionMementos.get(1)).getState().getInterventionId())).thenReturn(false);
		
		when(intMementoRepository.saveAndFlush(any(InterventionMemento.class))).thenReturn(interventionMementos.get(4));
		
		InterventionMemento result = caretaker.addMemento(email, interventions.get(1));
		
		assertEquals(" has been deleted by ", result.getMementoMessage().getMessageAction());
	}
	
	@Test
	void shouldRestoreMemento() throws Exception {
		when(intMementoRepository.findById(interventionMementos.get(1).getId())).thenReturn(Optional.of(interventionMementos.get(1)));
		when(interventionMementoMapper.interventionMementoDtoToIntervention(interventionMementos.get(1).getState())).thenReturn(interventions.get(1));
		
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedUser));
		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedCourse));
			
		when(interventionService.checkIntegrity(interventionsDtos.get(1))).thenReturn(true);
		when(interventionRepository.getOne(interventions.get(1).getId())).thenReturn(interventions.get(1));
		when(interventionRepository.saveAndFlush(interventions.get(1))).thenReturn(interventions.get(1));
		when(intMementoRepository.saveAndFlush(interventionMementos.get(3))).thenReturn(interventionMementos.get(3));
		when(interventionMapper.interventionToInterventionDto(interventions.get(1))).thenReturn(interventionsDtos.get(1));
		
		
		InterventionDto result = caretaker.restoreMemento(interventionMementos.get(1).getId(), email);
		
		assertEquals(interventionsDtos.get(1), result);
	}
	
	@Test
	void shouldRestoreMementoWithMasterEvent() throws Exception {
		when(intMementoRepository.findById(interventionMementos.get(5).getId())).thenReturn(Optional.of(interventionMementos.get(5)));
		when(interventionMementoMapper.interventionMementoDtoToIntervention(interventionMementos.get(5).getState())).thenReturn(interventions.get(3));
		
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedUser));
		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedCourse));
		
		
		when(interventionRepository.findById(interventionMementos.get(5).getState().getMasterInterventionId())).thenReturn(Optional.of(interventions.get(1)));
		
		when(interventionService.checkIntegrity(interventionsDtos.get(3))).thenReturn(true);
		when(interventionRepository.getOne(interventions.get(3).getId())).thenReturn(interventions.get(3));
		when(interventionRepository.saveAndFlush(interventions.get(3))).thenReturn(interventions.get(3));
		when(intMementoRepository.saveAndFlush(interventionMementos.get(5))).thenReturn(interventionMementos.get(5));
		when(interventionMapper.interventionToInterventionDto(interventions.get(3))).thenReturn(interventionsDtos.get(3));
		
		
		InterventionDto result = caretaker.restoreMemento(interventionMementos.get(5).getId(), email);
		
		assertEquals(interventionsDtos.get(3), result);
	}

	@Test
	void shouldGetMementoById() {
		when(intMementoRepository.findById(interventionMementos.get(5).getId())).thenReturn(Optional.of(interventionMementos.get(5)));
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedUser));
		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedCourse));
		
		InterventionMemento result1 = caretaker.getMementoById(interventionMementos.get(5).getId());
		assertEquals(interventionMementos.get(5), result1);
		
		when(intMementoRepository.findById(interventionMementos.get(6).getId())).thenReturn(Optional.of(interventionMementos.get(6)));
		InterventionMemento result2 = caretaker.getMementoById(interventionMementos.get(6).getId());
		assertEquals(interventionMementos.get(6), result2);
	}
	
	@Test
	void shouldGetMementoByIdNull() {
		when(intMementoRepository.findById(0L)).thenReturn(Optional.empty());
		
		InterventionMemento result = caretaker.getMementoById(0L);
		
		assertEquals(null, result);
	}

	@Test
	void shouldGetAllMemento() {
		when(intMementoRepository.findAll()).thenReturn(interventionMementos);
		
		List<InterventionMemento> result = caretaker.getAllMemento();
		
		assertEquals(interventionMementos, result);
	}

	@Test
	void testGetPaginatedMemento() {
		when(intMementoRepository.findAllByOrderByIdDesc(PageRequest.of(0, 2))).thenReturn(interventionMementos.subList(0, 2));
		
		List<InterventionMemento> result = caretaker.getAllMemento(0, 2);
		
		assertEquals(interventionMementos.subList(0, 2), result);
	}

	@Test
	void shouldGetAllMementoByDates() {
		LocalDate start = LocalDate.now();
		LocalDate end = LocalDate.now().plusDays(5);
		when(intMementoRepository.findAllByDateCreatedStateBetween(start.atStartOfDay(), end.plusDays(1).atStartOfDay())).thenReturn(interventionMementos);
		
		List<InterventionMemento> result = caretaker.getAllMementoDates(start, end);
		
		assertEquals(interventionMementos, result);
	}

	@Test
	void shouldReturnCount() {
		CountDto expectedCount = new CountDto(7);
		when(intMementoRepository.count()).thenReturn(7L);
		
		CountDto result = caretaker.count();
		
		assertThat(result).isNotNull();
		assertEquals(expectedCount.getNb(), result.getNb());
	}

	@Test
	void shouldFilterMemento() {
		long interventionId = 1;
		int page = 0;
		int size = 4;
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now().plusDays(5);
		when(intMementoRepository.filterByIntIdAndDates(interventionId, start, end, PageRequest.of(page, size))).thenReturn(interventionMementos.subList(1, 4));
		
		List<InterventionMemento> result = caretaker.filterMemento(interventionId, start, end, page, size);
		
		assertEquals(interventionMementos.subList(1, 4), result);

	}

	@Test
	void shouldReturnCountFilter() {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now().plusDays(5);
		CountDto expectedCount = new CountDto(1);
		when(intMementoRepository.countFilter(3L, start, end)).thenReturn(1L);
		
		CountDto result = caretaker.countFilter(3L, start, end);
		
		assertThat(result).isNotNull();
		assertEquals(expectedCount.getNb(), result.getNb());
	}

	@Test
	void shouldGetLastBeforeMemento() {
		long interventionId = 1;
		long interventionMementoId = 3;
		when(intMementoRepository.getLastBeforeIntMemento(interventionId, interventionMementoId)).thenReturn(interventionMementos.get(1));
		
		InterventionMemento result = caretaker.getLastBeforeMemento(interventionId, interventionMementoId);
		
		assertEquals(interventionMementos.get(1), result);
	}

}
