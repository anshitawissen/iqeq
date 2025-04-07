package com.iqeq.service;

import com.iqeq.dto.DocumentSubTypeDto;
import com.iqeq.exception.CustomException;
import com.iqeq.exception.ResourceNotFoundException;
import com.iqeq.model.DocumentSubType;
import com.iqeq.model.DocumentType;
import com.iqeq.repository.DocumentSubTypeRepository;
import com.iqeq.repository.DocumentTypeRepository;
import com.iqeq.util.CommonServiceUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.iqeq.dto.DocumentSubTypeDto.build;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentSubTypeService {

    private final ModelMapper modelMapper;

    private final DocumentSubTypeRepository documentSubTypeRepository;

    private final DocumentTypeRepository documentTypeRepository;

    public List<DocumentSubTypeDto> getDocumentSubTypesByDocumentType(Long documentTypeId) {
        List<DocumentSubType> documentSubTypes = documentSubTypeRepository.findByDocumentType_DocumentTypeIdOrderByNameAsc(documentTypeId);
        return documentSubTypes.stream()
                .map(DocumentSubTypeDto::build).toList();
    }

    public DocumentSubTypeDto createDocumentSubType(@Valid DocumentSubTypeDto documentSubTypeDto) throws CustomException {
        Optional<DocumentSubType> documentSubTypeOptional = documentSubTypeRepository.findByNameAndIsArchiveFalse(documentSubTypeDto.getName());
        if(documentSubTypeOptional.isPresent())
        {
            throw new CustomException(400, "Document Subtype "+ documentSubTypeDto.getName() +" already exist.");
        }
        DocumentSubType documentSubType = modelMapper.map(documentSubTypeDto, DocumentSubType.class);
        DocumentType documentType = documentTypeRepository.findById(documentSubTypeDto.getDocumentTypeId())
                .orElseThrow(() -> new CustomException(404, "Document Type not found"));
        documentSubType.setDocumentType(documentType);

        if (Objects.isNull(documentSubType.getCreatedBy())) {
            documentSubType.setCreatedBy(CommonServiceUtility.getLoggedInUserEmailId());
        }
        documentSubType.setCreatedDate(LocalDateTime.now());
        documentSubType.setRevisionNo(0);
        return build(documentSubTypeRepository.save(documentSubType));
    }

    public DocumentSubType getDocumentSubTypeById(Long documentSubTypeId) {
        Optional<DocumentSubType> documentSubType = documentSubTypeRepository.findByDocumentSubTypeIdAndIsArchive(documentSubTypeId, false);
        if (documentSubType.isEmpty()) {
            throw new ResourceNotFoundException("DocumentSubType", "ID", documentSubTypeId);
        }
        return documentSubType.get();
    }
}
