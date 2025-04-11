package com.iqeq.service;

import com.iqeq.dto.FileWithExcelResponse;
import com.iqeq.dto.JobUploadRequestDto;
import com.iqeq.model.Job;
import com.iqeq.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import com.jcraft.jsch.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
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
            Path tempFile = Files.createTempFile(jobId, ".pdf");
            request.getFile().transferTo(tempFile.toFile());
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
            JSch jsch = new JSch();
            session = jsch.getSession("iqeq", "10.221.162.2", 22);
            session.setPassword("Wissen@123");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            String remoteDir = "/shared_disk/iqeq/" + jobId;
            try {
                sftpChannel.mkdir(remoteDir);
            } catch (SftpException e) {
                if (e.id != ChannelSftp.SSH_FX_FAILURE) throw e;
            }
            sftpChannel.cd(remoteDir);
            try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
                sftpChannel.put(fis, jobId + ".pdf");
            }
            System.out.println("Pdf file is created successfully");
            String path = callWorkstationApi(remoteDir + "/" + jobId + ".pdf");
            System.out.println("Path "+path);
            if(path != null) {
                Job job = jobRepository.findById(jobId).orElseThrow();
                job.setStatus("COMPLETED");
                job.setResult("Success");
                job.setUpdatedAt(LocalDateTime.now());
                jobRepository.save(job);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Job job = jobRepository.findById(jobId).orElseThrow();
            job.setStatus("FAILED");
            job.setResult("Failed due to: " + e.getMessage());
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);
        } finally {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                System.err.println("Failed to delete temp file: " + ex.getMessage());
            }

            if (sftpChannel != null) sftpChannel.disconnect();
            if (session != null) session.disconnect();
        }
    }

    private String callWorkstationApi(String filePath) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String uploadApiUrl = "http://10.221.162.2:7061/upload";
            MultiValueMap<String, Object> uploadBody = new LinkedMultiValueMap<>();
            System.out.println("filePath "+filePath);
            uploadBody.add("path", filePath);
            HttpHeaders uploadHeaders = new HttpHeaders();
            uploadHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> uploadRequest = new HttpEntity<>(uploadBody, uploadHeaders);
            ResponseEntity<Map> uploadResponse = restTemplate.postForEntity(uploadApiUrl, uploadRequest, Map.class);
            if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Workstation upload API call failed: " + uploadResponse.getStatusCode());
            }
            Map<String, Object> uploadResponseBody = uploadResponse.getBody();
            System.out.println("Upload done");
            if (uploadResponseBody != null && uploadResponseBody.containsKey("saved_json_path")) {
                String jsonPath = uploadResponseBody.get("saved_json_path").toString();
                System.out.println("JSON saved at: " + jsonPath);
                String downloadApiUrl = "http://10.221.162.2:7018/download";
                Map<String, String> downloadBody = new HashMap<>();
                downloadBody.put("path", jsonPath);
                HttpHeaders downloadHeaders = new HttpHeaders();
                downloadHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, String>> downloadRequest = new HttpEntity<>(downloadBody, downloadHeaders);
                ResponseEntity<Map> downloadResponse = restTemplate.postForEntity(downloadApiUrl, downloadRequest, Map.class);
                if (!downloadResponse.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Workstation download API call failed: " + downloadResponse.getStatusCode());
                }
                Map<String, Object> downloadResponseBody = downloadResponse.getBody();
                if (downloadResponseBody != null && downloadResponseBody.containsKey("saved_json_path")) {
                    String xlsxPath = downloadResponseBody.get("saved_json_path").toString();
                    System.out.println("Excel saved at: " + xlsxPath);
                    return xlsxPath;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in workstation API chain: " + e.getMessage());
        }
        return null;
    }


    public ResponseEntity<FileWithExcelResponse> downloadFileWithExcel(String jobId) throws IOException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        if (!"COMPLETED".equalsIgnoreCase(job.getStatus())) {
            return ResponseEntity.ok()
                    .body(new FileWithExcelResponse(jobId, List.of(
                            Map.of("message", "File not ready for download")
                    )));
        }

        String remoteDir = "/shared_disk/iqeq/" + jobId + "/";
        Path excelTemp = Files.createTempFile(jobId + "_excel", ".xlsx");

        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession("iqeq", "10.221.162.2", 22);
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

        List<Map<String, String>> excelData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelTemp.toFile());
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<String> headers = new ArrayList<>();

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                if (rowIndex == 0) {
                    // First row as headers
                    for (Cell cell : row) {
                        headers.add(getCellValue(cell));
                    }
                } else {
                    Map<String, String> rowMap = new LinkedHashMap<>();
                    boolean isRowEmpty = true;

                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.getCell(i);
                        String value = getCellValue(cell);
                        if (value != null && !value.isBlank()) isRowEmpty = false;
                        rowMap.put(headers.get(i), value);
                    }

                    if (!isRowEmpty) {
                        excelData.add(rowMap);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse Excel file: " + e.getMessage());
        } finally {
            Files.deleteIfExists(excelTemp);
        }

        FileWithExcelResponse response = new FileWithExcelResponse(jobId, excelData);
        return ResponseEntity.ok(response);
    }
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK, _NONE, ERROR -> "";
        };
    }



}

