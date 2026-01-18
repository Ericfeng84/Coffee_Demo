package com.bluemountain.coffee.interfaces.dto;

import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for Delivery.
 * Used for transferring delivery data between layers.
 */
public class DeliveryDTO {
    private String deliveryId;
    private List<DeliveryItemDTO> items;
    private RiderInfoDTO riderInfo;
    private DeliveryStatus status;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DeliveryDTO() {
    }

    public DeliveryDTO(String deliveryId, List<DeliveryItemDTO> items, RiderInfoDTO riderInfo,
                       DeliveryStatus status, LocalDateTime pickupTime, LocalDateTime deliveryTime,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.deliveryId = deliveryId;
        this.items = items;
        this.riderInfo = riderInfo;
        this.status = status;
        this.pickupTime = pickupTime;
        this.deliveryTime = deliveryTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public List<DeliveryItemDTO> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItemDTO> items) {
        this.items = items;
    }

    public RiderInfoDTO getRiderInfo() {
        return riderInfo;
    }

    public void setRiderInfo(RiderInfoDTO riderInfo) {
        this.riderInfo = riderInfo;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryDTO that = (DeliveryDTO) o;
        return Objects.equals(deliveryId, that.deliveryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryId);
    }

    @Override
    public String toString() {
        return "DeliveryDTO{" +
                "deliveryId='" + deliveryId + '\'' +
                ", itemCount=" + (items != null ? items.size() : 0) +
                ", riderInfo=" + riderInfo +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
