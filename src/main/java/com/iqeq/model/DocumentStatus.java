package com.iqeq.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Table(name = "document_status")
@Data
@Entity
public class DocumentStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_status_seq")
    @SequenceGenerator(name = "document_status_seq", sequenceName = "document_status_seq", allocationSize = 1)
    @Column
    private Long documentStatusId;

    @Column(nullable = false)
    private String value;

    @Column
    private Integer uiSequence;

    @Column(nullable = false, unique = true)
    private String label;

    @Column
    private String iconName;

    @Column
    private String colorCode;
}
