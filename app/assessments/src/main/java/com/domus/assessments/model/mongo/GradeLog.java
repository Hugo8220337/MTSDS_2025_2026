package com.domus.assessments.model.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "grade_logs")
public class GradeLog {
    @Id
    private String id;
    
    private Long gradeId;
    private Long studentId;
    private Long performedByTeacherId;
    
    private Double oldGrade;
    private Double newGrade;
    
    private String action; 
    private String reason;
    
    private LocalDateTime timestamp;
}