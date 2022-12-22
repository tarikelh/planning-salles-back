package fr.dawan.calendarproject.mapper;

//import fr.dawan.calendarproject.dto.RoomDG2Dto;
import fr.dawan.calendarproject.dto.RoomDto;
import fr.dawan.calendarproject.entities.Room;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.RoomRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { RoomRepository.class, LocationRepository.class })
public interface RoomMapper {

    @Mapping(target = "locationId", source = "location.id")
    RoomDto roomToRoomDto(Room room);

    @Mapping(target = "location", ignore = true)
    Room roomDtoToRoom(RoomDto roomDto);

    /*
    TODO Mapping when roomDG2 imported
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idDg2", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "fullCapacity", source = "fullCapacity")
    @Mapping(target = "available", source = "available")
    @Mapping(target = "location", source = "locationId")
    Room roomDG2ToRoom(RoomDG2Dto roomDG2Dto);
    */
}
