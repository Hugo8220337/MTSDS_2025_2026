package com.domus.gradereport.model.postgres;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.domus.gradereport.util.enums.ReportEpoch;
import com.domus.gradereport.util.enums.ReportState;
import com.domus.gradereport.util.enums.ReportType;
import com.domus.gradereport.util.enums.SignatureType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grade_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "curricular_unit_id", nullable = false)
    private Long curricularUnitId;

    @Column(name = "school_year_id", nullable = false)
    private Long schoolYearId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportEpoch epoch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReportState state = ReportState.CREATED;

    @ElementCollection
    @CollectionTable(name = "grade_report_signatures", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "signature_type")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<SignatureType> signatures = new ArrayList<>();

    @OneToMany(mappedBy = "gradeReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StudentGrade> studentGrades = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}