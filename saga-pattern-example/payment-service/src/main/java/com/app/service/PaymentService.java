package com.app.service;

import com.app.commons.events.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @KafkaListener(topics = "payment-command", groupId = "payment-service")
    public void processPayment(OrderEvent event) {
        System.out.println("processing payment for::" + event.orderId());
        boolean paymentSuccessFull = true;
        if (paymentSuccessFull) {
            System.out.println("payment is successful");
            kafkaTemplate.send("payment-completed", event.orderId(), event);
        } else {
            kafkaTemplate.send("payment-failed", event.orderId(), event);
        }
    }

    @KafkaListener(topics = "payment-refund-command", groupId = "payment-service")
    public void handlePaymentRefund(OrderEvent event) {

        System.out.println("refund payment for id::" + event.orderId());
        // refund payment
        //update paymnet status  = REFUNDED
        kafkaTemplate.send("payment-refunded", event.orderId(), event);

    }


}
