package com.domus.schedules.valueObjects;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class AcademicPeriod {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer semester;

    protected AcademicPeriod() {}

    public AcademicPeriod(LocalDate startDate, LocalDate endDate, Integer semester) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (semester == null || semester < 1 || semester > 2) {
            throw new IllegalArgumentException("Semester must be 1 or 2");
        }

        this.startDate = startDate;
        this.endDate = endDate;
        this.semester = semester;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getSemester() {
        return semester;
    }

    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean overlapsWith(AcademicPeriod other) {
        if (other == null) return false;
        return this.startDate.isBefore(other.endDate) && this.endDate.isAfter(other.startDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcademicPeriod that)) return false;
        return Objects.equals(startDate, that.startDate) &&
               Objects.equals(endDate, that.endDate) &&
               Objects.equals(semester, that.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, semester);
    }

    @Override
    public String toString() {
        return "Semester " + semester + " (" + startDate + " to " + endDate + ")";
    }
}

