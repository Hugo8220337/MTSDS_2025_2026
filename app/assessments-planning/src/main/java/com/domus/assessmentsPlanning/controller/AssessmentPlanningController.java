package com.domus.assessmentsPlanning.controller;

import com.domus.assessmentsPlanning.dto.response.AssessmentPlanResponseDTO;
import com.domus.assessmentsPlanning.dto.request.CreatePlanRequestDto;
import com.domus.assessmentsPlanning.model.mongo.PlanContent;
import com.domus.assessmentsPlanning.service.AssessmentPlanServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assessments-planning")
@RequiredArgsConstructor
public class AssessmentPlanningController {

    private final AssessmentPlanServiceInterface service;

        @PostMapping("/create-assessment-planning")
    public ResponseEntity<AssessmentPlanResponseDTO> createPlan(@RequestBody CreatePlanRequestDto request) {
        AssessmentPlanResponseDTO createdPlan = service.createPlan(request);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

        @GetMapping("/{id}")
    public ResponseEntity<AssessmentPlanResponseDTO> getPlan(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFullPlan(id));
    }

        @PutMapping("/{id}/update-content")
    public ResponseEntity<Void> updateContent(@PathVariable Long id, @RequestBody PlanContent content) {
        service.updateContent(id, content);
        return ResponseEntity.noContent().build();
    }

        @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submitForApproval(@PathVariable Long id) {
        service.submitForApproval(id);
        return ResponseEntity.ok().build();
    }
}