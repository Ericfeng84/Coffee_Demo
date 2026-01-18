package com.bluemountain.coffee.domain.model.enums;

/**
 * Enum representing the status of a delivery.
 * 
 * State transitions:
 * CREATED → ASSIGNED → PICKED_UP → IN_TRANSIT → DELIVERED → COMPLETED
 *     ↓
 * CANCELLED (only from CREATED or ASSIGNED)
 */
public enum DeliveryStatus {
    /**
     * Delivery created, waiting for rider assignment
     */
    CREATED,
    
    /**
     * Rider assigned, waiting for pickup
     */
    ASSIGNED,
    
    /**
     * Rider picked up orders
     */
    PICKED_UP,
    
    /**
     * Rider is on the way to delivery location
     */
    IN_TRANSIT,
    
    /**
     * Orders delivered to customer
     */
    DELIVERED,
    
    /**
     * Delivery completed
     */
    COMPLETED,
    
    /**
     * Delivery cancelled
     */
    CANCELLED;

    /**
     * Check if a transition from current status to target status is valid.
     */
    public boolean canTransitionTo(DeliveryStatus targetStatus) {
        switch (this) {
            case CREATED:
                return targetStatus == ASSIGNED || targetStatus == CANCELLED;
            case ASSIGNED:
                return targetStatus == PICKED_UP || targetStatus == CANCELLED;
            case PICKED_UP:
                return targetStatus == IN_TRANSIT;
            case IN_TRANSIT:
                return targetStatus == DELIVERED;
            case DELIVERED:
                return targetStatus == COMPLETED;
            case COMPLETED:
                return false; // Terminal state
            case CANCELLED:
                return false; // Terminal state
            default:
                return false;
        }
    }

    /**
     * Check if this status is a terminal state (no further transitions possible).
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }

    /**
     * Check if this status is an active state (delivery is in progress).
     */
    public boolean isActive() {
        return this == ASSIGNED || this == PICKED_UP || this == IN_TRANSIT;
    }

    /**
     * Check if this status allows rider assignment.
     */
    public boolean canAssignRider() {
        return this == CREATED;
    }

    /**
     * Check if this status allows cancellation.
     */
    public boolean canCancel() {
        return this == CREATED || this == ASSIGNED;
    }

    /**
     * Check if this status allows marking as picked up.
     */
    public boolean canMarkPickedUp() {
        return this == ASSIGNED;
    }

    /**
     * Check if this status allows marking as in transit.
     */
    public boolean canMarkInTransit() {
        return this == PICKED_UP;
    }

    /**
     * Check if this status allows marking as delivered.
     */
    public boolean canMarkDelivered() {
        return this == IN_TRANSIT;
    }

    /**
     * Check if this status allows completion.
     */
    public boolean canComplete() {
        return this == DELIVERED;
    }
}
