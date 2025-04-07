package com.iqeq.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentUploadClientService {

    String uploadFile(MultipartFile file, String documentName, String filePath);
}

