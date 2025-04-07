package com.iqeq.repository;

import com.iqeq.model.DocumentBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DocumentBatchRepository extends JpaRepository<DocumentBatch, Long>, QuerydslPredicateExecutor<DocumentBatch> {
}

