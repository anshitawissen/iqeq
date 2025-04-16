package com.iqeq.enums;

public enum JobStatus {
    UPLOADED,         // PDF file accepted and saved locally/temp
    PROCESSING,       // File queued and being uploaded to workstation
    EXTRACTION_WIP,   // File uploaded to workstation, waiting for JSON/Excel
    EXPORT_READY,     // Excel file is available in the workstation
    EXPORTED,         // File has been downloaded by the user
    FAILED            // Error/Failure
}
