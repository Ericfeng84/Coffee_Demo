package com.bluemountain.coffee.interfaces.web;

import com.bluemountain.coffee.application.OrderAppService;
import com.bluemountain.coffee.interfaces.dto.CreateOrderCommand;
import com.bluemountain.coffee.interfaces.dto.OrderDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for order management.
 * 
 * Design Pattern: Controller Pattern (MVC)
 * Handles HTTP requests and responses for order operations.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Encapsulates HTTP handling logic
 * - Single Responsibility: Only handles HTTP requests/responses
 * 
 * SOLID Principles:
 * - Single Responsibility: Only handles HTTP endpoints
 * - Open/Closed: Can add new endpoints without modifying existing ones
 * - Dependency Inversion: Depends on application service abstraction
 * 
 * REST API Endpoints:
 * - POST /api/orders - Create a new order
 * - GET /api/orders/{id} - Get order by ID
 * - GET /api/orders - Get all orders
 * - GET /api/orders/status/{status} - Get orders by status
 * - PUT /api/orders/{id}/status - Update order status
 * - PUT /api/orders/{id}/ready - Mark coffee as ready
 * - PUT /api/orders/{id}/complete - Complete order
 * - DELETE /api/orders/{id} - Cancel order
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    private final OrderAppService orderAppService;
    
    /**
     * Constructor with dependency injection
     * 
     * @param orderAppService the order application service
     */
    public OrderController(OrderAppService orderAppService) {
        this.orderAppService = orderAppService;
    }
    
    /**
     * Create a new order
     * 
     * POST /api/orders
     * 
     * @param command the create order command
     * @return the created order DTO with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        logger.info("Creating order for customer: {}", command.getCustomerName());
        
        OrderDTO order = orderAppService.placeOrder(command);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    /**
     * Get an order by ID
     * 
     * GET /api/orders/{id}
     * 
     * @param id the order ID
     * @return the order DTO with HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String id) {
        logger.debug("Getting order with ID: {}", id);
        
        OrderDTO order = orderAppService.getOrder(id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * Get all orders
     * 
     * GET /api/orders
     * 
     * @return list of all order DTOs with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        logger.debug("Getting all orders");
        
        List<OrderDTO> orders = orderAppService.getAllOrders();
        
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get orders by status
     * 
     * GET /api/orders/status/{status}
     * 
     * @param status the order status
     * @return list of order DTOs with the given status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        logger.debug("Getting orders by status: {}", status);
        
        List<OrderDTO> orders = orderAppService.getOrdersByStatus(status);
        
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Update order status
     * 
     * PUT /api/orders/{id}/status
     * 
     * @param id the order ID
     * @param status the new status
     * @return the updated order DTO with HTTP 200 status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable String id,
            @RequestParam String status) {
        logger.info("Updating order {} status to: {}", id, status);
        
        OrderDTO order = orderAppService.updateOrderStatus(id, status);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * Mark coffee as ready for pickup/delivery
     * 
     * PUT /api/orders/{id}/ready
     * 
     * @param id the order ID
     * @return the updated order DTO with HTTP 200 status
     */
    @PutMapping("/{id}/ready")
    public ResponseEntity<OrderDTO> markCoffeeReady(@PathVariable String id) {
        logger.info("Marking coffee as ready for order: {}", id);
        
        OrderDTO order = orderAppService.markCoffeeReady(id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * Complete an order
     * 
     * PUT /api/orders/{id}/complete
     * 
     * @param id the order ID
     * @return the updated order DTO with HTTP 200 status
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<OrderDTO> completeOrder(@PathVariable String id) {
        logger.info("Completing order: {}", id);
        
        OrderDTO order = orderAppService.completeOrder(id);
        
        return ResponseEntity.ok(order);
    }
    
    /**
     * Cancel an order
     * 
     * DELETE /api/orders/{id}
     * 
     * @param id the order ID
     * @return the updated order DTO with HTTP 200 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable String id) {
        logger.info("Cancelling order: {}", id);
        
        OrderDTO order = orderAppService.cancelOrder(id);
        
        return ResponseEntity.ok(order);
    }
}
