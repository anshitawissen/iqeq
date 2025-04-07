package com.iqeq.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AzureBlobService implements DocumentUploadClientService {

    private final BlobServiceClient blobServiceClient;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Override
    public String uploadFile(MultipartFile file, String documentName, String filePath) {
        try {
            // Generate a unique file name
            String fileName = UUID.randomUUID() + "_" + documentName;

            // Get the container client
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            BlobClient blobClient = containerClient.getBlobClient(filePath + fileName);

            // Upload file to Azure Blob
            try (InputStream inputStream = file.getInputStream()) {
                blobClient.upload(inputStream, file.getSize(), true);
                blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(file.getContentType()));
            }

            log.info("File uploaded successfully to Azure Blob: {}", blobClient.getBlobUrl());
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            log.error("Azure Blob upload failed: {}", e.getMessage(), e);
            throw new RuntimeException("Azure Blob upload failed: " + e.getMessage());
        }
    }
}
