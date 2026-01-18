package com.bluemountain.coffee.application;

import com.bluemountain.coffee.domain.event.CoffeeReadyEvent;
import com.bluemountain.coffee.domain.event.OrderCreatedEvent;
import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.repository.OrderRepository;
import com.bluemountain.coffee.domain.service.DeliveryBatchService;
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
    private final DeliveryBatchService deliveryBatchService;
    private final OrderRepository orderRepository;
    
    /**
     * Constructor with dependency injection
     * 
     * @param notificationService notification service
     * @param deliveryBatchService delivery batching service
     * @param orderRepository order repository
     */
    public OrderEventListener(NotificationService notificationService,
                         DeliveryBatchService deliveryBatchService,
                         OrderRepository orderRepository) {
        this.notificationService = notificationService;
        this.deliveryBatchService = deliveryBatchService;
        this.orderRepository = orderRepository;
    }
    
    /**
     * Handle OrderCreatedEvent
     * 
     * @param event order created event
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
     * Triggers delivery batching for delivery orders
     * 
     * @param event coffee ready event
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
        
        // Check if this is a delivery order and trigger batching
        if (event.getOrderType().equals(OrderType.DELIVERY.name())) {
            logger.info("Delivery order ready, checking for batching opportunities");
            checkForBatching(event.getOrderId());
        }
        
        logger.info("CoffeeReadyEvent processed for order: {}", event.getOrderId());
    }
    
    /**
     * Check for batching opportunities when a delivery order is ready
     * 
     * @param orderId order ID that just became ready
     */
    private void checkForBatching(com.bluemountain.coffee.domain.model.valobj.OrderId orderId) {
        try {
            // Find batchable orders
            var batchableOrders = deliveryBatchService.findBatchableOrders();
            
            if (batchableOrders.isEmpty()) {
                logger.info("No batchable orders found");
                return;
            }
            
            // Check if current order is in batchable list
            boolean currentOrderIsBatchable = batchableOrders.stream()
                .anyMatch(order -> order.getId().equals(orderId));
            
            if (!currentOrderIsBatchable) {
                logger.info("Current order is not batchable (may already be in a delivery)");
                return;
            }
            
            // Find orders that can be batched with the current order
            var currentOrder = orderRepository.findById(orderId);
            if (currentOrder.isEmpty()) {
                logger.warn("Order not found: {}", orderId);
                return;
            }
            
            var batchableForCurrentOrder = deliveryBatchService.findBatchableOrdersFor(
                currentOrder.get(), 
                batchableOrders
            );
            
            // Try to create a batch if we have enough orders
            if (!batchableForCurrentOrder.isEmpty()) {
                logger.info("Found {} orders that can be batched together", 
                    batchableForCurrentOrder.size());
                
                // Add to current order to the batch
                var ordersToBatch = new java.util.ArrayList<>(batchableForCurrentOrder);
                ordersToBatch.add(currentOrder.get());
                
                // Limit batch size
                if (ordersToBatch.size() > 5) {
                    ordersToBatch = new java.util.ArrayList<>(ordersToBatch.subList(0, 5));
                }
                
                try {
                    var delivery = deliveryBatchService.createDeliveryBatch(ordersToBatch);
                    logger.info("Created delivery batch: {} with {} orders", 
                        delivery.getDeliveryId(), ordersToBatch.size());
                } catch (Exception e) {
                    logger.warn("Failed to create delivery batch: {}", e.getMessage());
                }
            } else {
                logger.info("No orders found that can be batched with current order");
            }
            
        } catch (Exception e) {
            logger.error("Error checking for batching: {}", e.getMessage(), e);
        }
    }
}
