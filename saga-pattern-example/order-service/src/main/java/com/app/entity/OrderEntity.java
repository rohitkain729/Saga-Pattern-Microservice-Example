package com.app.entity;

import com.app.commons.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId;
    private String productId;
    private int quantity;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
}
