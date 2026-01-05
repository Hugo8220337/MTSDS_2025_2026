package com.domus.schedules.valueObjects;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ClassroomLocation {
    private String building;
    private Integer floor;
    private String roomNumber;

    protected ClassroomLocation() {}

    public ClassroomLocation(String building, Integer floor, String roomNumber) {
        if (building == null || building.isBlank()) {
            throw new IllegalArgumentException("Building cannot be empty");
        }
        if (floor == null) {
            throw new IllegalArgumentException("Floor cannot be null");
        }
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException("Room number cannot be empty");
        }

        this.building = building;
        this.floor = floor;
        this.roomNumber = roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public Integer getFloor() {
        return floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getFullLocation() {
        return building + " - Floor " + floor + " - Room " + roomNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassroomLocation that)) return false;
        return Objects.equals(building, that.building) &&
               Objects.equals(floor, that.floor) &&
               Objects.equals(roomNumber, that.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(building, floor, roomNumber);
    }

    @Override
    public String toString() {
        return getFullLocation();
    }
}


