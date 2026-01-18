package com.bluemountain.coffee.domain.model.aggregate;

import com.bluemountain.coffee.domain.model.enums.DeliveryItemStatus;
import com.bluemountain.coffee.domain.model.valobj.OrderId;

import java.util.Objects;

/**
 * Entity representing an order within a delivery.
 * This tracks individual order status within a batch delivery.
 */
public class DeliveryItem {
    private final OrderId orderId;
    private final Order order;
    private DeliveryItemStatus itemStatus;

    public DeliveryItem(OrderId orderId, Order order) {
        if (orderId == null) {
            throw new IllegalArgumentException("OrderId cannot be null");
        }
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.orderId = orderId;
        this.order = order;
        this.itemStatus = DeliveryItemStatus.READY;
    }

    /**
     * Get the order ID.
     */
    public OrderId getOrderId() {
        return orderId;
    }

    /**
     * Get the order entity.
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Get the item status.
     */
    public DeliveryItemStatus getItemStatus() {
        return itemStatus;
    }

    /**
     * Mark this item as picked up by rider.
     */
    public void markAsPickedUp() {
        if (!itemStatus.canMarkPickedUp()) {
            throw new IllegalStateException(
                "Cannot mark item as picked up. Current status: " + itemStatus
            );
        }
        this.itemStatus = DeliveryItemStatus.PICKED_UP;
    }

    /**
     * Mark this item as delivered to customer.
     */
    public void markAsDelivered() {
        if (!itemStatus.canMarkDelivered()) {
            throw new IllegalStateException(
                "Cannot mark item as delivered. Current status: " + itemStatus
            );
        }
        this.itemStatus = DeliveryItemStatus.DELIVERED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryItem that = (DeliveryItem) o;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "DeliveryItem{" +
                "orderId=" + orderId +
                ", itemStatus=" + itemStatus +
                '}';
    }

    /**
     * Factory method to create a DeliveryItem.
     */
    public static DeliveryItem of(OrderId orderId, Order order) {
        return new DeliveryItem(orderId, order);
    }
}
