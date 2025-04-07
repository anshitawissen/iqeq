package com.iqeq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_INVOICE = "idp_invoice_queue";
    public static final String QUEUE_LETTER_CREDIT = "idp_letter_credit_queue";
    public static final String QUEUE_LAND_CONTRACT = "idp_land_contract_queue";
    public static final String EXCHANGE_NAME = "priority_topic_exchange";

    @Bean
    Queue invoiceQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        return new Queue(QUEUE_INVOICE, true, false, false, args);
    }

    @Bean
    Queue letterCreditQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        return new Queue(QUEUE_LETTER_CREDIT, true, false, false, args);
    }

    @Bean
    Queue landContractQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        return new Queue(QUEUE_LAND_CONTRACT, true, false, false, args);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding invoiceTopic(Queue invoiceQueue, TopicExchange exchange) {
        return BindingBuilder.bind(invoiceQueue).to(exchange).with("topic.invoice");
    }

    @Bean
    Binding landContractTopic(Queue landContractQueue, TopicExchange exchange) {
        return BindingBuilder.bind(landContractQueue).to(exchange).with("topic.landContract");
    }

    @Bean
    Binding letterCreditTopic(Queue letterCreditQueue, TopicExchange exchange) {
        return BindingBuilder.bind(letterCreditQueue).to(exchange).with("topic.letterCredit");
    }
}
