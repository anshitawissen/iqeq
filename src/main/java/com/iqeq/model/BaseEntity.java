package com.iqeq.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
public class BaseEntity {

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Column(nullable = false, updatable = false)
    private String createdBy;

    @Column
    private String updatedBy;

    @Column(nullable = false)
    private Integer revisionNo;

    @Column(nullable = false)
    @NotNull(message = "isArchive value is required.")
    private Boolean isArchive;
}
