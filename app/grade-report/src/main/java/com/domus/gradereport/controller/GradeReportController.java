package com.domus.gradereport.controller;

import com.domus.gradereport.dto.request.CreateGradeReportRequest;
import com.domus.gradereport.dto.request.PublishReportRequest;
import com.domus.gradereport.dto.request.SignReportRequest;
import com.domus.gradereport.model.postgres.GradeReport;
import com.domus.gradereport.model.postgres.StudentGrade;
import com.domus.gradereport.service.GradeReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grade-reports")
@RequiredArgsConstructor
public class GradeReportController {

    private final GradeReportService service;

    @PostMapping("/create-grade-report")
    public ResponseEntity<GradeReport> createGradeReport(@RequestBody CreateGradeReportRequest request) {
        return ResponseEntity.ok(service.createReport(request));
    }

    @PostMapping("/{id}/generate")
    public ResponseEntity<GradeReport> generateResults(@PathVariable Long id) {
        return ResponseEntity.ok(service.generateResults(id));
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<GradeReport> publishReport(@PathVariable Long id, @RequestBody PublishReportRequest request) {
        return ResponseEntity.ok(service.publishReport(id, request));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeReport(@PathVariable Long id) {
        service.closeReport(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/sign")
    public ResponseEntity<GradeReport> signReport(@PathVariable Long id, @RequestBody SignReportRequest request) {
        return ResponseEntity.ok(service.signReport(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeReport> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReport(id));
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<StudentGrade>> getResults(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReportResults(id));
    }

    @GetMapping("/curricular-unit/{ucId}")
    public ResponseEntity<List<GradeReport>> getByCurricularUnit(@PathVariable Long ucId) {
        return ResponseEntity.ok(service.getReportsByCurricularUnit(ucId));
    }
}