package com.iqeq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class FileWithExcelResponse {
    private String jobId;
    private List<Map<String, String>> excelData;
}

