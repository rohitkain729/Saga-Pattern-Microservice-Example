package com.app.commons.events;


public record OrderEvent(String orderId,
                         String productId,
                         int quantity,
                         double amount) { }
