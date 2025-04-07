package com.iqeq.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "field")
@Data
public class Field extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "field_seq")
    @SequenceGenerator(name = "field_seq", sequenceName = "field_seq", allocationSize = 1)
    @Column
    private Long fieldId;

    @ManyToOne
    @JoinColumn(name = "documentTypeId", nullable = false)
    private DocumentType documentType;

    @ManyToOne
    @JoinColumn(name = "documentSubTypeId")
    private DocumentSubType documentSubType;

    @Column(nullable = false)
    @NotNull(message = "Unique value is required.")
    private Boolean isUnique;

    @ManyToOne
    @JoinColumn(name = "dataTypeId", nullable = false)
    private DataType dataType;

    @Column(nullable = false)
    @NotNull(message = "Mandatory value is required.")
    private Boolean isMandatory;

    @Column(nullable = false, length = 40)
    @Size(max = 40, message = "Field name should be contain maximum {max} characters.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Field name should only contain letters and spaces.")
    @NotBlank(message = "Field name value is required.")
    private String name;

    @Column(length = 500)
    @Size(max = 500, message = "Description should be contain maximum {max} characters.")
    private String description;

    @Column(nullable = false)
    private Float confidenceScore;
}
