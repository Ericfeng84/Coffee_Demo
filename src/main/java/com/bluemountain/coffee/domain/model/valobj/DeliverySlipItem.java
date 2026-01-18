package com.bluemountain.coffee.domain.model.valobj;

import com.bluemountain.coffee.domain.model.aggregate.DeliveryItem;
import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.aggregate.OrderItem;
import com.bluemountain.coffee.domain.model.enums.DeliveryItemStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Value Object representing an item on a delivery slip.
 * This is an immutable value object derived from DeliveryItem.
 * 
 * DDD Concept: Value Objects are immutable objects that are defined
 * by their attributes rather than identity.
 */
public class DeliverySlipItem {
    private final OrderId orderId;
    private final String customerName;
    private final Address deliveryAddress;
    private final List<String> productNames;
    private final int itemCount;
    private final DeliveryItemStatus itemStatus;

    private DeliverySlipItem(OrderId orderId, String customerName, Address deliveryAddress,
                             List<String> productNames, int itemCount, DeliveryItemStatus itemStatus) {
        this.orderId = Objects.requireNonNull(orderId, "Order ID cannot be null");
        this.customerName = Objects.requireNonNull(customerName, "Customer name cannot be null");
        this.deliveryAddress = deliveryAddress;
        this.productNames = List.copyOf(Objects.requireNonNull(productNames, "Product names cannot be null"));
        this.itemCount = itemCount;
        this.itemStatus = Objects.requireNonNull(itemStatus, "Item status cannot be null");
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public int getItemCount() {
        return itemCount;
    }

    public DeliveryItemStatus getItemStatus() {
        return itemStatus;
    }

    /**
     * Create a DeliverySlipItem from a DeliveryItem
     */
    public static DeliverySlipItem fromDeliveryItem(DeliveryItem deliveryItem) {
        Objects.requireNonNull(deliveryItem, "DeliveryItem cannot be null");

        Order order = deliveryItem.getOrder();
        List<OrderItem> orderItems = order.getItems();

        List<String> productNames = orderItems.stream()
            .map(item -> item.getProductName())
            .collect(Collectors.toList());

        int itemCount = orderItems.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();

        return new DeliverySlipItem(
            deliveryItem.getOrderId(),
            order.getCustomerName(),
            order.getAddress(),
            productNames,
            itemCount,
            deliveryItem.getItemStatus()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliverySlipItem that = (DeliverySlipItem) o;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "DeliverySlipItem{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", itemCount=" + itemCount +
                ", itemStatus=" + itemStatus +
                '}';
    }
}
