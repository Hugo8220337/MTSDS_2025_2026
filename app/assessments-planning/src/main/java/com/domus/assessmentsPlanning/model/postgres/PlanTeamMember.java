package com.domus.assessmentsPlanning.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plan_team_members")
public class PlanTeamMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_plan_id")
    private AssessmentPlan assessmentPlan;

    @Column(nullable = false)
    private Long professorId; // External ID from IAM

    @Enumerated(EnumType.STRING)
    private ProfessorRole role;
}