package com.iqeq.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class DocumentStatusMappingId {

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "document_status_id", nullable = false)
    private Long statusId;
}
