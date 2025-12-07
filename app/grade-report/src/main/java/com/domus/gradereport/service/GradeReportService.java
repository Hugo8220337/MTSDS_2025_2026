package com.domus.gradereport.service;

import com.domus.gradereport.client.AcademinsApiClient;
import com.domus.gradereport.dto.request.CreateGradeReportRequest;
import com.domus.gradereport.dto.request.PublishReportRequest;
import com.domus.gradereport.dto.request.SignReportRequest;
import com.domus.gradereport.dto.response.StudentResponse;
import com.domus.gradereport.model.postgres.GradeReport;
import com.domus.gradereport.model.postgres.StudentGrade;
import com.domus.gradereport.repository.postgres.GradeReportRepository;
import com.domus.gradereport.repository.postgres.StudentGradeRepository;
import com.domus.gradereport.util.enums.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeReportService {

    private final GradeReportRepository reportRepository;
    private final StudentGradeRepository gradeRepository;
    private final AcademinsApiClient academinsClient; // Injected instance

    @Transactional
    public GradeReport createReport(CreateGradeReportRequest request) {
        log.info("Creating grade report for UC: {} and Teacher: {}", request.getUnidade_curricular_id(), request.getTeacher_id());
        
        GradeReport report = GradeReport.builder()
                .teacherId(request.getTeacher_id()) // Set Teacher ID
                .curricularUnitId(request.getUnidade_curricular_id())
                .schoolYearId(request.getAno_letivo_id())
                .type(request.getTipo())
                .epoch(request.getEpoca())
                .state(ReportState.CREATED)
                .build();
        
        return reportRepository.save(report);
    }

    @Transactional
    public GradeReport generateResults(Long id) {
        GradeReport report = getReport(id);

        if (report.getState() != ReportState.CREATED) {
             throw new IllegalStateException("Report already generated or closed.");
        }


        String codeCU = String.valueOf(report.getCurricularUnitId());
        String schoolYear = String.valueOf(report.getSchoolYearId()); 


        List<StudentResponse> students = academinsClient.getStudentsInUcByYear(
                report.getTeacherId(), 
                codeCU, 
                schoolYear
        );

        if (students.isEmpty()) {
            log.warn("No students found for Teacher {}, CU {}, Year {}", report.getTeacherId(), codeCU, schoolYear);
        }

        List<StudentGrade> grades = students.stream()
                .map(student -> StudentGrade.builder()
                        .gradeReport(report)
                        .studentId(student.getStudentId())
                        .studentName(student.getFullName()) 
                        .gradeValue(null) 
                        .build())
                .collect(Collectors.toList());
        
        report.getStudentGrades().clear();
        report.getStudentGrades().addAll(grades);
        report.setState(ReportState.GENERATED);
        
        return reportRepository.save(report);
    }
    
    public List<StudentGrade> getReportResults(Long id) {
        return gradeRepository.findByGradeReportId(id);
    }

    @Transactional
    public GradeReport publishReport(Long id, PublishReportRequest request) {
        GradeReport report = getReport(id);
        
        if ("PROVISORIA".equalsIgnoreCase(request.getEstado())) {
            report.setState(ReportState.PROVISIONAL);
        } else if ("DEFINITIVA".equalsIgnoreCase(request.getEstado())) {
            report.setState(ReportState.DEFINITIVE);
        }
        
        return reportRepository.save(report);
    }

    @Transactional
    public void closeReport(Long id) {
        GradeReport report = getReport(id);
        report.setState(ReportState.CLOSED);
        reportRepository.save(report);
    }

    @Transactional
    public GradeReport signReport(Long id, SignReportRequest request) {
        GradeReport report = getReport(id);
        report.getSignatures().add(request.getTipo_assinatura());
        return reportRepository.save(report);
    }
    
    public GradeReport getReport(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade Report not found: " + id));
    }

    public List<GradeReport> getReportsByCurricularUnit(Long ucId) {
        return reportRepository.findByCurricularUnitId(ucId);
    }
    
    public List<GradeReport> getReportsByTeacher(Long teacherId) {
        return reportRepository.findByTeacherId(teacherId);
    }
}