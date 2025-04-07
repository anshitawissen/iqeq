package com.iqeq.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentType extends BaseEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_type_seq")
	@SequenceGenerator(name = "document_type_seq", sequenceName = "document_type_seq", allocationSize = 1)
    @Column(name = "document_type_id")
    private Long documentTypeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

}
