package com.iqeq.util;

import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.model.QDocument;
import com.iqeq.model.QDocumentBatch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DocumentServiceUtility {

    private static final QDocument qDocument = QDocument.document;
    private static final QDocumentBatch qDocumentBatch = QDocument.document.documentBatch;

    private final EntityManager entityManager;

    public void setFilters(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder) {
        DocumentTypeFilterUtility.handleDocumentTypeFilters(searchRequestDto, booleanBuilder);
        DocumentBatchFilterUtility.handleDocumentBatchFilters(searchRequestDto, booleanBuilder);
        handleCreatedDateFilter(searchRequestDto, booleanBuilder);
        handleDocumentNameFilter(searchRequestDto, booleanBuilder);
        handleDocumentBatchNameFilter(searchRequestDto, booleanBuilder);
    }

    private void handleCreatedDateFilter(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder) throws DateTimeException {
        LocalDate startDate = searchRequestDto.getStartDate();
        LocalDate endDate = searchRequestDto.getEndDate();

        QDocument qDocument = QDocument.document;

        if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            BooleanExpression createdDateFilter = qDocument.createdDate.gt(startDateTime)
                    .and(qDocument.createdDate.loe(endDateTime));

            booleanBuilder.and(createdDateFilter);
        } else if (Objects.nonNull(startDate)) {
            BooleanExpression createdDateFilter = qDocument.createdDate.gt(startDate.atStartOfDay());
            booleanBuilder.and(createdDateFilter);
        } else if (Objects.nonNull(endDate)) {
            BooleanExpression createdDateFilter = qDocument.createdDate.loe(endDate.atTime(LocalTime.MAX));
            booleanBuilder.and(createdDateFilter);
        }
    }

    private void handleDocumentNameFilter(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder) throws DataAccessException {
        String name = searchRequestDto.getName();
        if (Objects.nonNull(searchRequestDto.getName()) && !searchRequestDto.getName().isEmpty()) {
            JPAQuery<?> queryDocumentName = getJpaQuery();
            BooleanExpression documentNameFilter = qDocument.name.containsIgnoreCase(name).or(qDocument.name.contains(name));

            JPAQuery<Long> documentIds = queryDocumentName.select(qDocument.documentId).from(qDocument).where(documentNameFilter);

            booleanBuilder.and(qDocument.documentId.in((documentIds)));
        }
    }

    private void handleDocumentBatchNameFilter(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder) throws DataAccessException {
        String name = searchRequestDto.getBatchName();
        if (Objects.nonNull(searchRequestDto.getBatchName()) && !searchRequestDto.getBatchName().isEmpty()) {

            JPAQuery<?> queryDocumentBatchName = getJpaQuery();
            BooleanExpression documentBatchNameFilter = qDocumentBatch.name.containsIgnoreCase(name).or(qDocumentBatch.name.contains(name));

            JPAQuery<Long> documentBatchIds = queryDocumentBatchName.select(qDocumentBatch.documentBatchId).from(qDocumentBatch).where(documentBatchNameFilter);

            booleanBuilder.and(qDocumentBatch.documentBatchId.in((documentBatchIds)));
        }
    }


    private JPAQuery<?> getJpaQuery() {
        return new JPAQuery<>(entityManager);
    }

}
