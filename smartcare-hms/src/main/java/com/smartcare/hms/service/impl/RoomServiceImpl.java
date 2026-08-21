package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Room;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.RoomRepository;
import com.smartcare.hms.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room addRoom(Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().isBlank()) {
            throw new InvalidInputException("Room number cannot be empty");
        }
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    @Override
    public Room updateRoom(Long id, Room updated) {
        Room existing = getRoomById(id);
        existing.setRoomNumber(updated.getRoomNumber());
        existing.setRoomCategory(updated.getRoomCategory());
        existing.setDailyCharge(updated.getDailyCharge());
        existing.setAvailable(updated.isAvailable());
        return roomRepository.save(existing);
    }

    @Override
    public void deleteRoom(Long id) {
        Room existing = getRoomById(id);
        roomRepository.delete(existing);
    }
}
