package com.iqeq.service;

import com.iqeq.config.RabbitMQConfigs;
import com.iqeq.dto.JobMessage;
import com.iqeq.repository.JobRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@AllArgsConstructor
@Component
public class DocumentUploadListener {

    private final JobService jobService;

    @RabbitListener(queues = RabbitMQConfigs.QUEUE_NAME, concurrency = "3")
    public void processUploadMessage(JobMessage message) {
        String jobId = message.getJobId();
        Path filePath = Paths.get(message.getFilePath());
        jobService.handleUploadAndSave(jobId, filePath);
    }
}

