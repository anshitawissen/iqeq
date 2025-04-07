package com.iqeq.repository;

import com.iqeq.model.DocumentType;
import com.iqeq.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long>, QuerydslPredicateExecutor<Field> {

    Field findByNameAndDocumentTypeAndIsArchive(String name, DocumentType documentType, boolean isArchive);

    Optional<Field> findByFieldIdAndIsArchive(Long fieldId, boolean isArchive);

    Field findByNameAndIsArchive(String name, boolean isArchive);
}
