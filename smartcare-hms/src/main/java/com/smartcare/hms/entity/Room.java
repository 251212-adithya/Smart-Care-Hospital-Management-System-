package com.smartcare.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rooms")
public class Room {

    public enum RoomCategory { GENERAL_WARD, PRIVATE_ROOM, ICU }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @NotBlank(message = "Room number is required")
    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @NotNull(message = "Room category is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "room_category", nullable = false, length = 20)
    private RoomCategory roomCategory;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @Column(name = "daily_charge")
    private Double dailyCharge;

    public Room() {}
    public Room(String roomNumber, RoomCategory roomCategory, Double dailyCharge) {
        this.roomNumber = roomNumber;
        this.roomCategory = roomCategory;
        this.dailyCharge = dailyCharge;
        this.available = true;
    }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public RoomCategory getRoomCategory() { return roomCategory; }
    public void setRoomCategory(RoomCategory roomCategory) { this.roomCategory = roomCategory; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public Double getDailyCharge() { return dailyCharge; }
    public void setDailyCharge(Double dailyCharge) { this.dailyCharge = dailyCharge; }
}
