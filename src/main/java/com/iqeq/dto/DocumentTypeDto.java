package com.iqeq.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DocumentTypeDto extends BaseDto {
    private Long documentTypeId;
    private String name;
}
