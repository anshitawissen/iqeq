package com.iqeq.repository;

import com.iqeq.dto.DocumentStatusDto;
import com.iqeq.model.Document;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<Document, Long>, QuerydslPredicateExecutor<Document> {

    @Query("SELECT new com.iqeq.dto.DocumentStatusDto(COALESCE(COUNT(d), 0), " +
            "ds.colorCode, " +
            "ds.label, " +
            "ds.uiSequence, " +
            "ds.value, " +
            "ds.iconName) " +
            "FROM DocumentStatusMapping dsm " +
            "LEFT JOIN dsm.document d " +
            "RIGHT JOIN dsm.documentStatus ds " +
            "LEFT JOIN d.documentBatch db " +
            "WHERE (CAST(:startDate AS timestamp) IS NULL OR CAST(:endDate AS timestamp) IS NULL " +
            "OR d.createdDate BETWEEN :startDate AND :endDate) " +
            "AND (:documentTypeId IS NULL OR d.documentType.id = :documentTypeId) " +
            "AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL)" +
            "AND (LOWER(db.name) LIKE LOWER(CONCAT('%',:batchName, '%')) OR :batchName IS NULL) " +
            "GROUP BY ds.documentStatusId, ds.colorCode, ds.label, ds.uiSequence, ds.value, ds.iconName")
    List<DocumentStatusDto> getStatusCounts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("documentTypeId") Long documentTypeId,
            @Param("name") String name,
            @Param("batchName") String batchName
    );

    @Query("SELECT d as document, " +
            "ds.value as statusValue, " +
            "ds.colorCode as statusColor, " +
            "ds.iconName as statusIcon, " +
            "ds.uiSequence as statusUiSequence " +
            "FROM Document d " +
            "LEFT JOIN DocumentStatusMapping dsm ON d = dsm.document AND dsm.isArchive = false " +
            "LEFT JOIN dsm.documentStatus ds " +
            "LEFT JOIN d.documentBatch db " +
            "WHERE (:status IS NULL OR :status = 'ALL' OR ds.label = :status) " +
            "AND (:docTypeId IS NULL OR d.documentType.documentTypeId = :docTypeId) " +
            "AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) " +
            "AND (LOWER(db.name) LIKE LOWER(CONCAT('%', :batchName, '%')) OR :batchName IS NULL) " +
            "AND (:startDate IS NULL OR CAST(d.createdDate AS date) >= :startDate) " +
            "AND (:endDate IS NULL OR CAST(d.createdDate AS date) <= :endDate) " +
            "AND d.isArchive = false ")
    Page<Tuple> findDocumentsWithStatus(
            @Param("status") String status,
            @Param("docTypeId") Long documentTypeId,
            @Param("name") String name,
            @Param("batchName") String batchName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT d FROM Document d WHERE d.documentType.documentTypeId IN :documentTypeIds")
    List<Document> findAllDocumentsByDocumentTypeIdIn(Set<Long> documentTypeIds);
}
