package fr.dawan.calendarproject.mapper;

import fr.dawan.calendarproject.dto.RoomDto;
import fr.dawan.calendarproject.entities.Room;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.RoomRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { RoomRepository.class, LocationRepository.class, LocationMapper.class })
public interface RoomMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "fullCapacity", source = "fullCapacity")
    @Mapping(target = "partialCapacity", source = "partialCapacity")
    @Mapping(target = "isAvailable", source = "available")
    @Mapping(target = "locationId", source = "location.id")
    RoomDto roomToRoomDto(Room room);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "fullCapacity", source = "fullCapacity")
    @Mapping(target = "partialCapacity", source = "partialCapacity")
    @Mapping(target = "available", source = "available")
    @Mapping(target = "location", source = "locationId")
    Room roomDtoToRoom(RoomDto roomDto);

}
