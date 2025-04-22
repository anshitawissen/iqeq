package com.iqeq.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JobArtifactResponseDto {
    private String jobId;
    private String documentName;
    private String documentType;
    private String priority;
    private String status;
    private String result;
    private String createdAt;
    private String updatedAt;

    private String pdfDownloadUrl;
    private List<Map<String, String>> extractedJson;
}
