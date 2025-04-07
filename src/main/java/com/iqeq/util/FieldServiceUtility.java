package com.iqeq.util;

import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.model.QField;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FieldServiceUtility {

    private static final QField qField = QField.field;

    private final EntityManager entityManager;

    public void setFilters(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder) {
        handleFieldNameFilter(searchRequestDto, booleanBuilder);
    }

    private void handleFieldNameFilter(SearchRequestDto searchRequestDto, BooleanBuilder booleanBuilder) throws DataAccessException {
        String name = searchRequestDto.getName();
        if (Objects.nonNull(searchRequestDto.getName()) && !searchRequestDto.getName().isEmpty()) {
            JPAQuery<?> queryFieldName = getJpaQuery();
            BooleanExpression fieldNameFilter = qField.name.containsIgnoreCase(name).or(qField.name.contains(name));

            JPAQuery<Long> fieldIds = queryFieldName.select(qField.fieldId).from(qField).where(fieldNameFilter);

            booleanBuilder.and(qField.fieldId.in((fieldIds)));
        }
    }

    private JPAQuery<?> getJpaQuery() {
        return new JPAQuery<>(entityManager);
    }

}
