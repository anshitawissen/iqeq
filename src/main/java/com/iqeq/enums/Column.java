package com.iqeq.enums;

import lombok.Getter;

@Getter
public enum Column {
    DOCUMENT_NAME("Document Name", "name"),
    BATCH_NAME("Batch Name", "batchName"),
    INVOICING_TYPE("Invoicing Type", "documentSubType"),
    PRIORITY_ORDER("Priority", "priority"),
    UPLOADED_DATE("Uploaded Date", "createdDate"),
    PROCESSING_DATE("Processing Date", "processingDateTime"),
    PROCESSING_TIME("Processing Time", "processingTime"),
    ACCURACY("Accuracy", "successMatrix"),
    STATUS("Status", "status");

    private final String label;
    private final String value;

    Column(String label, String value) {
        this.label = label;
        this.value = value;
    }

}
