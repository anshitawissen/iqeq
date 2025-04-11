package com.iqeq.scheduler;

import com.iqeq.model.Job;
import com.iqeq.repository.JobRepository;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
                if (isFilePresentInWorkstation(job.getJobId())) {
                    markJobAsCompleted(job.getJobId());
                } else {
                    System.out.println("File not yet present in workstation for jobId: " + job.getJobId());
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

    private boolean isFilePresentInWorkstation(String jobId) {
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

            String remoteFilePath = "/shared_disk/iqeq/" + jobId + "/" + jobId + ".xlsx";

            // Check file existence
            try {
                sftpChannel.stat(remoteFilePath);
                return true; // file exists
            } catch (Exception e) {
                return false; // file does not exist
            }

        } catch (Exception e) {
            System.err.println("Failed to check file in workstation: " + e.getMessage());
            return false;
        } finally {
            if (sftpChannel != null) sftpChannel.disconnect();
            if (session != null) session.disconnect();
        }
    }
}
