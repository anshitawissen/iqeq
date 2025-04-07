package com.iqeq.dto;

import com.iqeq.model.BaseEntity;
import com.iqeq.model.DataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataTypeDto extends BaseEntity {

    private Long dataTypeId;

    @NotBlank(message = "Data type name is required.")
    @Size(min = 3, max = 40, message = "Data type name should be between {min} and {max} characters.")
    private String name;

    private String value;

    public static DataTypeDto build(DataType dataType) {
        DataTypeDto dataTypeDto = new DataTypeDto();
        dataTypeDto.setDataTypeId(dataType.getDataTypeId());
        dataTypeDto.setName(dataType.getName());
        dataTypeDto.setValue(dataType.getValue());
        return dataTypeDto;
    }

    public static DataType buildDataType(DataTypeDto dataTypeDto) {
        DataType dataType = new DataType();
        dataType.setDataTypeId(dataTypeDto.getDataTypeId());
        dataType.setName(dataTypeDto.getName());
        dataType.setValue(dataTypeDto.getValue());
        return dataType;
    }
}
