package com.iqeq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    private String value;
    private String colorCode;
    private String iconName;
    private Integer uiSequence;
}
