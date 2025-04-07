package com.iqeq.mq;

import com.iqeq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String topic, String message, int priority) {
        String routingKey = "topic." + topic;
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                routingKey, 
                message, 
                msg -> {
                    msg.getMessageProperties().setPriority(priority);
                    return msg;
                }
        );
        System.out.println("Sent: " + message + " to topic: " + topic + " with priority: " + priority);
    }
}
