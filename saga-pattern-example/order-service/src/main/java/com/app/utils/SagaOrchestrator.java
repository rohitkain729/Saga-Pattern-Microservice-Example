package com.app.utils;

import com.app.commons.events.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SagaOrchestrator {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;


    @KafkaListener(topics = "order-created", groupId = "saga-orchestrator")
    public void handleOrderCreated(OrderEvent event) {
        System.out.println("saga started for order" + event.orderId());
        kafkaTemplate.send("payment-command", event.orderId(), event);
    }

    @KafkaListener(topics = "payment-completed", groupId = "saga-orchestrator")
    public void handlePaymentCompleted(OrderEvent event) {
        System.out.println("payment completed" + event.orderId());
        // kafka
        kafkaTemplate.send("inventory-command", event.orderId(), event);
    }

    @KafkaListener(topics = "inventory-reserved", groupId = "saga-orchestrator")
    public void handleInventoryReserved(OrderEvent event) {

        System.out.println("Inventory reserved  for::" + event.orderId());
        kafkaTemplate.send("order-confirmed", event.orderId(), event);

    }

    @KafkaListener(topics = "inventory-failed", groupId = "saga-orchestrator")
    public void handleInventoryFailed(OrderEvent event) {
        System.out.println("inventory failed for Order::" + event.orderId());
        System.out.println("statting compensation");
        kafkaTemplate.send("payment-refund-command", event.orderId(), event);
    }

    @KafkaListener(topics = "payment-refunded", groupId = "saga-orchestrator")
    public void handlePaymentRefunded(OrderEvent event) {
        System.out.println("payment trefunded::" + event.orderId());
        kafkaTemplate.send("order-cancelled", event.orderId(), event);
    }


}
