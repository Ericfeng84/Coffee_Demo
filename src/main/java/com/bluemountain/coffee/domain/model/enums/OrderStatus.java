package com.bluemountain.coffee.domain.model.enums;

/**
 * Enumeration representing the status of an order.
 * 
 * This enum demonstrates the State Pattern concept where orders
 * transition through different states during their lifecycle.
 * 
 * Valid state transitions:
 * CREATED -> SETTLED -> PREPARING -> READY -> COMPLETED
 * Any state -> CANCELLED (with certain restrictions)
 * 
 * OOP Principle: State Pattern - encapsulating state-specific behavior
 */
public enum OrderStatus {
    /**
     * Order has been created but not yet paid
     */
    CREATED,
    
    /**
     * Order has been paid/settled
     */
    SETTLED,
    
    /**
     * Coffee is being prepared
     */
    PREPARING,
    
    /**
     * Coffee is ready for pickup/delivery
     */
    READY,
    
    /**
     * Order has been completed
     */
    COMPLETED,
    
    /**
     * Order has been cancelled
     */
    CANCELLED
}
