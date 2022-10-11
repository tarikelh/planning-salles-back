package fr.dawan.calendarproject.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.TaskDto;
import fr.dawan.calendarproject.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	
	@Autowired
	private TaskService taskService;
	
	//GET
	@GetMapping(produces = "application/json")
	public List<TaskDto> getAll(){
		
		return taskService.getAllTask();	
	}

	//GET
	@GetMapping( value = "/{id}", produces = "application/json")
	public ResponseEntity<Object> getById(@PathVariable(value = "id") long id){

		ResponseEntity<Object> result = null;

		TaskDto task = taskService.getTaskById(id);
		if(task != null) {
			result = ResponseEntity.status(HttpStatus.OK).body(task);
		}else {
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with id : " + id + " not found");
		}
		return result; 
		
	}
	
	//GET
	@GetMapping( value = "/user/{id}", produces = "application/json" )
	public List<TaskDto> getAllByUserId(@PathVariable(value = "id") long id){
		
		return taskService.getAllTaskForUserId(id);
		
	}
	
	//GET
	@GetMapping( value = "/intervention/{id}", produces = "application/json" )
	public List<TaskDto> getAllByInterventionId(@PathVariable(value = "id") long id){
		
		return taskService.getAllTaskForInternventionId(id);
		
	}
	
	
	//GET
	@GetMapping( value = "/search/{search}", produces= "application/json")
	public List<TaskDto> getAllBySlugLikeOrTitleLike(@PathVariable(value= "search") String search){
		
		return taskService.getAllBySlugLikeOrTitleLike(search);
	}
	
	
	
	//GET
	@GetMapping( value = "/count/{search}", produces = "application/json")
	public CountDto getCountTasks(@PathVariable(value="search") String search) {
		
		return taskService.count(search);
	}
	
	
	//Post
	@PostMapping(consumes="application/json" , produces= "application/json")
	public ResponseEntity<Object> saveTask(@RequestBody TaskDto taskDto){
		
		ResponseEntity<Object> result = null;
		
		TaskDto response = taskService.saveOrUpdate(taskDto);

		if(response != null) {
			result = ResponseEntity.status(HttpStatus.CREATED).body(response);
		}else {
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task cannot be created. Either it already exists or fields are incorrectly filled");
		}
		
		return result;
	}
	
	//PUT
	@PutMapping(consumes="application/json", produces="application/json")
	public ResponseEntity<Object> updateTask(@RequestBody TaskDto taskDto) {
		
		ResponseEntity<Object> result = null;

		
		TaskDto response = taskService.saveOrUpdate(taskDto);

		if(response != null) {
			result = ResponseEntity.status(HttpStatus.CREATED).body(response);
		}else {
			result =  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not update selected task");
		}

		return result;
		
	}
	
	//DELETE
	@DeleteMapping(value= "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") long id){
		
		ResponseEntity<String> result = null;

		try {
			taskService.deleteById(id);
			result = ResponseEntity.status(HttpStatus.OK).body("Task with id : " + id + " succesfully deleted");
		} catch (Exception e) {
			result = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find task with id : " + id );
		}
		
		return result;
	}
	
	
	//GET 
	@GetMapping(value="/dg2/{start}/{end}", produces="application/json")
	public ResponseEntity<String> fetchAllFromDg2(@RequestHeader Map<String,String> headers, 
													@PathVariable("start") String start, 
													@PathVariable("end") String end  ){
		
		ResponseEntity<String> result = null;
		
		String auth = headers.get("x-auth-token");
		try {
			
			int count = taskService.fetchAllDG2Task(auth.split(":")[0], auth.split(":")[1] , LocalDate.parse(start), LocalDate.parse(end));
			result = ResponseEntity.status(HttpStatus.OK).body("Successfully imported " + count + " tasks ");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching data from the webservice");
		}
		
		return result;
	}
	
	//GET
	@GetMapping(value={"/between/{start}/{end}"}, produces="application/json" )
	public List<TaskDto> getAllBetweenDatesOptionalUser(@PathVariable("start") String start, 
															@PathVariable("end") String end){
		
		return  taskService.getAllTaskAssignedBetween(LocalDate.parse(start), LocalDate.parse(end));

	}
	
	
	//GET
	@GetMapping( value = "/count/userType/{type}", produces = "application/json")
	public CountDto getCountByUserType(@PathVariable(value="type") String type) {
		
		return taskService.countByUserType(type);
	}
		
	
}
