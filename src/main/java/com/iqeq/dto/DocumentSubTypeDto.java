package com.iqeq.dto;

import com.iqeq.model.DocumentSubType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentSubTypeDto extends BaseDto{


    private Long documentSubTypeId;

    @NotBlank(message = "Document sub type name is required.")
    @Size(min = 3, max = 40, message = "Document sub type name should be between {min} and {max} characters.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Document sub type name should only contain letters and spaces.")
    private String name;

    @Positive(message = "Document Type should be valid.")
    @NotNull(message = "Document Type is required.")
    private Long documentTypeId;

    public static DocumentSubTypeDto build(DocumentSubType documentSubType) {
        DocumentSubTypeDto dto = new DocumentSubTypeDto();
        dto.setDocumentSubTypeId(documentSubType.getDocumentSubTypeId());
        dto.setName(documentSubType.getName());
        dto.setDocumentTypeId(documentSubType.getDocumentType().getDocumentTypeId());
        dto.setCreatedDate(documentSubType.getCreatedDate());
        dto.setUpdatedDate(documentSubType.getUpdatedDate());
        dto.setCreatedBy(documentSubType.getCreatedBy());
        dto.setUpdatedBy(documentSubType.getUpdatedBy());
        dto.setIsArchive(documentSubType.getIsArchive());
        dto.setRevisionNo(documentSubType.getRevisionNo());
        return dto;
    }

}
