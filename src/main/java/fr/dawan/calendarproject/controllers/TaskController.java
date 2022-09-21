package fr.dawan.calendarproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

		TaskDto task = taskService.getTaskById(id);
		if(task != null) {
			return ResponseEntity.status(HttpStatus.OK).body(task);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with id : " + id + " not found");
		}
		
	}
	
	//GET
	@GetMapping( value = "/user/{id}", produces = "application/json" )
	public List<TaskDto> getAllByUserId(@PathVariable(value = "id") long id){
		
		return taskService.getAllTaskForUserId(id);
		
	}
	
	//GET
	@GetMapping( value = "/user/{id}", produces = "application/json" )
	public List<TaskDto> getAllByInterventionId(@PathVariable(value = "id") long id){
		
		return taskService.getAllTaskForInternventionId(id);
		
	}
	
	
	
	
}
