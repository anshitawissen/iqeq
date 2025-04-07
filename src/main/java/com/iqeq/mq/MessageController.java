package com.iqeq.mq;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO delete this controller
@RestController
@RequestMapping("/api/v1/auth")
public class MessageController {

    private final TopicMessageProducer producer;

    public MessageController(TopicMessageProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String topic, @RequestParam String message, @RequestParam int priority) {
        producer.sendMessage(topic, message, priority);
        return "Message sent to topic: " + topic + " with priority: " + priority;
    }
}
