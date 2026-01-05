package com.domus.assessments.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleMeetingRequest {
    private LocalDateTime meetingDatetime;
    private String meetingLocation;
}