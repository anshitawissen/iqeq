package com.iqeq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentStatusDto {
    private Long count;
    private String colorCode;
    private String label;
    private Integer uiSequence;
    private String value;
    private String iconName;


}