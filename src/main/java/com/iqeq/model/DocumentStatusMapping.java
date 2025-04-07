package com.iqeq.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Table(name = "document_status_mapping")
@Data
@Entity
public class DocumentStatusMapping extends BaseEntity {
    @EmbeddedId
    private DocumentStatusMappingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("documentId")
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("statusId")
    @JoinColumn(name = "document_status_id", nullable = false)
    private DocumentStatus documentStatus;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

}
