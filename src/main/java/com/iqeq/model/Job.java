package com.iqeq.model;

import com.iqeq.enums.JobStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String jobId;

    private String documentName;

    private String documentType;

    private String priority;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private String result;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
