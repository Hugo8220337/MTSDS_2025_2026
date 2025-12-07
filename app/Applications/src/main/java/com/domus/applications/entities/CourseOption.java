package com.domus.applications.entities;

import com.domus.applications.valueObjects.CourseOptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "course_options")
public class CourseOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "preference_order", nullable = false)
    private Integer preferenceOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseOptionStatus status = CourseOptionStatus.UNDER_REVIEW;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected CourseOption() {} // JPA

    public CourseOption(Application application, Long courseId, Integer preferenceOrder) {
        this.application = application;
        this.courseId = courseId;
        this.preferenceOrder = preferenceOrder;
    }

    public CourseOption(Application application, Long courseId, Integer preferenceOrder, CourseOptionStatus status) {
        this.application = application;
        this.courseId = courseId;
        this.preferenceOrder = preferenceOrder;
        this.status = status;
    }

    // Domain methods
    public void approve() {
        this.status = CourseOptionStatus.APPROVED;
    }

    public void reject() {
        this.status = CourseOptionStatus.REJECTED;
    }

    public void place() {
        if (this.status != CourseOptionStatus.APPROVED) {
            throw new IllegalStateException("Only approved options can be placed");
        }
        this.status = CourseOptionStatus.PLACED;
    }
}