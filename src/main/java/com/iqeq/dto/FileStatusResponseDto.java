package com.iqeq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileStatusResponseDto {
    private String fileName;
    private String status; // Success or Error
    private String message; //Per file if any
}
