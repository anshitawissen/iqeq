package com.iqeq.service;

import com.iqeq.dto.*;
import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.dto.common.SearchResponse;
import com.iqeq.exception.AzureFileUploadException;
import com.iqeq.exception.CustomException;
import com.iqeq.helper.DocumentFileWriter;
import com.iqeq.model.*;
import com.iqeq.repository.*;
import com.iqeq.util.CommonConstants;
import com.iqeq.util.CommonServiceUtility;
import com.iqeq.util.DocumentServiceUtility;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.iqeq.util.CommonServiceUtility.getLoggedInUserEmailId;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

	private final DocumentRepository documentRepository;
	private final DocumentBatchRepository documentBatchRepository;
	private final DocumentTypeRepository documentTypeRepository;
	private final DocumentSubTypeRepository documentSubTypeRepository;
	private final DocumentUploadClientService documentUploadClientService;
	private final DocumentServiceUtility documentServiceUtility;
	private final DocumentStatusRepository documentStatusRepository;
	private final DocumentStatusMappingRepository documentStatusMappingRepository;

	private final DocumentFileWriter documentFileWriter;

	private final PriorityService priorityService;

	public SearchResponse getDocumentsWithColumns(SearchRequestDto searchRequestDto) throws DataAccessException, CustomException {
		searchRequestDto.validSearchRequest(searchRequestDto);
		Sort sort = CommonServiceUtility.applySortFilter(searchRequestDto);
		if (Objects.isNull(sort)) {
			sort = Sort.by(Sort.Direction.ASC, "createdDate");
		}
		if(searchRequestDto.getFilterBy() != null){
			CommonServiceUtility.setDateRange(searchRequestDto);
		}
		Pageable pageable = PageRequest.of(searchRequestDto.getPage(), searchRequestDto.getSize(), sort);

		Page<Tuple> documentPage = documentRepository.findDocumentsWithStatus(
				searchRequestDto.getStatus(),
				searchRequestDto.getDocumentTypeId(),
				searchRequestDto.getName(),
				searchRequestDto.getBatchName(),
				searchRequestDto.getStartDate(),
				searchRequestDto.getEndDate(),
				pageable
		);

		List<DocumentDto> documentDtos = documentPage.getContent().stream()
				.map(DocumentDto::build)
				.collect(Collectors.toList());

		return SearchResponse.build(documentDtos, documentPage, searchRequestDto.getIsDashboard());
	}

	//Get count by status
    public List<DocumentStatusDto>  getStatusCounts(SearchRequestDto searchRequestDto) throws DataAccessException, ClassCastException, ArithmeticException, ArrayIndexOutOfBoundsException{
		log.info("Inside get count by status service method");
		if(searchRequestDto.getFilterBy() != null){
			CommonServiceUtility.setDateRange(searchRequestDto);
		}

		LocalDateTime startDate = null, endDate = null;
		if (Objects.nonNull(searchRequestDto.getStartDate()) && Objects.nonNull(searchRequestDto.getEndDate())){
			startDate = Timestamp.valueOf(searchRequestDto.getStartDate().atStartOfDay()).toLocalDateTime();
			endDate = Timestamp.valueOf(searchRequestDto.getEndDate().atTime(LocalTime.MAX)).toLocalDateTime();
		}

		List<DocumentStatusDto> results = documentRepository.getStatusCounts(startDate,endDate,
				searchRequestDto.getDocumentTypeId(),
				searchRequestDto.getName(),
				searchRequestDto.getBatchName()
		);

		DocumentStatusDto allStatus = new DocumentStatusDto();
		allStatus.setColorCode(CommonConstants.COLOR_CODE_ALL);
		allStatus.setUiSequence(CommonConstants.UI_SEQUENCE_ALL);
		allStatus.setIconName(CommonConstants.ICON_NAME_ALL);
		allStatus.setLabel(CommonConstants.STATUS_ALL.toUpperCase());
		allStatus.setValue(CommonConstants.STATUS_ALL);
		allStatus.setCount(0L);

		long allCount = results.stream()
				.map(dto -> {
					if (dto.getCount() == null) {
						dto.setCount(0L);
					}
					return dto.getCount();
				})
				.reduce(0L, Long::sum);

		allStatus.setCount(allCount);

		results.add(allStatus);

		log.info("Status Count Result: {}", results);
		return results;
	}

	public InputStreamResource exportFilterDocuments(SearchRequestDto searchRequestDto) throws CustomException {
		SearchResponse response = getDocumentsWithColumns(searchRequestDto);
		List<DocumentDto> documentDtoList = (List<DocumentDto>) response.getData();
		return documentFileWriter.export(documentDtoList, CommonConstants.EXPORT_DOCUMENT_HEADERS, CommonConstants.EXPORT_DOCUMENT_FIELDS, DocumentDto.class, CommonConstants.EXPORT_DOCUMENT_NAME.split("\\.")[0]);
	}

	public InputStreamResource auditExportFilterDocuments(Set<Long> documentTypeIds) throws CustomException {
		List<Document> results = documentRepository.findAllDocumentsByDocumentTypeIdIn(documentTypeIds);
		List<DocumentDto> documentDtoList = results.stream().map(DocumentDto::build).toList();
		return documentFileWriter.export(documentDtoList, CommonConstants.AUDIT_EXPORT_DOCUMENT_HEADERS, CommonConstants.AUDIT_EXPORT_DOCUMENT_FIELDS, DocumentDto.class, CommonConstants.EXPORT_AUDIT_DOCUMENT_NAME.split("\\.")[0]);
	}

	@Transactional
	public List <FileStatusResponseDto> uploadDocuments(DocumentUploadRequestDto request) throws CustomException {
		// Create Response
		List<FileStatusResponseDto> fileStatuses = new ArrayList<>();

		//Get DocumentType and SubType throw exception if not exist
		DocumentType documentType = fetchDocumentType(request.getDocumentType());
		DocumentSubType documentSubType = fetchDocumentSubType(request.getDocumentSubType());
		DocumentBatch batch = createBatchIfRequired(request);

		for (DocumentInfoDto documentInfo : request.getDocuments()) {
			processDocument(documentInfo, batch, documentType, documentSubType, fileStatuses);
		}

		return fileStatuses;
	}

	private DocumentType fetchDocumentType(String documentTypeName) {
		return documentTypeRepository.findByName(documentTypeName)
				.orElseThrow(() -> new RuntimeException("Document Type not found: " + documentTypeName));
	}

	private DocumentSubType fetchDocumentSubType(String documentSubTypeName) {
		return documentSubTypeRepository.findByName(documentSubTypeName)
				.orElseThrow(() -> new RuntimeException("Document Sub Type not found: " + documentSubTypeName));
	}

	private DocumentBatch createBatchIfRequired(DocumentUploadRequestDto request) {
		if (Objects.isNull(request.getBatchName()) || request.getBatchName().isEmpty() || request.getDocuments().isEmpty()) {
			return null;
		}
		try {
			DocumentBatch batch = new DocumentBatch();
			batch.setName(request.getBatchName());
			batch.setRevisionNo(0);
			batch.setIsArchive(false);
			batch.setCreatedBy(getLoggedInUserEmailId());
			return documentBatchRepository.save(batch);
		} catch (Exception e) {
			log.error("Failed to create batch: {}", e.getMessage());
			throw new RuntimeException("Batch creation failed: " + e.getMessage());
		}
	}

	private void processDocument(DocumentInfoDto documentInfo, DocumentBatch batch,
								 DocumentType documentType, DocumentSubType documentSubType,
								 List<FileStatusResponseDto> fileStatuses) {
		MultipartFile file = documentInfo.getFile();
		String filePath = documentType.getName() + CommonConstants.FORWARD_SLASH + documentSubType.getName() + CommonConstants.FORWARD_SLASH;
		String blobUrl = uploadFileToAzure(file, documentInfo.getDocumentName(), filePath, fileStatuses);

		if (!Objects.isNull(blobUrl)) {
			saveDocumentMetadata(documentInfo, batch, blobUrl, documentType, documentSubType, fileStatuses);
		}
	}

	private String uploadFileToAzure(MultipartFile file, String documentName, String filePath, List<FileStatusResponseDto> fileStatuses) {
		try {
			return documentUploadClientService.uploadFile(file, documentName, filePath);
		} catch (Exception e) {
			fileStatuses.add(new FileStatusResponseDto(documentName, CommonConstants.FAILURE, "Azure upload failed: " + e.getMessage()));
			throw new AzureFileUploadException("Azure upload failed for document: " + documentName, e);
		}
	}

	private void saveDocumentMetadata(DocumentInfoDto documentInfo, DocumentBatch batch,
									  String blobUrl, DocumentType documentType, DocumentSubType documentSubType,
									  List<FileStatusResponseDto> fileStatuses) {
		try {
			Document document = new Document();
			document.setName(documentInfo.getDocumentName());
			document.setDocumentType(documentType);
			document.setDocumentSubType(documentSubType);
			Priority priority = priorityService.getPriorityByLabel(documentInfo.getPriority());
			document.setPriority(priority);
			document.setPath(blobUrl);
			document.setIsArchive(false);
			document.setRevisionNo(0);
			document.setCreatedBy(getLoggedInUserEmailId());

			if (!Objects.isNull(batch)) {
				document.setDocumentBatch(batch);
			}
			Document savedDocument = documentRepository.save(document);
			assignDocumentStatus(savedDocument, CommonConstants.PENDING_EXTRACTION);

			fileStatuses.add(new FileStatusResponseDto(documentInfo.getDocumentName(), CommonConstants.SUCCESS, "File uploaded successfully"));
		} catch (Exception e) {
			fileStatuses.add(new FileStatusResponseDto(documentInfo.getDocumentName(), CommonConstants.FAILURE, "Database save failed: " + e.getMessage()));
			throw new RuntimeException("Document save failed: " + e.getMessage());
		}
	}

	public void assignDocumentStatus(Document document, String statusName) {
		com.iqeq.model.DocumentStatus status = documentStatusRepository.findByLabel(statusName)
				.orElseThrow(() -> new RuntimeException("Document status not found: " + statusName));

		try{
			DocumentStatusMappingId mappingId = new DocumentStatusMappingId();
			mappingId.setDocumentId(document.getDocumentId());
			mappingId.setStatusId(status.getDocumentStatusId());

			DocumentStatusMapping documentStatusMapping = new DocumentStatusMapping();
			documentStatusMapping.setId(mappingId);
			documentStatusMapping.setDocument(document);
			documentStatusMapping.setDocumentStatus(status);
			documentStatusMapping.setAssignedAt(LocalDateTime.now());
			documentStatusMapping.setIsArchive(false);
			documentStatusMapping.setRevisionNo(0);
			documentStatusMapping.setCreatedBy(getLoggedInUserEmailId());

			documentStatusMappingRepository.save(documentStatusMapping);
		}catch (Exception e){
			log.error("Error occurred while saving Document Status {}", e.getMessage());
			throw new RuntimeException("Status save Failed" + statusName );
		}

	}

}
