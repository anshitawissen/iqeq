package com.iqeq.dto;

import com.iqeq.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriorityDto extends BaseDto{

    private Long priorityId;

    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Label should only contain letters, numbers and spaces.")
    @NotBlank(message = "Label value is required.")
    private String label;

    @NotNull(message = "Value is required.")
    private Integer value;

    @NotBlank(message = "ColorCode is required.")
    private String colorCode;

    public static PriorityDto build(Priority priority) {
        PriorityDto priorityDto = new PriorityDto();
        priorityDto.setLabel(priority.getLabel());
        priorityDto.setValue(priority.getValue());
        priorityDto.setColorCode(priority.getColorCode());
        priorityDto.setCreatedBy(priority.getCreatedBy());
        priorityDto.setCreatedDate(priority.getCreatedDate());
        priorityDto.setUpdatedBy(priority.getUpdatedBy());
        priorityDto.setUpdatedDate(priority.getUpdatedDate());
        priorityDto.setIsArchive(priority.getIsArchive());
        priorityDto.setRevisionNo(priority.getRevisionNo());
        return priorityDto;
    }

}
