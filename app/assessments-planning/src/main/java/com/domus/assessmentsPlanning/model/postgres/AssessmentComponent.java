package com.domus.assessmentsPlanning.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "assessment_components")
public class AssessmentComponent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_plan_id")
    private AssessmentPlan assessmentPlan;

    private String name; 
    private String description;
    
    private Double weight; 
    private Double minGrade; 
    
    private boolean isRepeatable; 
    
    @Enumerated(EnumType.STRING)
    private ComponentType type; 
}

enum ComponentType {
    EXAM, PROJECT, CONTINUOUS
}