package com.iqeq.controller;

import com.iqeq.dto.*;
import com.iqeq.dto.common.Response;
import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.exception.CustomException;
import com.iqeq.model.Priority;
import com.iqeq.service.DocumentService;
import com.iqeq.service.JobService;
import com.iqeq.service.PriorityService;
import com.iqeq.util.CommonConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DocumentController extends BaseController {

    private final DocumentService documentService;

    private final PriorityService priorityService;
    private final JobService jobService;

    @GetMapping("/search/documents")
    public ResponseEntity<Response> getDocumentsByType(@ModelAttribute SearchRequestDto searchRequestDto) throws CustomException {
        return buildResponse(HttpStatus.OK, "Documents fetched successfully", documentService.getDocumentsWithColumns(searchRequestDto));
    }

    //Get count by status
    @GetMapping("/documents/status-count")
    public ResponseEntity<Response> getStatusCounts(@ModelAttribute SearchRequestDto searchRequestDto) throws CustomException{
        log.info("Inside Get count by status method");
        return buildResponse(HttpStatus.OK, CommonConstants.STATUS_COUNT_SUCCESS, documentService.getStatusCounts(searchRequestDto));
    }

    @GetMapping("/export/documents")
    public ResponseEntity<Resource> exportFilterDocuments(@ModelAttribute SearchRequestDto searchRequestDto) throws CustomException {
        log.info("In DocumentController.exportFilterDocuments method started");
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment; fileName=", CommonConstants.EXPORT_DOCUMENT_NAME);
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType(CommonConstants.EXPORT_MEDIA_TYPE)).body(documentService.exportFilterDocuments(searchRequestDto));
        }catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @GetMapping("/audit/export/documents")
    public ResponseEntity<Resource> exportDocuments(@RequestParam(required = false) Set<Long> documentTypeIds) throws CustomException {
        log.info("In DocumentController.exportDocuments method started");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment; fileName=", CommonConstants.EXPORT_AUDIT_DOCUMENT_NAME);
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType(CommonConstants.EXPORT_MEDIA_TYPE)).body(documentService.auditExportFilterDocuments(documentTypeIds));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping(value = "/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDto> uploadDocumentsAsync(@ModelAttribute JobUploadRequestDto request) throws CustomException {
        String jobId = jobService.upload(request);
        return ResponseEntity.ok(new UploadResponseDto("WIP", "Extraction started", jobId));
    }
    @GetMapping("/documents/download/{jobId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String jobId) throws IOException {
        return jobService.downloadFile(jobId);
    }


}
