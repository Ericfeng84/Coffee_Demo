package com.bluemountain.coffee.domain.model.enums;

/**
 * Enum representing the status of an individual item within a delivery.
 * This tracks the status of each order in a batch delivery.
 */
public enum DeliveryItemStatus {
    /**
     * Order ready for delivery
     */
    READY,
    
    /**
     * Item picked up by rider
     */
    PICKED_UP,
    
    /**
     * Item delivered to customer
     */
    DELIVERED;

    /**
     * Check if a transition from current status to target status is valid.
     */
    public boolean canTransitionTo(DeliveryItemStatus targetStatus) {
        switch (this) {
            case READY:
                return targetStatus == PICKED_UP;
            case PICKED_UP:
                return targetStatus == DELIVERED;
            case DELIVERED:
                return false; // Terminal state
            default:
                return false;
        }
    }

    /**
     * Check if this status is a terminal state (no further transitions possible).
     */
    public boolean isTerminal() {
        return this == DELIVERED;
    }

    /**
     * Check if this status allows marking as picked up.
     */
    public boolean canMarkPickedUp() {
        return this == READY;
    }

    /**
     * Check if this status allows marking as delivered.
     */
    public boolean canMarkDelivered() {
        return this == PICKED_UP;
    }
}
