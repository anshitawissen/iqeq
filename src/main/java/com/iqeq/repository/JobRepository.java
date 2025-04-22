package com.iqeq.repository;

import com.iqeq.enums.JobStatus;
import com.iqeq.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    List<Job> findAllByStatus(JobStatus status);
    Optional<Job> findByJobId(String jobId);
    @Query(value = """
    SELECT * FROM (
        SELECT *, ROW_NUMBER() OVER (PARTITION BY document_type ORDER BY created_at DESC) AS rn
        FROM job
    ) sub
    WHERE rn > :offset AND rn <= :offset + :size
    """, nativeQuery = true)
    List<Job> findJobsGroupedByTypeWithPagination(@Param("offset") int offset, @Param("size") int size);
    @Query(value = "SELECT * FROM job WHERE document_type = :documentType ORDER BY created_at DESC LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Job> findByDocumentTypeWithPagination(@Param("documentType") String documentType,
                                               @Param("offset") int offset,
                                               @Param("size") int size);


}

