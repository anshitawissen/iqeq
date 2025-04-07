package com.iqeq.util;

import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.model.QDocument;
import com.iqeq.model.QDocumentType;
import com.querydsl.core.BooleanBuilder;

public class DocumentTypeFilterUtility {

    private static final QDocumentType qDocumentType = QDocument.document.documentType;

    private DocumentTypeFilterUtility(){
    }

    public static void handleDocumentTypeFilters(SearchRequestDto searchRequestDto,
                                                 BooleanBuilder booleanBuilder) throws IllegalStateException{
        Long documentTypeFilters = searchRequestDto.getDocumentTypeId();

        if(documentTypeFilters != null){
            booleanBuilder.and(qDocumentType.documentTypeId.eq(documentTypeFilters));
        }

    }
}
