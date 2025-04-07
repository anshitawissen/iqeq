package com.iqeq.dto;

import com.iqeq.validation.ValidFile;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentInfoDto {
    @NotNull
    private String documentName;

    private String priority;

    @NotNull
    @ValidFile
    private MultipartFile file;
}

