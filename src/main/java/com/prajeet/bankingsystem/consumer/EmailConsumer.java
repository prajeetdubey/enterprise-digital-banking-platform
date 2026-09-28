package com.prajeet.bankingsystem.consumer;

import com.prajeet.bankingsystem.config.KafkaConfig;
import com.prajeet.bankingsystem.event.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @KafkaListener(
            topics = KafkaConfig.TRANSACTION_TOPIC,
            groupId = "email-service"
    )
    public void consume(TransactionEvent event) {

        System.out.println(
                "📧 EMAIL CONSUMER"
        );

        System.out.println(
                "Email notification generated for transaction: "
                        + event.getTransactionId()
        );
    }
}