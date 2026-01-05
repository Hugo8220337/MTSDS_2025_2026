package com.domus.assessmentsPlanning.repository;

import com.domus.assessmentsPlanning.model.mongo.PlanContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlanContentRepository extends MongoRepository<PlanContent, String> {
    
}