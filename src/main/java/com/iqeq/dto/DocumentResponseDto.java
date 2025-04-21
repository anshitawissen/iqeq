package com.iqeq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDto {
    private String documentName;
    private String documentType;
    private String jobId;
    private String status;
    private String result;
    private String priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

