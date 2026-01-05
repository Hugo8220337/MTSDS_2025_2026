package com.domus.applications.entities;

import com.domus.applications.valueObjects.ApplicationPeriod;
import com.domus.applications.valueObjects.PhaseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "application_phases")
public class CompetitionPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Column(name = "phase_number", nullable = false)
    private Integer phaseNumber;

    @Embedded
    private ApplicationPeriod period;

    @Column(name = "total_vacancies", nullable = false)
    private Integer totalVacancies;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhaseStatus status = PhaseStatus.CONFIGURATION;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected CompetitionPhase() {
    } // JPA

    public CompetitionPhase(Competition competition, Integer phaseNumber,
                            ApplicationPeriod period, Integer totalVacancies) {
        if (totalVacancies <= 0) {
            throw new IllegalArgumentException("Total vacancies must be positive");
        }
        this.competition = competition;
        this.phaseNumber = phaseNumber;
        this.period = period;
        this.totalVacancies = totalVacancies;
    }

    // Domain methods
    public void open() {
        if (this.status != PhaseStatus.CONFIGURATION) {
            throw new IllegalStateException("Can only open phases in CONFIGURATION status");
        }
        this.status = PhaseStatus.OPEN;
    }

    public void startReview() {
        if (this.status != PhaseStatus.OPEN) {
            throw new IllegalStateException("Can only review open phases");
        }
        this.status = PhaseStatus.UNDER_REVIEW;
    }

    public void rank() {
        if (this.status != PhaseStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Can only rank after review");
        }
        this.status = PhaseStatus.RANKED;
    }

    public void publish() {
        if (this.status != PhaseStatus.RANKED) {
            throw new IllegalStateException("Can only publish after ranking");
        }
        this.status = PhaseStatus.PUBLISHED;
    }

    public void close() {
        this.status = PhaseStatus.CLOSED;
    }
}
