package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.config.KafkaConfig;
import com.prajeet.bankingsystem.event.TransactionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaTransactionProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public KafkaTransactionProducer(
            KafkaTemplate<String, TransactionEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionEvent(TransactionEvent event) {

        kafkaTemplate.send(
                KafkaConfig.TRANSACTION_TOPIC,
                String.valueOf(event.getTransactionId()),
                event
        );

        System.out.println(
                "Kafka Event Sent: Transaction ID = "
                        + event.getTransactionId()
        );
    }
}