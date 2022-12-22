package fr.dawan.calendarproject.services;

import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.TaskDg2Dto;
import fr.dawan.calendarproject.dto.TaskDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Task;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserType;
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
	public List<TaskDto> getAllTask() throws Exception{
		List<Task> tasks = taskRepository.findAll();
		return taskMapper.taskListToTaskDtoList(tasks);	

	}
	
	
	
	
	/**
	 * Fetches all existing tasks for given user id
	 * 
	 * @param userId : Id of the user whose tasks are fetched
	 * @return Returns a list of TaskDto  
	 */
	@Override
	public List<TaskDto> getAllTaskForUserId(long userId) throws Exception{
		
		return taskMapper.taskListToTaskDtoList(taskRepository.findByUserId(userId));			
	}
	
	
	/**
	 * Fetches all existing tasks for given intervention id
	 * 
	 * @param interventionId : Id of the intervention whose tasks are fetched
	 * @return Returns a list of TaskDto  
	 */
	@Override
	public List<TaskDto> getAllTaskForInternventionId(long interventionId) throws Exception{
		
		return taskMapper.taskListToTaskDtoList(taskRepository.findByInterventionId(interventionId));			
	}
	
	
	/**
	 * Fetches all existing tasks whose slug contains the value of search string passed
	 * 
	 * @param search: String containing the value to search
	 * @return Returns a list of TaskDto  
	 */
	@Override
	public List<TaskDto> getAllBySlugLikeOrTitleLike(String search) throws Exception{

		return taskMapper.taskListToTaskDtoList(taskRepository.findAllBySlugContainingOrTitleContaining(search, search));
	}
	
	
	/**
	 * Searches for a Task with a given id and returns it as TaskDto if it exists
	 * 
	 * @param id : id of the task searched
	 * @return Returns a TaskDto if it exists 
	 * @return Returns null if no task was found 
	 */
	@Override
	public TaskDto getTaskById(long id) throws Exception{
		
		
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
	 * @return CountDto : Returns the number of tasks that match with the search word
	 * 
	 */
	@Override
	public CountDto count(String search) throws Exception{

		return new CountDto(taskRepository.findAllBySlugContainingOrTitleContaining(search, search).size());

	}

	/**
	 * Saves a new Task or Updates an existing Task
	 * 
	 * @param taskDto : Task to be saved/updated
	 * @return TaskDto : The saved task after beeing updated/created
	 * 
	 */
	@Override
	public TaskDto saveOrUpdate(TaskDto taskDto) throws Exception{
		
		TaskDto result = null;
		
		//Setting task to persist
		Task taskToPersist = taskMapper.taskDtoToTask(taskDto);
		
		//Setting User
		if(taskDto.getUser() != null) {
			User u = userRepository.findById(taskDto.getUserId()).orElse(null);
			taskToPersist.setUser(u);

		}
		//Setting intervention associated to the Task
		if(taskDto.getIntervention() != null) {
			Intervention intervention = interventionRepository.findById(taskDto.getInterventionId()).orElse(null);
			taskToPersist.setIntervention(intervention);
		}		

		
		//Calculation of the duration of the task
		long duration = ChronoUnit.DAYS.between(taskToPersist.getBeginDate(), taskToPersist.getEndDate());
		taskToPersist.setDuration(duration == 0 ? 1 : duration);
		
		
		// Update
		if(taskDto.getId() > 0 && taskRepository.existsById(taskDto.getId())) {
			
			Optional<Task> existingTask = taskRepository.findById(taskDto.getId());
			
			//If title changes the slug is then changed
			if(existingTask.isPresent() && !existingTask.get().getTitle().equals(taskToPersist.getTitle())) {
				
				taskToPersist.setSlug(slugCreator(taskDto.getTitle(), taskDto.getBeginDate().toString()));
			}
			
		}
		
		// Create
		if(taskDto.getId() == 0) {
			taskToPersist.setSlug(slugCreator(taskDto.getTitle(), taskDto.getBeginDate().toString()));
		}
		
		taskToPersist = taskRepository.saveAndFlush(taskToPersist);
	
		result = taskMapper.taskToTaskDto(taskToPersist);
		
		return result;
	}
	
	
	/**
	 * Creates a unique slug for tasks
	 * @param title : the title of the task we want to create a Slug for
	 * @param dateStart : the date of beggining of the task
	 * @return the constructed slug as String
	 */
	public String slugCreator(String title, String dateStart) throws Exception{
		
		
		StringBuilder slugBuilder = new StringBuilder();
		
		for (char character: title.toLowerCase().toCharArray()) {
			
			if(character == ' ') {
				slugBuilder.append('-');
			}else{
				slugBuilder.append(character);
			}
				
		}
		
		String constructedSlug = slugBuilder.toString() + '-' + dateStart;
		
		Optional<Task> slug = taskRepository.findBySlug(constructedSlug);
		int countDuplicates = 0;
		String resultSlug = constructedSlug;
		
		while(slug.isPresent()) {
			
			countDuplicates++;
			resultSlug = constructedSlug + '-' + countDuplicates;
			slug = taskRepository.findBySlug(resultSlug);
			
		}
		
		return resultSlug;
	}

	/**
	 * Delete a Task with given id
	 * 
	 * @param id :  long of the task to delete
	 * 
	 */
	@Override
	public void deleteById(long id) throws Exception{

		taskRepository.deleteById(id);
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
				
				
				Intervention inter = interventionRepository.findByIdDg2(taskDg2.getInterventionId()).orElse(null);
				
				
				//TODO Ask for Slug of the intervention associated to the task
				if(inter != null) {
					task.setIntervention(inter);
				}
				 
				Optional<User> user = userRepository.findByEmployeeIdDg2(taskDg2.getEmployeeId());
				
				if(user.isPresent()) {
					task.setUser(user.get());
				}
				
				long duration = ChronoUnit.DAYS.between(task.getBeginDate(), task.getEndDate());
				task.setDuration(duration == 0 ? 1 : duration);
				

				
				Optional<Task> duplicatesSlug = taskRepository.findBySlug(task.getSlug());
				Optional<Task> duplicateIdDg2 = taskRepository.findByTaskIdDg2(task.getTaskIdDg2());
			
				
				if(!duplicatesSlug.isPresent() && !duplicateIdDg2.isPresent()) {
					
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


	/**
	 * Fetches all tasks from dawan API and persists them into the database
	 * 
	 * @Param start A string defining a user email
	 * @Param end A string defining a user password
	 * @Param userId A long 
	 * 
	 * @exception Exception returns an exception if the request fails
	 */
	@Override
	public List<TaskDto> getAllTaskAssignedBetweenDatesAndUserType(LocalDate start, LocalDate end, String userType) throws Exception{
		
		UserType type = Enum.valueOf(UserType.class , userType);
		
		return taskMapper.taskListToTaskDtoList(taskRepository.getAllAssignedBetweenDatesAndUserType(start, end, type));
					
	}

	/**
	 * 
	 * Counts the number of Tasks having User type passed as parameter
	 * 
	 * @param type : UserType value that corresponds to the type of users we want to count tasks
	 * @return CountDto : Returns the number of tasks that have the UserType passed
	 * 
	 */
	@Override
	public CountDto countByUserType(String type) throws Exception{
		
		UserType userType = Enum.valueOf(UserType.class, type);
		
		return new CountDto(taskRepository.findByUserType(userType).size());
	}


	
}
