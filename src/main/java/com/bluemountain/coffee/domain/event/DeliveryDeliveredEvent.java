package com.bluemountain.coffee.domain.event;

import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Domain event published when a delivery is delivered to the customer.
 */
public class DeliveryDeliveredEvent extends ApplicationEvent {
    private final DeliveryId deliveryId;
    private final LocalDateTime deliveredAt;

    public DeliveryDeliveredEvent(Object source, DeliveryId deliveryId) {
        super(source);
        this.deliveryId = deliveryId;
        this.deliveredAt = LocalDateTime.now();
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    @Override
    public String toString() {
        return "DeliveryDeliveredEvent{" +
                "deliveryId=" + deliveryId +
                ", deliveredAt=" + deliveredAt +
                '}';
    }
}
