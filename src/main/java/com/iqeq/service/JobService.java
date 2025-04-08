package com.iqeq.service;

import com.iqeq.dto.JobUploadRequestDto;
import com.iqeq.model.Job;
import com.iqeq.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;

    public String upload(JobUploadRequestDto request) {
        String jobId = request.getDocumentName() + "_" + request.getInvoiceType() + "_" + request.getPriority();

        Job job = new Job();
        job.setJobId(jobId);
        job.setDocumentName(request.getDocumentName());
        job.setInvoiceType(request.getInvoiceType());
        job.setPriority(request.getPriority());
        job.setStatus("WIP");
        job.setResult("In progress");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        jobRepository.save(job);

        CompletableFuture.runAsync(() -> handleUploadAndSave(request, jobId));

        return jobId;
    }

    public void handleUploadAndSave(JobUploadRequestDto request, String jobId) {
        try {
            // Ensure folder exists
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Save file locally
            Path path = uploadDir.resolve(jobId + ".pdf");
            Files.write(path, request.getFile().getBytes());

            // Mark job completed
            Job job = jobRepository.findById(jobId).orElseThrow();
            job.setStatus("COMPLETED");
            job.setResult("Success");
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);

        } catch (Exception e) {
            Job job = jobRepository.findById(jobId).orElseThrow();
            job.setStatus("FAILED");
            job.setResult("Failed due to: " + e.getMessage());
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);

            e.printStackTrace(); // for debugging
        }
    }

    public ResponseEntity<Resource> downloadFile(String jobId) throws IOException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        if (!"COMPLETED".equalsIgnoreCase(job.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not ready for download");
        }
        Path path = Paths.get("uploads/" + jobId + ".pdf");
        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found on server");
        }
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + jobId + ".pdf\"")
                .body(resource);
    }

}

