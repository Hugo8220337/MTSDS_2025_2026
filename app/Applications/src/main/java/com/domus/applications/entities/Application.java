package com.domus.applications.entities;

import com.domus.applications.valueObjects.ApplicantInfo;
import com.domus.applications.valueObjects.ApplicationOrigin;
import com.domus.applications.valueObjects.ApplicationStatus;
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
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phase_id", nullable = false)
    private Long phaseId;

    @Embedded
    private ApplicantInfo applicantInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.DRAFT; // Default status is DRAFT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationOrigin origin = ApplicationOrigin.LOCAL; // Default origin is LOCAL

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("preferenceOrder ASC")
    private List<CourseOption> courseOptions = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationDocument> documents = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    protected Application() {} // JPA

    public Application(Long phaseId, ApplicantInfo applicantInfo) {
        this.phaseId = phaseId;
        this.applicantInfo = applicantInfo;
    }

    public Application(Long phaseId, ApplicantInfo applicantInfo, ApplicationOrigin origin) {
        this.phaseId = phaseId;
        this.applicantInfo = applicantInfo;
        this.origin = origin;
    }

    // Domain methods
    public CourseOption addCourseOption(Long courseId, int preferenceOrder) {
        if (this.status != ApplicationStatus.DRAFT) {
            throw new IllegalStateException("Cannot modify submitted application");
        }

        CourseOption option = new CourseOption(this, courseId, preferenceOrder);
        courseOptions.add(option);
        return option;
    }

    public CourseOption addCourseOption(CourseOption courseOption) {
        if (this.status != ApplicationStatus.DRAFT) {
            throw new IllegalStateException("Cannot modify submitted application");
        }

        courseOptions.add(courseOption);
        return courseOption;
    }

    public ApplicationDocument addDocument(String documentType, String documentUrl) {
        if (this.status == ApplicationStatus.APPROVED ||
                this.status == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Cannot add documents to finalized application");
        }

        ApplicationDocument document = new ApplicationDocument(this, documentType, documentUrl);
        documents.add(document);
        return document;
    }

    public void saveAsDraft() {
        this.status = ApplicationStatus.DRAFT;
    }

    public void submit() {
        if (this.status != ApplicationStatus.DRAFT) {
            throw new IllegalStateException("Only draft applications can be submitted");
        }
        if (courseOptions.isEmpty()) {
            throw new IllegalStateException("Cannot submit application without course options");
        }

        this.status = ApplicationStatus.SUBMITTED;
        this.submissionDate = LocalDateTime.now();
    }

    public boolean isSubmitted() {
        return this.status != ApplicationStatus.DRAFT;
    }

    public boolean isFinalized() {
        return this.status == ApplicationStatus.APPROVED ||
               this.status == ApplicationStatus.REJECTED ||
               this.status == ApplicationStatus.SELECTED;
    }

    public void startReview() {
        if (this.status != ApplicationStatus.SUBMITTED) {
            throw new IllegalStateException("Only submitted applications can be reviewed");
        }
        this.status = ApplicationStatus.UNDER_REVIEW;
    }

    public void markAsComplete() {
        if (this.status != ApplicationStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Only applications under review can be marked complete");
        }
        this.status = ApplicationStatus.COMPLETE;
    }

    public void markAsIncomplete() {
        if (this.status != ApplicationStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Only applications under review can be marked incomplete");
        }
        this.status = ApplicationStatus.INCOMPLETE;
    }

    public void approve() {
        if (this.status != ApplicationStatus.COMPLETE) {
            throw new IllegalStateException("Only complete applications can be approved");
        }
        this.status = ApplicationStatus.APPROVED;
    }

    public void reject() {
        this.status = ApplicationStatus.REJECTED;
    }

    public void select() {
        if (this.status != ApplicationStatus.APPROVED) {
            throw new IllegalStateException("Only approved applications can be selected");
        }
        this.status = ApplicationStatus.SELECTED;
    }

}