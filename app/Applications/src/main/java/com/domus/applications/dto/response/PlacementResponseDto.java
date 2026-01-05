package com.domus.applications.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacementResponseDto {
	public Long id;
	public ApplicationResponseDto application;
	public CourseOptionResponseDto courseOption;
	public Integer round;
	public LocalDateTime placedAt;
}
