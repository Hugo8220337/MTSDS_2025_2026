package com.domus.assessments.model.postgres;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "grade_reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GradeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private StudentGrade studentGrade;

    @Column(nullable = false)
    private Long studentId;

    @Column(length = 1000)
    private String studentJustification;

    @Column(length = 1000)
    private String teacherObservations;

    private LocalDateTime meetingDateTime;
    private String meetingLocation;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ReviewStatus {
        PENDING_APPROVAL,
        MEETING_SCHEDULED,
        CONCLUDED
    }
}