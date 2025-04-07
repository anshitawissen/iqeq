package com.iqeq.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_seq")
	@SequenceGenerator(name = "document_seq", sequenceName = "document_seq", allocationSize = 1)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "priorityId")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "parent_document_id")
    private Document parentDocument;

    @Column(name = "processing_start_at")
    private LocalDateTime processingStartAt;

    @Column(name = "processing_end_at")
    private LocalDateTime processingEndAt;

    @ManyToOne
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

    @ManyToOne
    @JoinColumn(name = "document_sub_type_id")
    private DocumentSubType documentSubType;

    @ManyToOne
    @JoinColumn(name = "document_batch_id")
    private DocumentBatch documentBatch;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "success_matrix")
    private Float successMatrix;

}
