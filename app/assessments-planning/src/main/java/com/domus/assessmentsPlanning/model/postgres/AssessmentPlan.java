package com.domus.assessmentsPlanning.model.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "assessment_plans")
@Getter
@Setter
public class AssessmentPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // External Reference to "Gestão Académica"
    @Column(nullable = false)
    private Long courseUnitId; 

    // Timeline
    @Column(nullable = false)
    private String academicYear; 
    
    @Column(nullable = false)
    private Integer semester; 

    // Workflow Status
    @Enumerated(EnumType.STRING)
    private PlanStatus status; 

    // Hours (Snapshotted or Overridden)
    private Integer theoreticalHours;
    private Integer practicalLabHours;
    
    // Link to MongoDB Document
    private String contentId; 

    @OneToMany(mappedBy = "assessmentPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanTeamMember> team = new ArrayList<>(); 

    @OneToMany(mappedBy = "assessmentPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentComponent> components = new ArrayList<>();
}
