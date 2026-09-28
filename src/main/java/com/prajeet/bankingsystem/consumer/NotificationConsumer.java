package com.prajeet.bankingsystem.consumer;

import com.prajeet.bankingsystem.config.KafkaConfig;
import com.prajeet.bankingsystem.event.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(
            topics = KafkaConfig.TRANSACTION_TOPIC,
            groupId = "notification-service"
    )
    public void consume(TransactionEvent event) {

        System.out.println(
                "📱 NOTIFICATION CONSUMER"
        );

        System.out.println(
                "Transaction notification: Transaction ID = "
                        + event.getTransactionId()
        );

        System.out.println(
                "Amount = " + event.getAmount()
        );
    }
}