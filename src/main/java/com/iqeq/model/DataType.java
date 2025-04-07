package com.iqeq.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_type_seq")
    @SequenceGenerator(name = "data_type_seq", sequenceName = "data_type_seq", allocationSize = 1)
    @Column(name = "dataTypeId")
    private Long dataTypeId;

    @Column(nullable = false, length = 40)
    @Size(max = 40, message = "Data type name should be contain maximum {max} characters.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Field name should only contain letters and spaces.")
    @NotBlank(message = "Data type name value is required.")
    private String name;

    @Column(name = "value")
    private String value;
}
