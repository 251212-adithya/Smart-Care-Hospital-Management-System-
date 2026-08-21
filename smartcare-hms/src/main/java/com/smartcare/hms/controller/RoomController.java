package com.smartcare.hms.controller;

import com.smartcare.hms.entity.Room;
import com.smartcare.hms.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> add(@Valid @RequestBody Room room) {
        return new ResponseEntity<>(roomService.addRoom(room), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Room getById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @GetMapping
    public List<Room> getAll() {
        return roomService.getAllRooms();
    }

    @GetMapping("/available")
    public List<Room> getAvailable() {
        return roomService.getAvailableRooms();
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @Valid @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
