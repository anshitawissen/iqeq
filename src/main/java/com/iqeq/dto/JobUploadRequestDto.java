package com.iqeq.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Data
public class JobUploadRequestDto {
    @NotNull
    private String documentName;

    @NotNull
    private String documentType;

    @NotNull
    private String priority;

    @NotNull
    private MultipartFile file;

    // Getters and setters
}

