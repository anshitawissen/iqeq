package com.iqeq.service;

import com.iqeq.dto.FileWithExcelResponse;
import com.iqeq.dto.JobUploadRequestDto;
import com.iqeq.model.Job;
import com.iqeq.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import com.jcraft.jsch.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;

    public String upload(JobUploadRequestDto request) {
        String jobId = request.getDocumentName() + "_" + request.getDocumentType() + "_" + request.getPriority();
        Job job = new Job();
        job.setJobId(jobId);
        job.setDocumentName(request.getDocumentName());
        job.setDocumentType(request.getDocumentType());
        job.setPriority(request.getPriority());
        job.setStatus("WIP");
        job.setResult("In progress");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
        try {
            // Save to temp file before starting async
            Path tempFile = Files.createTempFile(jobId, ".pdf");
            request.getFile().transferTo(tempFile.toFile());

            // Async file transfer
            CompletableFuture.runAsync(() -> handleUploadAndSave(jobId, tempFile));
        } catch (IOException e) {
            job.setStatus("FAILED");
            job.setResult("Failed to save file: " + e.getMessage());
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);
            throw new RuntimeException("File save error", e);
        }
        return jobId;
    }

    public void handleUploadAndSave(String jobId, Path filePath) {
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            // Setup SSH connection
            JSch jsch = new JSch();
            session = jsch.getSession("iqeq", "10.221.162.5", 22);
            session.setPassword("Wissen@123");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            // Create remote directory
            String remoteDir = "/home/iqeq/iqeq_storage/" + jobId;
            try {
                sftpChannel.mkdir(remoteDir);
            } catch (SftpException e) {
                // Folder may already exist
                if (e.id != ChannelSftp.SSH_FX_FAILURE) throw e;
            }

            sftpChannel.cd(remoteDir);

            // Upload file using try-with-resources to ensure file is closed
            try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
                sftpChannel.put(fis, jobId + ".pdf");
            }

            // Update job status
            Job job = jobRepository.findById(jobId).orElseThrow();
            job.setStatus("COMPLETED");
            job.setResult("Success");
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);

        } catch (Exception e) {
            e.printStackTrace();
            Job job = jobRepository.findById(jobId).orElseThrow();
            job.setStatus("FAILED");
            job.setResult("Failed due to: " + e.getMessage());
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);
        } finally {
            // Always attempt file cleanup at the end
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                System.err.println("Failed to delete temp file: " + ex.getMessage());
            }

            if (sftpChannel != null) sftpChannel.disconnect();
            if (session != null) session.disconnect();
        }
    }

    public ResponseEntity<FileWithExcelResponse> downloadFileWithExcel(String jobId) throws IOException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        if (!"COMPLETED".equalsIgnoreCase(job.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not ready for download");
        }

        String remoteDir = "/home/iqeq/iqeq_storage/" + jobId + "/";
        Path excelTemp = Files.createTempFile(jobId + "_excel", ".xlsx");

        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession("iqeq", "10.221.162.5", 22);
            session.setPassword("Wissen@123");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.get(remoteDir + jobId + ".xlsx", excelTemp.toString());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download Excel file: " + e.getMessage());
        } finally {
            if (sftpChannel != null) sftpChannel.disconnect();
            if (session != null) session.disconnect();
        }

        List<List<String>> excelData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelTemp.toFile());
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                boolean isRowEmpty = true;

                for (Cell cell : row) {
                    String value = switch (cell.getCellType()) {
                        case STRING -> cell.getStringCellValue();
                        case _NONE -> null;
                        case NUMERIC -> String.valueOf(cell.getNumericCellValue());
                        case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                        case FORMULA -> cell.getCellFormula();
                        case BLANK -> "";
                        case ERROR -> null;
                    };

                    if (!value.isBlank()) isRowEmpty = false;
                    rowData.add(value);
                }

                if (!isRowEmpty) {
                    excelData.add(rowData);
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse Excel file: " + e.getMessage());
        } finally {
            Files.deleteIfExists(excelTemp);
        }

        FileWithExcelResponse response = new FileWithExcelResponse(
                jobId,
                excelData
        );

        return ResponseEntity.ok(response);
    }


}

