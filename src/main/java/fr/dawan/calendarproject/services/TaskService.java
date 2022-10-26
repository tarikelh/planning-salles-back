package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.TaskDto;

public interface TaskService {
	
	
	List<TaskDto> getAllTask() throws Exception;
	
	List<TaskDto> getAllTaskForUserId(long userId) throws Exception;
	
	List<TaskDto> getAllTaskForInternventionId(long interventionId) throws Exception;
	
	List<TaskDto> getAllBySlugLikeOrTitleLike(String search) throws Exception;
	
	List<TaskDto> getAllTaskAssignedBetween(LocalDate start, LocalDate end) throws Exception;
	
	int fetchAllDG2Task(String email, String password, LocalDate dateStart, LocalDate dateEnd) throws Exception;
	
	TaskDto getTaskById(long id) throws Exception;
	
	CountDto count(String search) throws Exception;
	
	CountDto countByUserType(String type) throws Exception;
	
	TaskDto saveOrUpdate(TaskDto taskDto) throws Exception;
	
	void deleteById(long id) throws Exception;

	
}
