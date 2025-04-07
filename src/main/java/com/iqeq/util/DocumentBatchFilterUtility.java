package com.iqeq.util;

import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.model.QDocument;
import com.iqeq.model.QDocumentBatch;
import com.querydsl.core.BooleanBuilder;

public class DocumentBatchFilterUtility {

    private static final QDocumentBatch qDocumentBatch = QDocument.document.documentBatch;

    private DocumentBatchFilterUtility(){
    }

    public static void handleDocumentBatchFilters(SearchRequestDto searchRequestDto,
                                                 BooleanBuilder booleanBuilder) throws IllegalStateException{
        Long documentBatchFilters = searchRequestDto.getDocumentBatchId();

        if(documentBatchFilters != null){
            booleanBuilder.and(qDocumentBatch.documentBatchId.eq(documentBatchFilters));
        }

    }

}
