package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.TaskDto;
import fr.dawan.calendarproject.entities.Task;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.TaskMapper;
import fr.dawan.calendarproject.repositories.TaskRepository;

@Service
@Transactional
public class TaskServiceImpl implements TaskService{

	
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskMapper taskMapper;
	
	
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
	 * @param l An object representing an Task.
	 * 
	 */
	public void checkUniqueness(TaskDto taskDto) {
		
		List<Task> duplicates = taskRepository.findAllBySlugEquals(taskDto.getSlug());
		
		
		if(!duplicates.isEmpty()) {
			
			Set<APIError> errors = new HashSet<>();
		
			String instanceClass = duplicates.get(0).getClass().toString();
			String path = "api/task";
			
			
			errors.add(new APIError(505, instanceClass, "This task is not unique", "Task slug already exists" ,path));
		
			throw new EntityFormatException(errors);
			
		}
		
	}
}
