package com.domus.assessments.service;

import com.domus.assessments.config.AcademinsApiClient;
import com.domus.assessments.config.MQConfig;
import com.domus.assessments.dto.request.*;
import com.domus.assessments.dto.response.*;
import com.domus.assessments.messages.SendNotificationMessage;
import com.domus.assessments.model.mongo.GradeLog;
import com.domus.assessments.model.postgres.GradeReview;
import com.domus.assessments.model.postgres.StudentGrade;
import com.domus.assessments.repository.mongo.GradeLogRepository;
import com.domus.assessments.repository.postgres.GradeRepository;
import com.domus.assessments.repository.postgres.ReviewRepository;
import com.domus.assessments.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssessmentService {

    private final AcademinsApiClient academinsApiClient;
    private final GradeRepository gradeRepository;
    private final ReviewRepository reviewRepository;
    private final GradeLogRepository gradeLogRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;


    @Transactional
    public GradeResponse createOrUpdateGrade(CreateGradeRequest request) {
//        StudentResponseDTO student = academinsApiClient.getStudentById(request.getStudentId());
//        if (student == null) {
//            throw new ResourceNotFoundException("Student not found in Academins");
//        }

        StudentGrade grade = gradeRepository.findByStudentIdAndEvaluationMomentId(
                request.getStudentId(), request.getEvaluationMomentId()
        ).orElse(StudentGrade.builder()
                .studentId(request.getStudentId())
                .evaluationMomentId(request.getEvaluationMomentId())
                .isPublished(false)
                .build());

        Double oldVal = grade.getGradeValue();
        grade.setGradeValue(request.getGradeValue());
        grade.setTeacherId(request.getTeacherId());

        StudentGrade saved = gradeRepository.save(grade);


        logToMongo(saved.getId(), saved.getStudentId(), request.getTeacherId(), oldVal, saved.getGradeValue(), "UPDATE_GRADE");

        SendNotificationMessage message = SendNotificationMessage.builder()
                .userId(saved.getStudentId()) // TODO this should change after IAM implementation
                .title("New Grade Recorded")
//                .email(student.getEmail())
                .email("Dummy.email@jmail.com")
                .title("New Grade Recorded")
                .message("A new grade has been recorded for you. Please check your assessment portal for details.")
                .channel("EMAIL")
                .build();

        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, message);

        return modelMapper.map(saved, GradeResponse.class);
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(g -> modelMapper.map(g, GradeResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getGradesByMoment(Long momentId) {
        return gradeRepository.findByEvaluationMomentId(momentId).stream()
                .map(g -> modelMapper.map(g, GradeResponse.class))
                .collect(Collectors.toList());
    }


    @Transactional
    public ReviewResponse submitReview(Long studentId, SubmitReviewRequest request) {
        StudentGrade grade = gradeRepository.findById(request.getGradeId())
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found"));

        if (!grade.getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized: Student does not own this grade");
        }

        GradeReview review = GradeReview.builder()
                .studentGrade(grade)
                .studentId(studentId)
                .studentJustification(request.getJustification())
                .status(GradeReview.ReviewStatus.PENDING_APPROVAL)
                .build();

        GradeReview saved = reviewRepository.save(review);
        return mapToReviewResponse(saved);
    }

    @Transactional
    public ReviewResponse scheduleMeeting(Long reviewId, ScheduleMeetingRequest request) {
        GradeReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setMeetingDateTime(request.getMeetingDatetime());
        review.setMeetingLocation(request.getMeetingLocation());
        review.setStatus(GradeReview.ReviewStatus.MEETING_SCHEDULED);

        GradeReview saved = reviewRepository.save(review);
        return mapToReviewResponse(saved);
    }

    @Transactional
    public ReviewResponse closeReview(Long reviewId, CloseReviewRequest request) {
        GradeReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setTeacherObservations(request.getObservations());
        review.setStatus(GradeReview.ReviewStatus.CONCLUDED);

        StudentGrade studentGrade = review.getStudentGrade();
        Double oldVal = studentGrade.getGradeValue();
        Double newVal = oldVal;

        if (request.getNewGrade() != null) {
            studentGrade.setGradeValue(request.getNewGrade());
            gradeRepository.save(studentGrade);
            newVal = request.getNewGrade();
        }

        GradeReview savedReview = reviewRepository.save(review);

        if (oldVal != null && !oldVal.equals(newVal)) {
            logToMongo(studentGrade.getId(), review.getStudentId(), null, oldVal, newVal, "REVIEW_CHANGE");
        }

        return mapToReviewResponse(savedReview);
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long id) {
        GradeReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return mapToReviewResponse(review);
    }


    private void logToMongo(Long gradeId, Long studentId, Long teacherId, Double oldVal, Double newVal, String action) {
        try {
            GradeLog logEntry = GradeLog.builder()
                    .gradeId(gradeId)
                    .studentId(studentId)
                    .performedByTeacherId(teacherId)
                    .oldGrade(oldVal)
                    .newGrade(newVal)
                    .action(action)
                    .timestamp(LocalDateTime.now())
                    .build();
            gradeLogRepository.save(logEntry);
        } catch (Exception e) {
            log.error("Failed to save mongo log: {}", e.getMessage());
        }
    }

    private ReviewResponse mapToReviewResponse(GradeReview review) {
        ReviewResponse resp = modelMapper.map(review, ReviewResponse.class);
        if (review.getStudentGrade() != null) {
            resp.setGradeId(review.getStudentGrade().getId());
            resp.setCurrentGrade(review.getStudentGrade().getGradeValue());
        }
        return resp;
    }
}