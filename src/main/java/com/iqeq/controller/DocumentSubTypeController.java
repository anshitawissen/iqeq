package com.iqeq.controller;

import com.iqeq.dto.DocumentSubTypeDto;
import com.iqeq.dto.common.Response;
import com.iqeq.exception.CustomException;
import com.iqeq.service.DocumentSubTypeService;
import com.iqeq.util.CommonConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DocumentSubTypeController extends BaseController{

    private final DocumentSubTypeService documentSubTypeService;

    @GetMapping("/document-sub-type/{documentTypeId}")
    public ResponseEntity<Response> getDocumentSubTypesByDocumentType(@Valid @PathVariable Long documentTypeId)
    {
        log.info("Inside get document-sub-type by document type method");
        return buildResponse(HttpStatus.OK, CommonConstants.GET_DOCUMENT_SUB_TYPE_SUCCESS, documentSubTypeService.getDocumentSubTypesByDocumentType(documentTypeId));
    }
    @PostMapping("/document-sub-type")
    public ResponseEntity<Response> createDocumentSubType(@Valid @RequestBody DocumentSubTypeDto documentSubTypeDto) throws CustomException {
        log.info("Inside create document-sub-type method");
        return buildResponse(HttpStatus.OK, CommonConstants.DOCUMENT_SUB_TYPE_SUCCESS, documentSubTypeService.createDocumentSubType(documentSubTypeDto));
    }


}
