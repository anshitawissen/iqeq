package com.iqeq.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "priority")
@Data
public class Priority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priority_seq")
    @SequenceGenerator(name = "priority_seq", sequenceName = "priority_seq", allocationSize = 1)
    @Column
    private Long priorityId;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Label should only contain letters, numbers and spaces.")
    @NotBlank(message = "Label value is required.")
    private String label;

    @Column(nullable = false)
    @NotNull(message = "Value is required.")
    private Integer value;

    @Column(nullable = false)
    private String colorCode;

}
