package com.domus.applications.valueObjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
public class ApplicationPeriod {
    private LocalDate startDate;
    private LocalDate endDate;

    protected ApplicationPeriod() {} // JPA

    public ApplicationPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    public boolean hasStarted() {
        return LocalDate.now().isAfter(startDate);
    }

    public boolean hasEnded() {
        return LocalDate.now().isAfter(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationPeriod)) return false;
        ApplicationPeriod that = (ApplicationPeriod) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
