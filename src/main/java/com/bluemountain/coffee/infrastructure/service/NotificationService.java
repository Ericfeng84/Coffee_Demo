package com.bluemountain.coffee.infrastructure.service;

import com.bluemountain.coffee.domain.model.valobj.OrderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Mock notification service for sending notifications to customers.
 * 
 * This is a simplified implementation for educational purposes.
 * In a real application, this would integrate with email services,
 * SMS providers, or push notification services.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Hides notification complexity
 * - Single Responsibility: Only handles notification operations
 * 
 * SOLID Principles:
 * - Single Responsibility: Only sends notifications
 * - Interface Segregation: Provides only notification-related methods
 */
@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    /**
     * Send notification when order is created
     * 
     * @param orderId the ID of the order
     * @param customerName the name of the customer
     * @param orderType the type of order (DINE_IN or DELIVERY)
     */
    public void notifyOrderCreated(OrderId orderId, String customerName, String orderType) {
        logger.info("Sending order created notification to {} for order {} ({})", 
                customerName, orderId, orderType);
        
        // In a real application, this would:
        // 1. Format the notification message
        // 2. Send via email, SMS, or push notification
        // 3. Handle delivery failures and retries
        
        logger.info("Order created notification sent successfully");
    }
    
    /**
     * Send notification when coffee is ready
     * 
     * @param orderId the ID of the order
     * @param customerName the name of the customer
     * @param orderType the type of order (DINE_IN or DELIVERY)
     */
    public void notifyCoffeeReady(OrderId orderId, String customerName, String orderType) {
        logger.info("Sending coffee ready notification to {} for order {} ({})", 
                customerName, orderId, orderType);
        
        // In a real application, this would send the notification
        // through the appropriate channel
        
        logger.info("Coffee ready notification sent successfully");
    }
    
    /**
     * Send notification when order is completed
     * 
     * @param orderId the ID of the order
     * @param customerName the name of the customer
     */
    public void notifyOrderCompleted(OrderId orderId, String customerName) {
        logger.info("Sending order completed notification to {} for order {}", 
                customerName, orderId);
        
        logger.info("Order completed notification sent successfully");
    }
}
