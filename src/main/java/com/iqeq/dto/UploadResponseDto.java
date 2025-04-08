package com.iqeq.dto;

import lombok.*;
@AllArgsConstructor
@Data
public class UploadResponseDto {
    private String status;
    private String message;
    private String jobId;
}


