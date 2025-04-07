package com.iqeq.dto;

import com.iqeq.model.DataType;
import com.iqeq.model.DocumentSubType;
import com.iqeq.model.DocumentType;
import com.iqeq.model.Field;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FieldDto extends BaseDto {

    private Long fieldId;

    @Positive(message = "Document Type should be valid.")
    @NotNull(message = "Document Type is required.")
    private Long documentTypeId;

    private Long documentSubTypeId;

    @NotNull(message = "Unique is required.")
    private Boolean isUnique;

    @Positive(message = "Data Type should be valid.")
    @NotNull(message = "Data Type is required.")
    private Long dataTypeId;

    @NotNull(message = "Mandatory is required.")
    private Boolean isMandatory;

    @NotBlank(message = "Field name is required.")
    @Size(min = 3, max = 40, message = "Field name should be between {min} and {max} characters.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Field name should only contain letters and spaces.")
    private String name;

    @Size(max = 500, message = "Description should be contain maximum {max} characters.")
    private String description;

    @NotNull(message = "Confidence score is required.")
    private Float confidenceScore;

    public static FieldDto build(Field field) {
        FieldDto fieldDto = new FieldDto();
        fieldDto.setFieldId(field.getFieldId());
        fieldDto.setDocumentTypeId(field.getDocumentType().getDocumentTypeId());
        fieldDto.setIsUnique(field.getIsUnique());
        fieldDto.setDataTypeId(field.getDataType().getDataTypeId());
        fieldDto.setIsMandatory(field.getIsMandatory());
        fieldDto.setName(field.getName());
        fieldDto.setDescription(field.getDescription());
        fieldDto.setConfidenceScore(field.getConfidenceScore());
        if (field.getDocumentSubType() != null) {
            fieldDto.setDocumentSubTypeId(field.getDocumentSubType().getDocumentSubTypeId());
        }
        fieldDto.setCreatedBy(field.getCreatedBy());
        fieldDto.setCreatedDate(field.getCreatedDate());
        fieldDto.setUpdatedDate(field.getUpdatedDate());
        fieldDto.setUpdatedBy(field.getUpdatedBy());
        fieldDto.setIsArchive(field.getIsArchive());
        fieldDto.setRevisionNo(field.getRevisionNo());

        return fieldDto;
    }

    public static Field buildField(FieldDto fieldDto, DocumentType documentType, DataType dataType, DocumentSubType documentSubType) {
        Field field = new Field();
        field.setFieldId(fieldDto.getFieldId());
        field.setDocumentType(documentType);
        field.setIsUnique(fieldDto.getIsUnique());
        field.setDataType(dataType);
        field.setIsMandatory(fieldDto.getIsMandatory());
        field.setName(fieldDto.getName());
        field.setDescription(fieldDto.getDescription());
        field.setConfidenceScore(fieldDto.getConfidenceScore());
        field.setDocumentSubType(documentSubType);
        return field;
    }
}
