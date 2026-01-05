package com.domus.gradereport.model.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "student_grades", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"grade_report_id", "student_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_report_id", nullable = false)
    private GradeReport gradeReport;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "student_name")
    private String studentName;

    
    @Column(name = "grade_value")
    private BigDecimal gradeValue;

    @Column(length = 500)
    private String observation;
}