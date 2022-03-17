package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.ResourceDto;
import fr.dawan.calendarproject.entities.Resource;
import fr.dawan.calendarproject.repositories.RoomRepository;


@Mapper(componentModel = "spring", uses = { RoomRepository.class})
public interface ResourceMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "quantity", source = "quantity")
	@Mapping(target = "roomId", source = "room.id")
	@Mapping(target = "version", source = "version")
	ResourceDto resourceToResourceDto(Resource resource);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "quantity", source = "quantity")
	@Mapping(target = "room.id", source = "roomId")
	@Mapping(target = "version", source = "version")
	Resource resourceDtoToResource(ResourceDto resource);
}
