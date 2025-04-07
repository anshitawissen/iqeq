package com.iqeq.service;

import com.iqeq.dto.FieldDto;
import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.dto.common.SearchResponse;
import com.iqeq.exception.CustomException;
import com.iqeq.exception.ResourceNotFoundException;
import com.iqeq.model.DataType;
import com.iqeq.model.DocumentSubType;
import com.iqeq.model.DocumentType;
import com.iqeq.model.Field;
import com.iqeq.repository.FieldRepository;
import com.iqeq.util.CommonServiceUtility;
import com.iqeq.util.FieldServiceUtility;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldServiceUtility fieldServiceUtility;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private DataTypeService dataTypeService;

    @Autowired
    private DocumentSubTypeService documentSubTypeService;

    public SearchResponse getFieldsWithColumns(SearchRequestDto searchRequestDto) throws CustomException {
        searchRequestDto.validSearchRequest(searchRequestDto);
        Sort sort = CommonServiceUtility.applySortFilter(searchRequestDto);
        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), searchRequestDto.getSize(), sort);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        fieldServiceUtility.setFilters(searchRequestDto, booleanBuilder);

        Page<Field> fieldPage = fieldRepository.findAll(booleanBuilder, pageable);

        if (fieldPage.hasContent()) {
            List<FieldDto> fieldDtos = fieldPage.getContent().stream()
                    .map(FieldDto::build)
                    .toList();
            return SearchResponse.build(fieldDtos, fieldPage, searchRequestDto.getIsDashboard());
        }
        return new SearchResponse();
    }

    public FieldDto createField(FieldDto fieldDto) throws CustomException {
        DocumentType documentType = documentTypeService.getDocumentTypeById(fieldDto.getDocumentTypeId());
        DataType dataType = dataTypeService.getDataTypeById(fieldDto.getDataTypeId());
        DocumentSubType documentSubType = null;
        if(fieldDto.getDocumentSubTypeId() != null) {
            documentSubType = documentSubTypeService.getDocumentSubTypeById(fieldDto.getDocumentSubTypeId());
        }

        Field field = FieldDto.buildField(fieldDto, documentType, dataType, documentSubType);
        Field existingField = fieldRepository.findByNameAndDocumentTypeAndIsArchive(fieldDto.getName(), documentType, false);
        if (existingField != null) {
            throw new CustomException(400, "Field name " + fieldDto.getName() + " is already exists.");
        }

        field.setCreatedDate(LocalDateTime.now());
        field.setCreatedBy(CommonServiceUtility.getLoggedInUserEmailId());
        field.setIsArchive(false);
        field.setRevisionNo(0);
        return FieldDto.build(fieldRepository.save(field));
    }

    public FieldDto updateField(Long fieldId, FieldDto fieldDto) throws CustomException {
        Field existingField = getFieldById(fieldId);

        //Check if field with name is already exists
        Field existingFieldByName = fieldRepository.findByNameAndIsArchive(fieldDto.getName(), false);
        if (existingFieldByName != null && !existingFieldByName.getFieldId().equals(fieldId)) {
            throw new CustomException(400, "Field name " + fieldDto.getName() + " is already exists.");
        }
        DocumentType documentType = documentTypeService.getDocumentTypeById(fieldDto.getDocumentTypeId());
        DataType dataType = dataTypeService.getDataTypeById(fieldDto.getDataTypeId());
        DocumentSubType documentSubType = null;
        if(fieldDto.getDocumentSubTypeId() != null) {
            documentSubType = documentSubTypeService.getDocumentSubTypeById(fieldDto.getDocumentSubTypeId());
        }
        existingField.setDocumentType(documentType);
        existingField.setDataType(dataType);
        existingField.setDocumentSubType(documentSubType);
        existingField.setName(fieldDto.getName());
        existingField.setDescription(fieldDto.getDescription());
        existingField.setIsUnique(fieldDto.getIsUnique());
        existingField.setIsMandatory(fieldDto.getIsMandatory());
        existingField.setConfidenceScore(fieldDto.getConfidenceScore());
        existingField.setUpdatedDate(LocalDateTime.now());
        existingField.setUpdatedBy(CommonServiceUtility.getLoggedInUserEmailId());
        existingField.setRevisionNo(existingField.getRevisionNo() + 1);
        return FieldDto.build(fieldRepository.save(existingField));
    }

    public FieldDto deleteField(Long fieldId) {
        Field field = getFieldById(fieldId);

        try {
            field.setIsArchive(true);
            fieldRepository.save(field);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Cannot delete field");
        }
        return FieldDto.build(field);
    }

    public Field getFieldById(Long fieldId) {
        Optional<Field> field = fieldRepository.findByFieldIdAndIsArchive(fieldId, false);
        if (field.isPresent()) {
            return field.get();
        } else {
            throw new ResourceNotFoundException("Field", "ID", fieldId);
        }
    }
}
