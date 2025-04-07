package com.iqeq.service;

import com.iqeq.exception.ResourceNotFoundException;
import com.iqeq.model.DocumentType;
import com.iqeq.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentType getDocumentTypeById(Long documentTypeId) {
        Optional<DocumentType> documentType = documentTypeRepository.findById(documentTypeId);
        if (documentType.isEmpty()) {
            throw new ResourceNotFoundException("DocumentType", "ID", documentTypeId);
        }
        return documentType.get();
    }
}
