package fr.dawan.calendarproject.services;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.TaskDg2Dto;
import fr.dawan.calendarproject.dto.TaskDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Task;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.TaskMapper;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.TaskRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class TaskServiceImpl implements TaskService{

	
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private InterventionRepository interventionRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	/**
	 * Fetches all existing tasks from task table
	 * 
	 * @return Returns a list of TaskDto
	 */
	@Override
	public List<TaskDto> getAllTask() {

		List<Task> tasks = taskRepository.findAll();
		
		return convertTaskListToTaskDtoList(tasks);			

	}
	
	
	/**
	 * Fetches all existing tasks for given user id
	 * 
	 * @param userId : Id of the user whose tasks are fetched
	 * @return Returns a list of TaskDto  
	 */
	@Override
	public List<TaskDto> getAllTaskForUserId(long userId) {

		List<Task> tasks = taskRepository.findByUserId(userId);
		
		return convertTaskListToTaskDtoList(tasks);			
	}
	
	
	/**
	 * Fetches all existing tasks for given intervention id
	 * 
	 * @param interventionId : Id of the intervention whose tasks are fetched
	 * @return Returns a list of TaskDto  
	 */
	@Override
	public List<TaskDto> getAllTaskForInternventionId(long interventionId) {

		List<Task> tasks = taskRepository.findByInterventionId(interventionId);
		
		return convertTaskListToTaskDtoList(tasks);			
	}
	
	
	/**
	 * Fetches all existing tasks whose slug contains the value of search string passed
	 * 
	 * @param search: String containing the value to search
	 * @return Returns a list of TaskDto  
	 */
	@Override
	public List<TaskDto> getAllBySlugLike(String search) {

		List<Task> tasks = taskRepository.findAllBySlugContaining(search);

		return convertTaskListToTaskDtoList(tasks);
	}
	
	
	/**
	 * Searches for a Task with a given id and returns it as TaskDto if it exists
	 * 
	 * @param id : id of the task searched
	 * @return Returns a TaskDto if it exists 
	 * @return Returns null if no task was found 
	 */
	@Override
	public TaskDto getTaskById(long id) {
		
		
		Optional<Task> task = taskRepository.findById(id);
		
		if(task.isPresent()) {
			return taskMapper.taskToTaskDto(task.get());
		}

		return null;
	}

	/**
	 * 
	 * Counts the number of Tasks containing the value passed in String search
	 * 
	 * @param Search : A string of the value which is searched
	 * @return CountDto : Returns the number of locations that match with the search word
	 * 
	 */
	@Override
	public CountDto count(String search) {

		return new CountDto(taskRepository.findAllBySlugContaining(search).size());

	}

	/**
	 * Saves a new Task or Updates an existing Task
	 * 
	 * @param taskDto : Task to be saved/updated
	 * @return TaskDto : The saved task after beeing updated/created
	 * 
	 */
	@Override
	public TaskDto saveOrUpdate(TaskDto taskDto) {

		checkUniqueness(taskDto);
		
		if(taskDto.getId() > 0 && !taskRepository.existsById(taskDto.getId())) {
			return null;
		}
		
		Task t = taskMapper.taskDtoToTask(taskDto);
		
		t = taskRepository.saveAndFlush(t);
	
		return taskMapper.taskToTaskDto(t);
	}

	/**
	 * Delete a Task with given id
	 * 
	 * @param id :  long of the task to delete
	 * 
	 */
	@Override
	public void deleteById(long id) {

		taskRepository.deleteById(id);
	}
	
	/**
	 * Converts a list of Task to a list of TaskDto
	 * 
	 * @param tasks : List of Task to be converted
	 * @return List<TaskDto> : List of converted TaskDto
	 */
	public List<TaskDto> convertTaskListToTaskDtoList(List<Task> tasks){
		
		List<TaskDto> result = new ArrayList<>(); 
		for (Task task : tasks) {
			
			result.add(taskMapper.taskToTaskDto(task));
		}
		return result;
	}

	
	/**
	 * Checks whether a newly registered task is valid and throws an exception if not
	 * 
	 * @param taskDto An object representing a Task.
	 * 
	 */
	public void checkUniqueness(TaskDto taskDto) {
		
		Optional<List<Task>> duplicates = taskRepository.findBySlugOrTaskIdDg2(taskDto.getSlug(), taskDto.getTaskIdDg2());
		
		
		if(duplicates.isPresent() && taskDto.getId() == 0) {
			
			Set<APIError> errors = new HashSet<>();
		
			String instanceClass = duplicates.get().get(0).getClass().toString();
			String path = "api/task";
			
			
			errors.add(new APIError(505, instanceClass, "This task is not unique", "Task slug already exists" ,path));
		
			throw new EntityFormatException(errors);
			
		}
		
	}

	/**
	 * Fetches all tasks from dawan API and persists them into the database
	 * 
	 * @Param email A string defining a user email
	 * @Param pwd A string defining a user password
	 * 
	 * @exception Exception returns an exception if the request fails
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int fetchAllDG2Task(String email, String password, LocalDate dateStart, LocalDate dateEnd) throws Exception {
		
		List<TaskDg2Dto> tasksResultJson;
		int count = 0;
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		URI url = new URI(String.format("https://dawan.org/api2/planning/tasks/" + "%s/%s", dateStart.toString(), dateEnd.toString() ));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + password);
		
		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		
		if(repWs.getStatusCode() == HttpStatus.OK) {
			
			String json = repWs.getBody();
			TaskDg2Dto[] responseArray = objectMapper.readValue(json, TaskDg2Dto[].class);
			
			tasksResultJson = Arrays.asList(responseArray);
			
			
			for(TaskDg2Dto taskDg2 : tasksResultJson) {
			
				taskDg2.setBeginAt(taskDg2.getBeginAt().substring(0, 10));
				taskDg2.setFinishAt(taskDg2.getFinishAt().substring(0, 10));
					
				Task task = taskMapper.taskDg2DtoToTask(taskDg2);
				
				
				Optional<List<Intervention>> inter = interventionRepository.findByIdDg2(taskDg2.getInterventionId());
				
				if(inter.isPresent()) {
					task.setIntervention(inter.get().get(0));
				}
				 
				Optional<User> user = userRepository.findByEmployeeIdDg2(taskDg2.getEmployeeId());
				
				if(user.isPresent()) {
					task.setUser(user.get());
				}

				
				Optional<List<Task>> duplicates = taskRepository.findBySlugOrTaskIdDg2(task.getSlug(), task.getTaskIdDg2());
				
				if(!duplicates.isPresent()) {
					
					try {
						taskRepository.saveAndFlush(task);
						count++;

					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		}else {
			throw new Exception("ResponseEntity from the webservice WDG2 not correct");
		}
		
		return count;
	}
}
