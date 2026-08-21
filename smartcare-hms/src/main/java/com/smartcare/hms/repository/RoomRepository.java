package com.smartcare.hms.repository;

import com.smartcare.hms.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailableTrue();
    List<Room> findByRoomCategory(Room.RoomCategory category);
}
