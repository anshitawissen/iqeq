package com.iqeq.repository;

import com.iqeq.model.Priority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Long>, QuerydslPredicateExecutor<Priority> {

    Optional<Priority> findByLabelAndIsArchiveFalse(String label);

    List<Priority> findByIsArchiveFalse(Sort value);
}
