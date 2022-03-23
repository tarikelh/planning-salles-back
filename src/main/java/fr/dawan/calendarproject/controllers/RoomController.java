package fr.dawan.calendarproject.controllers;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.RoomDto;
import fr.dawan.calendarproject.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        RoomDto room = roomService.getById(id);

        if (room != null)
            return ResponseEntity.status(HttpStatus.OK).body(room);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with Id " + id + " Not Found");
    }

    @GetMapping(value = { "/count" }, produces = "application/json")
    public CountDto countFilter(@RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return roomService.count(search);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") long id) {
        try {
            roomService.deleteById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Room with Id " + id + " Deleted");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with Id " + id + " Not Found");
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public RoomDto save(@RequestBody RoomDto room) {
        return roomService.saveOrUpdate(room);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> update(@RequestBody RoomDto room) {
        RoomDto updatedRoom = roomService.saveOrUpdate(room);

        if (updatedRoom != null)
            return ResponseEntity.status(HttpStatus.OK).body(updatedRoom);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with Id " + room.getId() + " Not Found");
    }

}
