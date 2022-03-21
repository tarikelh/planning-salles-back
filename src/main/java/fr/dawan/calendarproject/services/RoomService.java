package fr.dawan.calendarproject.services;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.RoomDto;

import java.util.List;

public interface RoomService {

    List<RoomDto> getAllRooms();

    List<RoomDto> getAllRooms(int page, int max,String search);

    CountDto count(String search);

    RoomDto getById(long id);

    void deleteById(long id);

    RoomDto saveOrUpdate(RoomDto room);

    boolean checkUniqness(RoomDto room);
}
