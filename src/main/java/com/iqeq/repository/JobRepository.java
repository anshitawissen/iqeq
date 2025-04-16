package com.iqeq.repository;

import com.iqeq.enums.JobStatus;
import com.iqeq.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    List<Job> findAllByStatus(JobStatus status);
    Optional<Job> findByJobId(String jobId);
}

