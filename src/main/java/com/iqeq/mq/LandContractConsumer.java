package com.iqeq.mq;

import com.iqeq.config.RabbitMQConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LandContractConsumer {

    private static final Logger logger = LogManager.getLogger(LandContractConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LAND_CONTRACT)
    public void receiveLandContractMessage(String message) {

            logger.info("Land contract Consumer Received:: {}", message);

    }
}
