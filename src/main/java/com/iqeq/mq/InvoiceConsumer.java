package com.iqeq.mq;

import com.iqeq.config.RabbitMQConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class InvoiceConsumer {

    private static final Logger logger = LogManager.getLogger(InvoiceConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_INVOICE)
    public void receiveInvoiceMessage(String message) {

            logger.info("Invoice Consumer Received: {}", message);

    }
}
