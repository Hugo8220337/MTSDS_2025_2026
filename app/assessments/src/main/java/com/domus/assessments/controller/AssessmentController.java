package com.domus.assessments.controller;

import com.domus.assessments.dto.request.*;
import com.domus.assessments.dto.response.ReviewResponse;
import com.domus.assessments.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    
    @PostMapping("/grades")
    public ResponseEntity<GradeResponse> submitGrade(@RequestBody CreateGradeRequest request) {
        return new ResponseEntity<>(assessmentService.createOrUpdateGrade(request), HttpStatus.CREATED);
    }

    @GetMapping("/grades/student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getStudentGrades(@PathVariable Long studentId) {
        return ResponseEntity.ok(assessmentService.getGradesByStudent(studentId));
    }

    @GetMapping("/grades/moment/{momentId}")
    public ResponseEntity<List<GradeResponse>> getMomentGrades(@PathVariable Long momentId) {
        return ResponseEntity.ok(assessmentService.getGradesByMoment(momentId));
    }

    
    @PostMapping("/reviews/submit-request")
    public ResponseEntity<ReviewResponse> requestReview(@RequestBody SubmitReviewRequest request) {
                Long mockStudentId = 789L; 
        return new ResponseEntity<>(assessmentService.submitReview(mockStudentId, request), HttpStatus.CREATED);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getReview(id));
    }

    @PatchMapping("/reviews/{id}/schedule-meeting")
    public ResponseEntity<ReviewResponse> scheduleMeeting(@PathVariable Long id, @RequestBody ScheduleMeetingRequest request) {
        return ResponseEntity.ok(assessmentService.scheduleMeeting(id, request));
    }

    @PatchMapping("/reviews/{id}/close")
    public ResponseEntity<ReviewResponse> closeReview(@PathVariable Long id, @RequestBody CloseReviewRequest request) {
        return ResponseEntity.ok(assessmentService.closeReview(id, request));
    }
}