package com.bluemountain.coffee.domain.model.valobj;

import com.bluemountain.coffee.domain.model.aggregate.Delivery;
import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Value Object representing a delivery slip for printing.
 * This is an immutable value object derived from Delivery aggregate.
 * 
 * DDD Concept: Value Objects are immutable objects that are defined
 * by their attributes rather than identity. Two DeliverySlip objects with
 * the same attributes are considered equal.
 * 
 * Design Patterns:
 * - Builder Pattern: Provides fluent API for creating complex objects
 * - Immutable Object: Once created, cannot be modified
 */
public class DeliverySlip {
    private final DeliveryId deliveryId;
    private final List<DeliverySlipItem> items;
    private final RiderInfo riderInfo;
    private final DeliveryStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime pickupTime;
    private final LocalDateTime deliveryTime;

    private DeliverySlip(Builder builder) {
        this.deliveryId = Objects.requireNonNull(builder.deliveryId, "Delivery ID cannot be null");
        this.items = List.copyOf(Objects.requireNonNull(builder.items, "Items cannot be null"));
        this.riderInfo = builder.riderInfo;
        this.status = Objects.requireNonNull(builder.status, "Status cannot be null");
        this.createdAt = Objects.requireNonNull(builder.createdAt, "Created at cannot be null");
        this.pickupTime = builder.pickupTime;
        this.deliveryTime = builder.deliveryTime;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public List<DeliverySlipItem> getItems() {
        return items;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Get the total number of orders in this delivery
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Get the total number of items (products) across all orders
     */
    public int getTotalProductCount() {
        return items.stream()
            .mapToInt(DeliverySlipItem::getItemCount)
            .sum();
    }

    /**
     * Create a DeliverySlip from a Delivery aggregate
     */
    public static DeliverySlip fromDelivery(Delivery delivery) {
        Objects.requireNonNull(delivery, "Delivery cannot be null");

        List<DeliverySlipItem> items = delivery.getItems().stream()
            .map(item -> DeliverySlipItem.fromDeliveryItem(item))
            .collect(Collectors.toList());

        return new Builder()
            .deliveryId(delivery.getDeliveryId())
            .items(items)
            .riderInfo(delivery.getRiderInfo())
            .status(delivery.getStatus())
            .createdAt(delivery.getCreatedAt())
            .pickupTime(delivery.getPickupTime())
            .deliveryTime(delivery.getDeliveryTime())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliverySlip that = (DeliverySlip) o;
        return Objects.equals(deliveryId, that.deliveryId) &&
                Objects.equals(items, that.items) &&
                Objects.equals(riderInfo, that.riderInfo) &&
                status == that.status &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(pickupTime, that.pickupTime) &&
                Objects.equals(deliveryTime, that.deliveryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryId, items, riderInfo, status, createdAt, pickupTime, deliveryTime);
    }

    @Override
    public String toString() {
        return "DeliverySlip{" +
                "deliveryId=" + deliveryId +
                ", itemCount=" + items.size() +
                ", riderInfo=" + riderInfo +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", pickupTime=" + pickupTime +
                ", deliveryTime=" + deliveryTime +
                '}';
    }

    /**
     * Builder for DeliverySlip
     */
    public static class Builder {
        private DeliveryId deliveryId;
        private List<DeliverySlipItem> items;
        private RiderInfo riderInfo;
        private DeliveryStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime pickupTime;
        private LocalDateTime deliveryTime;

        public Builder deliveryId(DeliveryId deliveryId) {
            this.deliveryId = deliveryId;
            return this;
        }

        public Builder items(List<DeliverySlipItem> items) {
            this.items = items;
            return this;
        }

        public Builder riderInfo(RiderInfo riderInfo) {
            this.riderInfo = riderInfo;
            return this;
        }

        public Builder status(DeliveryStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder pickupTime(LocalDateTime pickupTime) {
            this.pickupTime = pickupTime;
            return this;
        }

        public Builder deliveryTime(LocalDateTime deliveryTime) {
            this.deliveryTime = deliveryTime;
            return this;
        }

        public DeliverySlip build() {
            return new DeliverySlip(this);
        }
    }
}
