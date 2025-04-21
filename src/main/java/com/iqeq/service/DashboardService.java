package com.iqeq.service;

import com.iqeq.dto.DocumentResponseDto;
import com.iqeq.model.Job;
import com.iqeq.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class DashboardService {
    private final JobRepository jobRepository;
    public DashboardService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    public Map<String, List<DocumentResponseDto>> getDocumentsGrouped(int page, int size) {
        int offset = page * size;
        List<Job> jobs = jobRepository.findJobsGroupedByTypeWithPagination(offset, size);

        return jobs.stream()
                .map(this::mapToDto)
                .collect(Collectors.groupingBy(DocumentResponseDto::getDocumentType));
    }


    private DocumentResponseDto mapToDto(Job job) {
        DocumentResponseDto dto = new DocumentResponseDto();
        dto.setDocumentName(job.getDocumentName());
        dto.setDocumentType(job.getDocumentType());
        dto.setJobId(job.getJobId());
        dto.setStatus(job.getStatus().name());
        dto.setPriority(job.getPriority());
        dto.setResult(job.getResult());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());
        return dto;
    }

}
