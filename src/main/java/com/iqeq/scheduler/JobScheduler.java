package com.iqeq.scheduler;

import com.iqeq.model.Job;
import com.iqeq.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Component
public class JobScheduler {

    private final JobRepository jobRepository;

    @Scheduled(fixedRate = 5000)
    public void processPendingJobs() {
        List<Job> pendingJobs = jobRepository.findAllByStatus("WIP");

        for (Job job : pendingJobs) {
            try {
                String filePath = "uploads/" + job.getJobId() + ".pdf";

                if (Files.exists(Paths.get(filePath))) {
                    markJobAsCompleted(job.getJobId());
                } else {
                    System.out.println("File not yet present for jobId: " + job.getJobId());
                }

            } catch (Exception e) {
                System.err.println("Scheduler error for jobId " + job.getJobId() + ": " + e.getMessage());
            }
        }
    }
    public void markJobAsCompleted(String jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        job.setStatus("COMPLETED");
        job.setResult("Success");
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

}
