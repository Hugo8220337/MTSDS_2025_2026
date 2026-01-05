package com.domus.applications.dto.response;

import com.domus.applications.valueObjects.CourseOptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseOptionResponseDto {
	public Long id;
	public Long courseId;
	public Integer preferenceOrder;
	public CourseOptionStatus status;
	public LocalDateTime createdAt;
	public LocalDateTime updatedAt;
}
