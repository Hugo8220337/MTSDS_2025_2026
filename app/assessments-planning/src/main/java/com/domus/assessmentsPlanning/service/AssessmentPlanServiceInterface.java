package com.domus.assessmentsPlanning.service;

import com.domus.assessmentsPlanning.dto.response.AssessmentPlanResponseDTO;
import com.domus.assessmentsPlanning.dto.request.CreatePlanRequestDto;
import com.domus.assessmentsPlanning.model.mongo.PlanContent;

public interface AssessmentPlanServiceInterface {
    
    AssessmentPlanResponseDTO createPlan(CreatePlanRequestDto request);
    
    AssessmentPlanResponseDTO getFullPlan(Long planId);

    void updateContent(Long planId, PlanContent content);
    void submitForApproval(Long planId);
}