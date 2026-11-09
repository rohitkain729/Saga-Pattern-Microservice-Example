package com.app.service;

import com.app.commons.events.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @KafkaListener(topics = "inventory-command", groupId = "inventory-service")
    public void handleInventory(OrderEvent event) {
        System.out.println("reserving inventory::" + event.orderId());
        boolean available = true;
        if (available) {
            kafkaTemplate.send("inventory-reserved", event.orderId(), event);
        } else {
            System.out.println("inventory unavailable");
            kafkaTemplate.send("inventory-failed", event.orderId(), event);
        }
    }

}
