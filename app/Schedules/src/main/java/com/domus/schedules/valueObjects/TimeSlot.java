package com.domus.schedules.valueObjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Embeddable
public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;

    protected TimeSlot() {}

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getDurationInMinutes() {
        return (int) Duration.between(startTime, endTime).toMinutes();
    }

    public boolean overlapsWith(TimeSlot other) {
        if (other == null) return false;
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot timeSlot)) return false;
        return Objects.equals(startTime, timeSlot.startTime) &&
               Objects.equals(endTime, timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime + " (" + getDurationInMinutes() + " min)";
    }
}

