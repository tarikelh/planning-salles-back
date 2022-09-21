package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.TaskDto;
import fr.dawan.calendarproject.entities.Task;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Mapper(componentModel = "spring", uses = { InterventionRepository.class , UserRepository.class })
public interface TaskMapper {

	
	@Mapping(target="userId", source="user.id" )
	@Mapping(target="interventionId", source="intervention.id" )
	TaskDto taskToTaskDto(Task task);
	
	
	@Mapping(target="user", ignore=true )
	@Mapping(target="intervention", ignore=true )
	@Mapping(target = "beginDate", source = "beginDate", dateFormat = "yyyy-MM-dd")
	@Mapping(target = "endDate", source = "endDate", dateFormat = "yyyy-MM-dd")
	Task taskDtoToTask(TaskDto taskDto);
}
