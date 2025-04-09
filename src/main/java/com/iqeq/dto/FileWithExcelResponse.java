package com.iqeq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class FileWithExcelResponse {
    private String jobId;
    private List<List<String>> excelData;
}

