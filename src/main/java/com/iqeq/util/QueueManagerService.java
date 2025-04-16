package com.iqeq.util;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QueueManagerService {

    private final AmqpAdmin amqpAdmin;

    public void purgeUploadQueue() {
        amqpAdmin.purgeQueue("document_upload_queue", true);
        System.out.println("Queue purged successfully.");
    }
}

