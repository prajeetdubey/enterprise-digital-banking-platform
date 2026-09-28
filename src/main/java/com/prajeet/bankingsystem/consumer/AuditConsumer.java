package com.prajeet.bankingsystem.consumer;

import com.prajeet.bankingsystem.config.KafkaConfig;
import com.prajeet.bankingsystem.event.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditConsumer {

    @KafkaListener(
            topics = KafkaConfig.TRANSACTION_TOPIC,
            groupId = "audit-service"
    )
    public void consume(TransactionEvent event) {

        System.out.println(
                "🔐 AUDIT CONSUMER"
        );

        System.out.println(
                "Transaction recorded in audit system: "
                        + event.getTransactionId()
        );
    }
}