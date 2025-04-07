package com.iqeq.mq;

import com.iqeq.config.RabbitMQConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LetterCreditConsumer {

    private static final Logger logger = LogManager.getLogger(LetterCreditConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LETTER_CREDIT)
    public void receiveLetterCreditMessage(String message) {

            logger.info("Letter Credit Consumer Received:: {}", message);

    }
}
