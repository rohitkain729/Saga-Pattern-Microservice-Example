package com.app.service;

import com.app.commons.OrderStatusEnum;
import com.app.commons.events.OrderEvent;
import com.app.entity.OrderEntity;
import com.app.reposiotry.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public String createOrder(OrderEvent event) {
        System.out.println("Creating order: " + event.orderId());
        OrderEntity orderEntity = dtoToEntityConverter(event);
        orderEntity.setOrderStatus(OrderStatusEnum.PENDING);
        repository.save(orderEntity);

        //kafka
        kafkaTemplate.send("order-created", orderEntity.getOrderId(), event);

        return "order created with Id::" + orderEntity.getOrderId();
    }

    private OrderEntity dtoToEntityConverter(OrderEvent event) {
        return new OrderEntity().builder()
                .amount(event.amount())
                .quantity(event.quantity())
                .productId(event.productId())
                .build();
    }

    @KafkaListener(topics = "order-confirmed", groupId = "order-service")
    public void confirmOrder(OrderEvent event) {
        System.out.println("confiremd order::" + event.orderId());

        // update database
        // status= confirmed

    }

    @KafkaListener(topics = "order-cancelled", groupId = "order-service")
    public void cancelOrder(OrderEvent event) {
        System.out.println("cancelled order::" + event.orderId());
        // update  database
        // cancel order
    }
}
