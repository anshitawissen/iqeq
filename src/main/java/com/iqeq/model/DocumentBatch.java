package com.iqeq.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentBatch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_batch_seq")
    @SequenceGenerator(name = "document_batch_seq", sequenceName = "document_batch_seq", allocationSize = 1)
    @Column(name = "document_batch_id")
    private Long documentBatchId;

    @Column(name = "name", nullable = false)
    private String name;

}
