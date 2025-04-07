package com.iqeq.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String label;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;
}