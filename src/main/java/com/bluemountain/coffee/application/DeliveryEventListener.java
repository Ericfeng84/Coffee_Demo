package com.bluemountain.coffee.application;

import com.bluemountain.coffee.domain.event.DeliveryAssignedEvent;
import com.bluemountain.coffee.domain.event.DeliveryCompletedEvent;
import com.bluemountain.coffee.domain.event.DeliveryCreatedEvent;
import com.bluemountain.coffee.domain.event.DeliveryDeliveredEvent;
import com.bluemountain.coffee.domain.event.DeliveryPickedUpEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event Listener for Delivery domain events.
 * 
 * DDD Concept: Event Listeners react to domain events and trigger
 * side effects such as notifications, logging, or integration with external systems.
 * 
 * Design Patterns:
 * - Observer Pattern: Listens to domain events
 * - Event-Driven Architecture: Decouples event producers from consumers
 */
@Component
public class DeliveryEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(DeliveryEventListener.class);
    
    /**
     * Handle DeliveryCreatedEvent
     * 
     * @param event the delivery created event
     */
    @EventListener
    public void onDeliveryCreated(DeliveryCreatedEvent event) {
        logger.info("Delivery created: {}", event.getDeliveryId());
        logger.info("Orders in delivery: {}", event.getOrderIds());
        
        // Print delivery slip
        logger.info("Printing delivery slip for delivery: {}", event.getDeliveryId());
    }
    
    /**
     * Handle DeliveryAssignedEvent
     * 
     * @param event the delivery assigned event
     */
    @EventListener
    public void onDeliveryAssigned(DeliveryAssignedEvent event) {
        logger.info("Delivery assigned to rider: {}", event.getDeliveryId());
        logger.info("Rider: {} ({})", 
            event.getRiderInfo().getRiderName(), 
            event.getRiderInfo().getRiderId());
    }
    
    /**
     * Handle DeliveryPickedUpEvent
     * 
     * @param event the delivery picked up event
     */
    @EventListener
    public void onDeliveryPickedUp(DeliveryPickedUpEvent event) {
        logger.info("Delivery picked up: {}", event.getDeliveryId());
        logger.info("Pickup time: {}", event.getPickedUpAt());
    }
    
    /**
     * Handle DeliveryDeliveredEvent
     * 
     * @param event the delivery delivered event
     */
    @EventListener
    public void onDeliveryDelivered(DeliveryDeliveredEvent event) {
        logger.info("Delivery delivered: {}", event.getDeliveryId());
        logger.info("Delivery time: {}", event.getDeliveredAt());
    }
    
    /**
     * Handle DeliveryCompletedEvent
     * 
     * @param event the delivery completed event
     */
    @EventListener
    public void onDeliveryCompleted(DeliveryCompletedEvent event) {
        logger.info("Delivery completed: {}", event.getDeliveryId());
        logger.info("Completion time: {}", event.getCompletedAt());
    }
}
