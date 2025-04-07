package com.iqeq.repository;

import com.iqeq.model.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface DocumentStatusRepository extends JpaRepository<DocumentStatus, Long>, QuerydslPredicateExecutor<DocumentStatus> {
    Optional<DocumentStatus> findByLabel(String documentStatus);
}
