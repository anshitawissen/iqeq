package com.iqeq.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DocumentUploadRequestDto {

    @NotNull
    private String documentType;
    private String batchName;
    private String documentSubType;
    @NotNull
    private String priority;

    @NotEmpty
    @Valid
    private List<DocumentInfoDto> documents;

}