package fr.dawan.calendarproject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.TaskDg2Dto;
import fr.dawan.calendarproject.dto.TaskDto;
import fr.dawan.calendarproject.entities.Task;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.TaskRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Mapper(componentModel = "spring", uses = { InterventionRepository.class , UserRepository.class, TaskRepository.class, UserMapper.class })
public interface TaskMapper {
	
	@Mapping(target="interventionId", source="intervention.id" )
	TaskDto taskToTaskDto(Task task);
	
	
	@Mapping(target="user", ignore=true )
	@Mapping(target="intervention", ignore=true )
	Task taskDtoToTask(TaskDto taskDto);
	
	
	@Mapping(target="id", ignore = true)
	@Mapping(target="taskIdDg2", source="taskId" )
	@Mapping(target="user", ignore = true)
	@Mapping(target="intervention", ignore = true)
	@Mapping(target="slug", source="registrationSlug")
	@Mapping(target="beginDate", source="beginAt", dateFormat = "yyyy-MM-dd")
	@Mapping(target="endDate", source="finishAt", dateFormat = "yyyy-MM-dd")
	@Mapping(target="version", ignore = true)
	@Mapping(target="duration", ignore = true)
	Task taskDg2DtoToTask( TaskDg2Dto taskDg2Dto );
	
	
	List<TaskDto> taskListToTaskDtoList(List<Task> tasks);
	
}
