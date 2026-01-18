package com.bluemountain.coffee.domain.event;

import com.bluemountain.coffee.domain.model.valobj.OrderId;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event published when coffee is ready for pickup/delivery.
 * 
 * DDD Concept: Domain events represent something that happened in the domain
 * that domain experts care about. Other parts of the system can react to
 * these events (e.g., send notifications to customers).
 * 
 * OOP Principles demonstrated:
 * - Inheritance: Extends Spring's ApplicationEvent
 * - Encapsulation: Contains all relevant event data
 * - Event-driven architecture: Decouples components
 */
public class CoffeeReadyEvent extends ApplicationEvent {
    private final OrderId orderId;
    private final String orderType;
    private final String customerName;
    
    /**
     * Constructor
     * 
     * @param source the object that published the event
     * @param orderId the ID of the order
     * @param orderType the type of order (DINE_IN or DELIVERY)
     * @param customerName the name of the customer
     */
    public CoffeeReadyEvent(Object source, OrderId orderId, String orderType, String customerName) {
        super(source);
        this.orderId = orderId;
        this.orderType = orderType;
        this.customerName = customerName;
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
    
    /**
     * Get the customer name
     * 
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }
}
