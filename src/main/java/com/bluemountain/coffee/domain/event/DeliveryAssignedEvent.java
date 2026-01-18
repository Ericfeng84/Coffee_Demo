package com.bluemountain.coffee.domain.event;

import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import com.bluemountain.coffee.domain.model.valobj.RiderInfo;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Domain event published when a rider is assigned to a delivery.
 */
public class DeliveryAssignedEvent extends ApplicationEvent {
    private final DeliveryId deliveryId;
    private final RiderInfo riderInfo;
    private final LocalDateTime assignedAt;

    public DeliveryAssignedEvent(Object source, DeliveryId deliveryId, RiderInfo riderInfo) {
        super(source);
        this.deliveryId = deliveryId;
        this.riderInfo = riderInfo;
        this.assignedAt = LocalDateTime.now();
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    @Override
    public String toString() {
        return "DeliveryAssignedEvent{" +
                "deliveryId=" + deliveryId +
                ", riderInfo=" + riderInfo +
                ", assignedAt=" + assignedAt +
                '}';
    }
}
