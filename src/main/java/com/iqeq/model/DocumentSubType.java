package com.iqeq.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_sub_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DocumentSubType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_sub_type_seq")
    @SequenceGenerator(name = "document_sub_type_seq", sequenceName = "document_sub_type_seq", allocationSize = 1)
    @Column(name = "document_sub_type_id")
    private Long documentSubTypeId;

    @Column(nullable = false, length = 40)
    @Size(max = 40, message = "Document sub type name should be contain maximum {max} characters.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Document sub type name should only contain letters and spaces.")
    @NotBlank(message = "Document sub type name value is required.")
    private String name;

    @ManyToOne
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

}
