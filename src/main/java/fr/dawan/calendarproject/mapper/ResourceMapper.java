package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.ResourceDto;
import fr.dawan.calendarproject.entities.Resource;
import fr.dawan.calendarproject.repositories.RoomRepository;


@Mapper(componentModel = "spring", uses = { RoomRepository.class})
public interface ResourceMapper {

	@Mapping(target = "roomId", source = "room.id")
	ResourceDto resourceToResourceDto(Resource resource);

	@Mapping(target = "room.id", source = "roomId")
	Resource resourceDtoToResource(ResourceDto resource);
}
