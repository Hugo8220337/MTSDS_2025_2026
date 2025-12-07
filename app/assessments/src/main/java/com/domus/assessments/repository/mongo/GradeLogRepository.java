package com.domus.assessments.repository.mongo;

import com.domus.assessments.model.mongo.GradeLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeLogRepository extends MongoRepository<GradeLog, String> {
    List<GradeLog> findByGradeId(Long gradeId);
}