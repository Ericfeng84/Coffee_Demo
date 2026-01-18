package com.bluemountain.coffee.application;

import com.bluemountain.coffee.domain.event.CoffeeReadyEvent;
import com.bluemountain.coffee.domain.event.OrderCreatedEvent;
import com.bluemountain.coffee.infrastructure.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event listener for domain events.
 * 
 * Design Pattern: Observer Pattern
 * Listens to domain events and reacts to them asynchronously.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Encapsulates event handling logic
 * - Single Responsibility: Only handles domain events
 * 
 * SOLID Principles:
 * - Single Responsibility: Only handles event processing
 * - Open/Closed: Can add new event handlers without modifying existing code
 * 
 * Benefits:
 * - Decouples event publishers from event consumers
 * - Allows multiple handlers for the same event
 * - Enables asynchronous processing
 */
@Component
public class OrderEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);
    
    private final NotificationService notificationService;
    
    /**
     * Constructor with dependency injection
     * 
     * @param notificationService the notification service
     */
    public OrderEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    /**
     * Handle OrderCreatedEvent
     * 
     * @param event the order created event
     */
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        logger.info("Handling OrderCreatedEvent for order: {}", event.getOrderId());
        
        // Send notification to customer
        notificationService.notifyOrderCreated(
                event.getOrderId(),
                "Customer", // In a real app, this would come from the order
                event.getOrderType()
        );
        
        logger.info("OrderCreatedEvent processed for order: {}", event.getOrderId());
    }
    
    /**
     * Handle CoffeeReadyEvent
     * 
     * @param event the coffee ready event
     */
    @EventListener
    public void handleCoffeeReady(CoffeeReadyEvent event) {
        logger.info("Handling CoffeeReadyEvent for order: {}", event.getOrderId());
        
        // Send notification to customer
        notificationService.notifyCoffeeReady(
                event.getOrderId(),
                event.getCustomerName(),
                event.getOrderType()
        );
        
        logger.info("CoffeeReadyEvent processed for order: {}", event.getOrderId());
    }
}
