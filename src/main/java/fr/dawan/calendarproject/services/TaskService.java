package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.TaskDto;

public interface TaskService {
	
	
	List<TaskDto> getAllTask();
	
	List<TaskDto> getAllTaskForUserId(long userId);
	
	List<TaskDto> getAllTaskForInternventionId(long interventionId);
	
	List<TaskDto> getAllBySlugLikeOrTitleLike(String search);
	
	List<TaskDto> getAllTaskBetweenOptionalUser(LocalDate start, LocalDate end, long userId);
	
	int fetchAllDG2Task(String email, String password, LocalDate dateStart, LocalDate dateEnd) throws Exception;
	
	TaskDto getTaskById(long id);
	
	CountDto count(String search);
	
	CountDto countByUserType(String type);
	
	TaskDto saveOrUpdate(TaskDto taskDto);
	
	void deleteById(long id);

}
