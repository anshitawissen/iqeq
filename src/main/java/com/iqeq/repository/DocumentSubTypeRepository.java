package com.iqeq.repository;

import com.iqeq.model.DocumentSubType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface DocumentSubTypeRepository extends JpaRepository<DocumentSubType, Long>, QuerydslPredicateExecutor<DocumentSubType> {
    Optional<DocumentSubType> findByName(String subTypeName);

    List<DocumentSubType> findByDocumentType_DocumentTypeIdOrderByNameAsc(Long documentTypeId);

    Optional<DocumentSubType> findByNameAndIsArchiveFalse(@NotBlank(message = "Document sub type name is required.") @Size(min = 3, max = 40, message = "Document sub type name should be between {min} and {max} characters.") @Pattern(regexp = "^[a-zA-Z ]+$", message = "Document sub type name should only contain letters and spaces.") String name);

    Optional<DocumentSubType> findByDocumentSubTypeIdAndIsArchive(Long documentSubTypeId, boolean isArchive);
}
