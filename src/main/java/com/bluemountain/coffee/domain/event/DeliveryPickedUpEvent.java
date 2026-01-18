package com.bluemountain.coffee.domain.event;

import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Domain event published when a delivery is picked up by the rider.
 */
public class DeliveryPickedUpEvent extends ApplicationEvent {
    private final DeliveryId deliveryId;
    private final LocalDateTime pickedUpAt;

    public DeliveryPickedUpEvent(Object source, DeliveryId deliveryId) {
        super(source);
        this.deliveryId = deliveryId;
        this.pickedUpAt = LocalDateTime.now();
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public LocalDateTime getPickedUpAt() {
        return pickedUpAt;
    }

    @Override
    public String toString() {
        return "DeliveryPickedUpEvent{" +
                "deliveryId=" + deliveryId +
                ", pickedUpAt=" + pickedUpAt +
                '}';
    }
}
