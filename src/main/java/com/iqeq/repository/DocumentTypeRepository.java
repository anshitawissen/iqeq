package com.iqeq.repository;

import com.iqeq.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long>, QuerydslPredicateExecutor<DocumentType>{
    Optional<DocumentType> findByName(String documentType);
}
