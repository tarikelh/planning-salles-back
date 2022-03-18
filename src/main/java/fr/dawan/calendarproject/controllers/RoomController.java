package fr.dawan.calendarproject.controllers;

import fr.dawan.calendarproject.dto.RoomDto;
import fr.dawan.calendarproject.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping(produces = "application/json")
    public List<RoomDto> getAll() {
        return roomService.getAllRooms();
    }

    @GetMapping(value = {"/pagination"}, produces = "application/json")
    public List<RoomDto> getAllPagination(
            @RequestParam(value = "page", defaultValue = "-1", required = false) int page,
            @RequestParam(value = "max", defaultValue = "-1", required = false) int max,
            @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return roomService.getAllRooms(page, max, search);
    }

}
