package com.iqeq.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "job")
public class Job {

    @Id
    private String jobId;

    private String documentName;
    private String invoiceType;
    private String priority;

    private String status; // WIP, COMPLETED, FAILED
    private String result; // In progress, Success, or Failure reason

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
}
