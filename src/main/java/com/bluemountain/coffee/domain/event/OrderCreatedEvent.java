package com.bluemountain.coffee.domain.event;

import com.bluemountain.coffee.domain.model.valobj.OrderId;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when a new order is created.
 * 
 * DDD Concept: Domain events represent something that happened in the domain
 * that domain experts care about. Other parts of the system can react to
 * these events (e.g., send notifications, update analytics).
 * 
 * OOP Principles demonstrated:
 * - Inheritance: Extends Spring's ApplicationEvent
 * - Encapsulation: Contains all relevant event data
 * - Event-driven architecture: Decouples components
 */
public class OrderCreatedEvent extends ApplicationEvent {
    private final OrderId orderId;
    private final String orderType;
    
    /**
     * Constructor
     * 
     * @param source the object that published the event
     * @param orderId the ID of the created order
     * @param orderType the type of order (DINE_IN or DELIVERY)
     */
    public OrderCreatedEvent(Object source, OrderId orderId, String orderType) {
        super(source);
        this.orderId = orderId;
        this.orderType = orderType;
    }
    
    /**
     * Get the order ID
     * 
     * @return orderId
     */
    public OrderId getOrderId() {
        return orderId;
    }
    
    /**
     * Get the order type
     * 
     * @return orderType
     */
    public String getOrderType() {
        return orderType;
    }
}
