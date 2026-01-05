package com.domus.assessmentsPlanning.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "plan_contents")
public class PlanContent {
    
    @Id
    private String id; 

    private String generalObjectives;
    private List<String> specificObjectives;
    private List<String> syllabusTopics;
    private String teachingMethodologies;
    
    private String coherenceContentsToObjectives;
    
    private List<BookReference> bibliography;
}
