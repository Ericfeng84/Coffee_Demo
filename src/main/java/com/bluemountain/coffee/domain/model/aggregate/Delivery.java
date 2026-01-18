package com.bluemountain.coffee.domain.model.aggregate;

import com.bluemountain.coffee.domain.event.*;
import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import com.bluemountain.coffee.domain.model.valobj.OrderId;
import com.bluemountain.coffee.domain.model.valobj.RiderInfo;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Aggregate Root representing a delivery batch containing one or more orders.
 * 
 * DDD Concept: Aggregate Root is the entry point to an aggregate.
 * All access to entities within the aggregate must go through the root.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Protects internal state and enforces invariants
 * - State Pattern: Manages delivery state transitions
 * - Factory Pattern: Provides factory method for complex object creation
 * 
 * SOLID Principles:
 * - Single Responsibility: Manages delivery lifecycle and invariants
 * - Open/Closed: Open for extension (new behaviors), closed for modification
 * 
 * Design Patterns Used:
 * - State Pattern: DeliveryStatus enum with state transition logic
 * - Factory Pattern: create() factory method
 * - Domain Events: Publishes events for important state changes
 */
public class Delivery {
    
    private final DeliveryId deliveryId;
    private final List<DeliveryItem> items;
    private RiderInfo riderInfo;
    private DeliveryStatus status;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * Private constructor to enforce use of factory method
     * 
     * @param deliveryId the delivery ID
     * @param items the list of delivery items
     * @param createdAt the creation timestamp
     * @param eventPublisher the event publisher for domain events
     */
    private Delivery(DeliveryId deliveryId, List<DeliveryItem> items, 
                     LocalDateTime createdAt, ApplicationEventPublisher eventPublisher) {
        this.deliveryId = Objects.requireNonNull(deliveryId, "Delivery ID cannot be null");
        this.items = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(items, "Items cannot be null")));
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = createdAt;
        this.status = DeliveryStatus.CREATED;
        this.eventPublisher = eventPublisher;
        
        // Validate that delivery has at least one item
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Delivery must have at least one item");
        }
        
        // Validate that all orders are in READY state
        for (DeliveryItem item : items) {
            if (item.getOrder().getStatus() != OrderStatus.READY) {
                throw new IllegalArgumentException(
                    "All orders must be in READY state. Order " + item.getOrderId() + " is in " + 
                    item.getOrder().getStatus() + " state"
                );
            }
        }
    }
    
    /**
     * Factory method to create a new delivery
     * 
     * Design Pattern: Factory Method
     * Encapsulates complex object creation logic
     * 
     * @param orders the list of orders to include in this delivery
     * @return new Delivery instance
     */
    public static Delivery create(List<Order> orders) {
        List<DeliveryItem> items = orders.stream()
            .map(order -> DeliveryItem.of(order.getId(), order))
            .collect(Collectors.toList());
        
        Delivery delivery = new Delivery(
            DeliveryId.generate(),
            items,
            LocalDateTime.now(),
            null
        );
        
        return delivery;
    }
    
    /**
     * Factory method to create a new delivery with event publisher
     * 
     * @param orders the list of orders to include in this delivery
     * @param eventPublisher the event publisher
     * @return new Delivery instance
     */
    public static Delivery create(List<Order> orders, ApplicationEventPublisher eventPublisher) {
        List<DeliveryItem> items = orders.stream()
            .map(order -> DeliveryItem.of(order.getId(), order))
            .collect(Collectors.toList());
        
        Delivery delivery = new Delivery(
            DeliveryId.generate(),
            items,
            LocalDateTime.now(),
            eventPublisher
        );
        
        // Publish domain event
        List<String> orderIds = items.stream()
            .map(item -> item.getOrderId().getValue().toString())
            .collect(Collectors.toList());
        delivery.eventPublisher.publishEvent(new DeliveryCreatedEvent(
            delivery,
            delivery.deliveryId,
            orderIds
        ));
        
        return delivery;
    }
    
    /**
     * Assign a rider to this delivery
     * 
     * @param riderInfo the rider information
     */
    public void assignRider(RiderInfo riderInfo) {
        Objects.requireNonNull(riderInfo, "Rider info cannot be null");
        
        // Validate state transition
        if (!status.canAssignRider()) {
            throw new IllegalStateException(
                "Cannot assign rider. Current status: " + status
            );
        }
        
        this.riderInfo = riderInfo;
        this.status = DeliveryStatus.ASSIGNED;
        this.updatedAt = LocalDateTime.now();
        
        // Publish domain event
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new DeliveryAssignedEvent(
                this,
                this.deliveryId,
                riderInfo
            ));
        }
    }
    
    /**
     * Mark as picked up by rider
     */
    public void markAsPickedUp() {
        // Validate state transition
        if (!status.canMarkPickedUp()) {
            throw new IllegalStateException(
                "Cannot mark as picked up. Current status: " + status
            );
        }
        
        this.status = DeliveryStatus.PICKED_UP;
        this.pickupTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Mark all items as picked up
        for (DeliveryItem item : items) {
            item.markAsPickedUp();
        }
        
        // Publish domain event
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new DeliveryPickedUpEvent(
                this,
                this.deliveryId
            ));
        }
    }
    
    /**
     * Mark as in transit
     */
    public void markAsInTransit() {
        // Validate state transition
        if (!status.canMarkInTransit()) {
            throw new IllegalStateException(
                "Cannot mark as in transit. Current status: " + status
            );
        }
        
        this.status = DeliveryStatus.IN_TRANSIT;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Mark as delivered to customer
     */
    public void markAsDelivered() {
        // Validate state transition
        if (!status.canMarkDelivered()) {
            throw new IllegalStateException(
                "Cannot mark as delivered. Current status: " + status
            );
        }
        
        this.status = DeliveryStatus.DELIVERED;
        this.deliveryTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Mark all items as delivered
        for (DeliveryItem item : items) {
            item.markAsDelivered();
        }
        
        // Publish domain event
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new DeliveryDeliveredEvent(
                this,
                this.deliveryId
            ));
        }
    }
    
    /**
     * Complete the delivery
     */
    public void complete() {
        // Validate state transition
        if (!status.canComplete()) {
            throw new IllegalStateException(
                "Cannot complete delivery. Current status: " + status
            );
        }
        
        this.status = DeliveryStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
        
        // Publish domain event
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new DeliveryCompletedEvent(
                this,
                this.deliveryId
            ));
        }
    }
    
    /**
     * Cancel the delivery (before pickup)
     */
    public void cancel() {
        // Validate state transition
        if (!status.canCancel()) {
            throw new IllegalStateException(
                "Cannot cancel delivery. Current status: " + status
            );
        }
        
        this.status = DeliveryStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    
    public DeliveryId getDeliveryId() {
        return deliveryId;
    }
    
    public List<DeliveryItem> getItems() {
        return items;
    }
    
    public RiderInfo getRiderInfo() {
        return riderInfo;
    }
    
    public DeliveryStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getPickupTime() {
        return pickupTime;
    }
    
    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Get the list of orders in this delivery
     */
    public List<Order> getOrders() {
        return items.stream()
            .map(DeliveryItem::getOrder)
            .collect(Collectors.toList());
    }
    
    /**
     * Get the list of order IDs in this delivery
     */
    public List<OrderId> getOrderIds() {
        return items.stream()
            .map(DeliveryItem::getOrderId)
            .collect(Collectors.toList());
    }
    
    /**
     * Check if delivery is active (not completed or cancelled)
     */
    public boolean isActive() {
        return status.isActive();
    }
    
    /**
     * Check if delivery is in a terminal state
     */
    public boolean isTerminal() {
        return status.isTerminal();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(deliveryId, delivery.deliveryId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(deliveryId);
    }
    
    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId=" + deliveryId +
                ", itemCount=" + items.size() +
                ", riderInfo=" + riderInfo +
                ", status=" + status +
                ", pickupTime=" + pickupTime +
                ", deliveryTime=" + deliveryTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
