package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
		
		interventionsDtos.add(new InterventionDto(0, "I am a new Intervention", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		interventionsDtos.add(new InterventionDto(1, "I am a lamba Intervention", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		interventionsDtos.add(new InterventionDto(1, "I am a lamba Intervention updated", 1, 1, 1, "SUR_MESURE", true, LocalDate.now(),
				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		
		intMementoDtos.add(new InterventionMementoDto(0, "I am a new Intervention", 1, "Bordeaux", 1, "Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));
		intMementoDtos.add(new InterventionMementoDto(0, "I am a lamba Intervention", 1, "Bordeaux", 1, "Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));
		intMementoDtos.add(new InterventionMementoDto(0, "I am a lamba Intervention updated", 1, "Bordeaux", 1, "Java for intermediate level", 1, "Admin Fullname", "SUR_MESURE",
				true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false));
	
		interventionMementos.add(new InterventionMemento(intMementoDtos.get(0)));
		interventionMementos.get(0).setId(1);
		interventionMementos.get(0).setMementoMessage(new MementoMessageDto(1, " has been created by ", email, ""));
		interventionMementos.add(new InterventionMemento(intMementoDtos.get(1)));
		interventionMementos.get(1).setId(2);
		interventionMementos.get(1).setMementoMessage(new MementoMessageDto(2, " has been created by ", email, ""));
		interventionMementos.add(new InterventionMemento(intMementoDtos.get(2)));
		interventionMementos.get(2).setId(3);
		interventionMementos.get(2).setMementoMessage(new MementoMessageDto(3, " has been changed by ", email, ""));
		interventionMementos.add(new InterventionMemento(intMementoDtos.get(1)));
		interventionMementos.get(3).setId(4);
		interventionMementos.get(3).setMementoMessage(new MementoMessageDto(4, " Has been restored ", email, ""));
	}
	
	@Test
	void contextLoads() {
		assertThat(caretaker).isNotNull();
	}

	@Test
	void testAddMementoForANewIntervention() throws Exception {	
		when(interventionMementoMapper.interventionToInterventionMementoDto(interventions.get(0))).thenReturn(intMementoDtos.get(0));
		when(intMementoRepository.countByInterventionId((interventionMementos.get(0)).getState().getInterventionId())).thenReturn(0L);
		when(intMementoRepository.saveAndFlush(any(InterventionMemento.class))).thenReturn(interventionMementos.get(0));
		
		InterventionMemento result = caretaker.addMemento(email, interventions.get(0));
		
		//assertEquals(" has been created by ", result.getMementoMessage().getMessageAction());
	}
	
	@Test
	void testRestoreMemento() throws Exception {
		/*
		when(intMementoRepository.findById(interventionMementos.get(1).getId()).get()).thenReturn(interventionMementos.get(1));
		when(interventionMementoMapper.interventionMementoDtoToIntervention(interventionMementos.get(1).getState())).thenReturn(interventions.get(1));
		when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedLoc));
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedUser));
		when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(mockedCourse));
		when(interventionRepository.findById(any(Long.class))).thenReturn(Optional.of(interventions.get(0)));
		when(interventionMapper.interventionToInterventionDto(interventions.get(1))).thenReturn(interventionsDtos.get(1));
		when(interventionRepository.getOne(interventions.get(1).getId())).thenReturn(interventions.get(1));
		when(interventionRepository.saveAndFlush(interventions.get(1))).thenReturn(interventions.get(1));
		when(intMementoRepository.saveAndFlush(interventionMementos.get(3))).thenReturn(interventionMementos.get(3));
		when(interventionMapper.interventionToInterventionDto(interventions.get(1))).thenReturn(interventionsDtos.get(1));
		
		InterventionDto  result = caretaker.restoreMemento(interventionMementos.get(1).getId(), email);
		*/
		//assertEquals(interventionsDtos.get(1), result);
	}

	@Test
	void testGetMementoById() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllMemento() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllMementoIntInt() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllMementoDates() {
		fail("Not yet implemented");
	}

	@Test
	void testSerializeInterventionMementosAsCSV() {
		fail("Not yet implemented");
	}

	@Test
	void testSerializeInterventionMementosAsCSVByDates() {
		fail("Not yet implemented");
	}

	@Test
	void testCount() {
		fail("Not yet implemented");
	}

	@Test
	void testFilterMemento() {
		fail("Not yet implemented");
	}

	@Test
	void testCountFilter() {
		fail("Not yet implemented");
	}

	@Test
	void testGetLastBeforeMemento() {
		fail("Not yet implemented");
	}

}
