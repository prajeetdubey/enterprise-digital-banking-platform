package com.prajeet.bankingsystem.consumer;

import com.prajeet.bankingsystem.config.KafkaConfig;
import com.prajeet.bankingsystem.event.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LoggingConsumer {

    @KafkaListener(
            topics = KafkaConfig.TRANSACTION_TOPIC,
            groupId = "logging-service"
    )
    public void consume(TransactionEvent event) {

        System.out.println(
                "📝 LOGGING CONSUMER"
        );

        System.out.println(
                "Transaction event received: "
                        + event.getTransactionId()
        );

        System.out.println(
                "Transaction type: "
                        + event.getTransactionType()
        );

        System.out.println(
                "Amount: "
                        + event.getAmount()
        );
    }
}