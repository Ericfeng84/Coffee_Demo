package com.bluemountain.coffee.domain.event;

import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain event published when a delivery is created.
 */
public class DeliveryCreatedEvent extends ApplicationEvent {
    private final DeliveryId deliveryId;
    private final List<String> orderIds;
    private final LocalDateTime createdAt;

    public DeliveryCreatedEvent(Object source, DeliveryId deliveryId, List<String> orderIds) {
        super(source);
        this.deliveryId = deliveryId;
        this.orderIds = orderIds;
        this.createdAt = LocalDateTime.now();
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "DeliveryCreatedEvent{" +
                "deliveryId=" + deliveryId +
                ", orderIds=" + orderIds +
                ", createdAt=" + createdAt +
                '}';
    }
}
