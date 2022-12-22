package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.dto.AdvancedInterventionDto;
import fr.dawan.calendarproject.dto.AdvancedInterventionDto2;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.dto.TaskDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionFollowed;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.Task;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.TaskMapper;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.TaskRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class TaskServiceTest {

	@Autowired
	private TaskService taskService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private InterventionRepository interventionRepository;
	
	@MockBean
	private TaskMapper taskMapper;
	
	@MockBean
	private TaskRepository taskRepository;
	
	private Task task = new Task();
	private TaskDto taskDto = new TaskDto();
	private User user = new User();
	private UserDto userDto = new UserDto();
	private Intervention intervention = new Intervention();
	private AdvancedInterventionDto2 interventionDto = new AdvancedInterventionDto2();
	private Task existingTask = new Task();
	private Location location = new Location();


	@BeforeEach
	void before() {
		
		location = new Location(5L, "Toulouse", "France", "rose", true, 1);
		
		user = new User(3L, 3L, 3L, "firstname", "lastname", location, "name@dawan.fr", new HashSet<Skill>(), 
				UserType.FORMATEUR, UserCompany.DAWAN, "gdfsdfzaq.png", LocalDate.parse("2022-10-01"), new HashSet<InterventionFollowed>() , 1);
		
		
		intervention = new Intervention(3L, 3L, "slugtest", "optionSlug", "comment", location, new Course(), user, 1, InterventionStatus.TP, 
				true, LocalDate.parse("2022-10-01"), LocalDate.parse("2022-10-10"), LocalTime.of(7,0), LocalTime.of(17, 0), new Intervention(), 
				false, 1L, "customer", new HashSet<InterventionFollowed>(), 1 );
		
		task = new Task(1L, 10L, "testTask", user, intervention, "testTask1", LocalDate.parse("2022-10-01"), LocalDate.parse("2022-10-10"), 10, 0);
		
		taskDto = new TaskDto(1L, 10L, "testTask" , interventionDto, userDto, "testTask1", LocalDate.parse("2022-10-01"),
				LocalDate.parse("2022-10-10"), 10, 0);
		
		existingTask = new Task(1L, 10L, "existingTask", user, intervention, "testTask1", LocalDate.parse("2022-10-01"), 
				LocalDate.parse("2022-10-10"), 10, 0);
		
		
		userDto = new UserDto(3L, 3L, 3L, "firstname", "lastname", 5L, "name@dawan.fr", 
				"FORMATEUR", "DAWAN", "gdfsdfzaq.png", "2022-10-01", 1);
		
		
		interventionDto = new AdvancedInterventionDto2(3L, 3L, "slugtest", "optionSlug", "comment", new LocationDto(), new CourseDto(), userDto, 
				1, "TP",  true, LocalDate.parse("2022-10-01"), LocalDate.parse("2022-10-10"), LocalTime.of(7,0), LocalTime.of(17, 0), 
				new InterventionDto(), false,"customer", 1, new ArrayList<AdvancedInterventionDto>());
	}
	
	@Test
	void contextLoads() {
		assertThat(taskService).isNotNull();
	}
	
	@Test
	void shouldCreateNewTask() throws Exception {
		
		taskDto.setId(0L);
		
		when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
		when(userRepository.findById(taskDto.getUserId())).thenReturn(Optional.of(user));
		when(interventionRepository.findById(taskDto.getInterventionId())).thenReturn(Optional.of(intervention));
		when(taskRepository.saveAndFlush(task)).thenReturn(task);
		when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);
		
		TaskDto result = taskService.saveOrUpdate(taskDto);
		
		assertEquals(result, taskDto);
	}
	
	
	@Test
	void shouldUpdateTaskWithBlanksInTitle() throws Exception {
		
		task = new Task(1L, 10L, "testTasktoUpdate", user, intervention, "testTask1", LocalDate.parse("2022-10-01"), LocalDate.parse("2022-10-10"), 10, 0);
		
		taskDto = new TaskDto(1L, 10L, "test Task to Update" , interventionDto, userDto, "testTask1", LocalDate.parse("2022-10-01"),
				LocalDate.parse("2022-10-10"), 10, 0);
		
		when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
		when(userRepository.findById(taskDto.getUserId())).thenReturn(Optional.of(user));
		when(interventionRepository.findById(taskDto.getInterventionId())).thenReturn(Optional.of(intervention));
		when(taskRepository.existsById(taskDto.getId())).thenReturn(true);
		when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(existingTask));
		when(taskRepository.saveAndFlush(task)).thenReturn(task);
		when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);
		
		
		TaskDto result = taskService.saveOrUpdate(taskDto);
		
		assertEquals(result, taskDto);
	}
	
	
	
	@Test
	void shouldCreateNewTaskWithoutUserAndWithoutInterventionAndDuration0() throws Exception {
		
		task = new Task(1L, 10L, "testTask", null, null, "testTask1", LocalDate.parse("2022-10-10"), LocalDate.parse("2022-10-10"), 10, 0);
		
		taskDto = new TaskDto(1L, 10L, "testTask" , null, null, "testTask1", LocalDate.parse("2022-10-10"),
				LocalDate.parse("2022-10-10"), 10, 0);
		
		
		when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
		when(taskRepository.saveAndFlush(task)).thenReturn(task);
		when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);
		
		TaskDto result = taskService.saveOrUpdate(taskDto);
		
		assertThat(result.getUser()).isNull();
		assertThat(result.getIntervention()).isNull();
		
	}
	
	
	@Test 
	void shouldUpdateTaskWithSameTitle() throws Exception {
		
		task = new Task(1L, 10L, "testTasktoUpdate", user, intervention, "testTask1", LocalDate.parse("2022-10-01"), LocalDate.parse("2022-10-10"), 10, 0);
		
		taskDto = new TaskDto(1L, 10L, "testTasktoUpdate" , interventionDto, userDto, "testTask1", LocalDate.parse("2022-10-01"),
				LocalDate.parse("2022-10-10"), 10, 1);
		
		existingTask = new Task(1L, 10L, "testTasktoUpdat", user, intervention, "testTask1", LocalDate.parse("2022-10-01"), 
				LocalDate.parse("2022-10-10"), 10, 1);
		
		when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
		when(userRepository.findById(taskDto.getUserId())).thenReturn(Optional.of(user));
		when(interventionRepository.findById(taskDto.getInterventionId())).thenReturn(Optional.of(intervention));
		when(taskRepository.existsById(taskDto.getId())).thenReturn(true);
		when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(existingTask));
		when(taskRepository.findBySlug("testtasktoupdate-2022-10-01")).thenReturn(Optional.of(existingTask));
		when(taskRepository.saveAndFlush(task)).thenReturn(task);
		when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);
		
		
		TaskDto result = taskService.saveOrUpdate(taskDto);
		
		assertEquals(result, taskDto);
		
	}

}
