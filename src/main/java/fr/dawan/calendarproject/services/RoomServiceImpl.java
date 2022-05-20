package fr.dawan.calendarproject.services;

import fr.dawan.calendarproject.dto.*;
import fr.dawan.calendarproject.entities.Room;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.RoomMapper;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        List<RoomDto> result = new ArrayList<>();
        for (Room r : rooms) {
            result.add(roomMapper.roomToRoomDto(r));
        }
        return result;
    }

    @Override
    public List<RoomDto> getAllRooms(int page, int max, String search) {
        Pageable pagination;

        if (page > -1 && max > 0)
            pagination = PageRequest.of(page, max);
        else
            pagination = Pageable.unpaged();

        List<Room> rooms = roomRepository.findAllByLocationNameContaining("%" + search + "%", pagination).get()
                .collect(Collectors.toList());

        List<RoomDto> result = new ArrayList<>();
        for (Room r : rooms) {
            result.add(roomMapper.roomToRoomDto(r));
        }
        return result;
    }

    @Override
    public CountDto count(String search) {
        return new CountDto(roomRepository.countByLocationNameContaining("%" +search + "%"));
    }

    @Override
    public RoomDto getById(long id) {
        Optional<Room> r = roomRepository.findById(id);
        return r.map(room -> roomMapper.roomToRoomDto(room)).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public RoomDto saveOrUpdate(RoomDto room) {

        if (room.getId() > 0 && !roomRepository.findById(room.getId()).isPresent())
            return null;

        Room r = roomMapper.roomDtoToRoom(room);

        r.setLocation(locationRepository.findById(room.getLocationId()).orElse(null));

        try {
            r = roomRepository.saveAndFlush(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomMapper.roomToRoomDto(r);
    }

}
