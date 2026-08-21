package com.smartcare.hms.service;

import com.smartcare.hms.entity.Room;
import java.util.List;

public interface RoomService {
    Room addRoom(Room room);
    Room getRoomById(Long id);
    List<Room> getAllRooms();
    List<Room> getAvailableRooms();
    Room updateRoom(Long id, Room room);
    void deleteRoom(Long id);
}
