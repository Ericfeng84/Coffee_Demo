package com.bluemountain.coffee.interfaces.platform;

import com.bluemountain.coffee.application.OrderAppService;
import com.bluemountain.coffee.interfaces.dto.CreateOrderCommand;
import com.bluemountain.coffee.interfaces.dto.OrderDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for external delivery platform callbacks.
 * 
 * Design Pattern: Controller Pattern (MVC)
 * Handles HTTP requests from external delivery platforms (e.g., Uber Eats, DoorDash).
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Encapsulates platform-specific HTTP handling logic
 * - Single Responsibility: Only handles platform callbacks
 * 
 * SOLID Principles:
 * - Single Responsibility: Only handles platform integration
 * - Open/Closed: Can add new platform endpoints without modifying existing ones
 * - Dependency Inversion: Depends on application service abstraction
 * 
 * REST API Endpoints:
 * - POST /api/platform/orders - Create order from platform
 * - POST /api/platform/orders/{id}/ready - Notify platform that order is ready
 * - POST /api/platform/orders/{id}/complete - Notify platform that order is completed
 */
@RestController
@RequestMapping("/api/platform")
public class PlatformController {
    
    private static final Logger logger = LoggerFactory.getLogger(PlatformController.class);
    
    private final OrderAppService orderAppService;
    
    /**
     * Constructor with dependency injection
     * 
     * @param orderAppService the order application service
     */
    public PlatformController(OrderAppService orderAppService) {
        this.orderAppService = orderAppService;
    }
    
    /**
     * Create order from external platform
     * 
     * POST /api/platform/orders
     * 
     * This endpoint is called by external delivery platforms when a customer
     * places an order through their app.
     * 
     * @param command the create order command from the platform
     * @return the created order DTO with HTTP 201 status
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createPlatformOrder(@Valid @RequestBody CreateOrderCommand command) {
        logger.info("Received order from platform for customer: {}", command.getCustomerName());
        
        OrderDTO order = orderAppService.placeOrder(command);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    /**
     * Notify platform that coffee is ready
     * 
     * POST /api/platform/orders/{id}/ready
     * 
     * This endpoint can be called internally to notify the external platform
     * that the order is ready for pickup.
     * 
     * @param id the order ID
     * @return the updated order DTO with HTTP 200 status
     */
    @PostMapping("/orders/{id}/ready")
    public ResponseEntity<OrderDTO> notifyReady(@PathVariable String id) {
        logger.info("Notifying platform that order {} is ready", id);
        
        OrderDTO order = orderAppService.markCoffeeReady(id);
        
        // In a real application, this would also send a callback to the platform
        logger.info("Platform notified that order {} is ready", id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * Notify platform that order is completed
     * 
     * POST /api/platform/orders/{id}/complete
     * 
     * This endpoint can be called internally to notify the external platform
     * that the order has been completed.
     * 
     * @param id the order ID
     * @return the updated order DTO with HTTP 200 status
     */
    @PostMapping("/orders/{id}/complete")
    public ResponseEntity<OrderDTO> notifyComplete(@PathVariable String id) {
        logger.info("Notifying platform that order {} is completed", id);
        
        OrderDTO order = orderAppService.completeOrder(id);
        
        // In a real application, this would also send a callback to the platform
        logger.info("Platform notified that order {} is completed", id);
        
        return ResponseEntity.ok(order);
    }
}
