package com.domus.applications.entities;

import com.domus.applications.valueObjects.ApplicationPeriod;
import com.domus.applications.valueObjects.ApplicationProcessStatus;
import com.domus.applications.valueObjects.ApplicationProcessType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "competition")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationProcessType type;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Embedded
    private ApplicationPeriod period;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationProcessStatus status;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // prevents infinite serialization loops
    private List<CompetitionPhase> phases = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected Competition() {} // JPA

    public Competition(String name, ApplicationProcessType type,
                       String academicYear, ApplicationPeriod period) {
        this.name = name;
        this.type = type;
        this.academicYear = academicYear;
        this.period = period;
        this.status = ApplicationProcessStatus.CONFIGURATION;
    }

    // Domain methods
    public CompetitionPhase addPhase(int phaseNumber, ApplicationPeriod period, int totalVacancies) {
        if (this.status == ApplicationProcessStatus.CLOSED) {
            throw new IllegalStateException("Cannot add phases to a closed application process");
        }

        CompetitionPhase phase = new CompetitionPhase(this, phaseNumber, period, totalVacancies);
        phases.add(phase);
        return phase;
    }

    public void open() {
        if (this.status != ApplicationProcessStatus.CONFIGURATION) {
            throw new IllegalStateException("Can only open processes in CONFIGURATION status");
        }
        if (phases.isEmpty()) {
            throw new IllegalStateException("Cannot open process without phases");
        }
        this.status = ApplicationProcessStatus.OPEN;
    }

    public void startReview() {
        if (this.status != ApplicationProcessStatus.OPEN) {
            throw new IllegalStateException("Can only review processes in OPEN status");
        }
        this.status = ApplicationProcessStatus.UNDER_REVIEW;
    }

    public void startRanking() {
        if (this.status != ApplicationProcessStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Can only rank after review");
        }
        this.status = ApplicationProcessStatus.RANKING;
    }

    public void publish() {
        if (this.status != ApplicationProcessStatus.RANKING) {
            throw new IllegalStateException("Can only publish after ranking");
        }
        this.status = ApplicationProcessStatus.PUBLISHED;
    }

    public void close() {
        this.status = ApplicationProcessStatus.CLOSED;
    }

}