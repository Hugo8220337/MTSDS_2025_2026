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
public class DocumentResponseDto {
	public Long id;
	public String documentType;
//	public String documentUrl;
    public String documentBase64;
	public LocalDateTime createdAt;
	public LocalDateTime updatedAt;
}
