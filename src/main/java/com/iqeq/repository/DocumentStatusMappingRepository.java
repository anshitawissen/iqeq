package com.iqeq.repository;

import com.iqeq.model.DocumentStatusMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DocumentStatusMappingRepository extends JpaRepository<DocumentStatusMapping, Long>, QuerydslPredicateExecutor<DocumentStatusMapping> {
}
