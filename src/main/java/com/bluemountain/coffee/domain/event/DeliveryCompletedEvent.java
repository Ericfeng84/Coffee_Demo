package com.bluemountain.coffee.domain.event;

import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Domain event published when a delivery is completed.
 */
public class DeliveryCompletedEvent extends ApplicationEvent {
    private final DeliveryId deliveryId;
    private final LocalDateTime completedAt;

    public DeliveryCompletedEvent(Object source, DeliveryId deliveryId) {
        super(source);
        this.deliveryId = deliveryId;
        this.completedAt = LocalDateTime.now();
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    @Override
    public String toString() {
        return "DeliveryCompletedEvent{" +
                "deliveryId=" + deliveryId +
                ", completedAt=" + completedAt +
                '}';
    }
}
