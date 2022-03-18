package fr.dawan.calendarproject.mapper;

import fr.dawan.calendarproject.dto.AdvancedRoomDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.RoomDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Room;
import fr.dawan.calendarproject.repositories.RoomRepository;
import fr.dawan.calendarproject.repositories.SkillRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { RoomRepository.class })
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
    @Mapping(target = "location.id", source = "locationId")
    /*@Mapping(target = "bookings", ignore = true)*/
    Room roomDtoToRoom(RoomDto roomDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "fullCapacity", source = "fullCapacity")
    @Mapping(target = "partialCapacity", source = "partialCapacity")
    @Mapping(target = "isAvailable", source = "available")
    @Mapping(target = "locationId", source = "location.id")
    /*@Mapping(target = "bookings", source = "bookings")*/
    AdvancedRoomDto roomToAdvancedRoomDto(Room room);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "fullCapacity", source = "fullCapacity")
    @Mapping(target = "partialCapacity", source = "partialCapacity")
    @Mapping(target = "available", source = "available")
    @Mapping(target = "location.id", source = "locationId")
    /*@Mapping(target = "bookings", source = "bookings")*/
    Room advancedRoomDtoToRoom(AdvancedRoomDto advancedRoomDto);

}
