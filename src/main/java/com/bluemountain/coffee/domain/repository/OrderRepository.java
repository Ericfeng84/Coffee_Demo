package com.bluemountain.coffee.domain.repository;

import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.model.valobj.OrderId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order aggregates.
 * 
 * Design Pattern: Repository Pattern
 * Provides an abstraction over data storage, allowing the domain layer
 * to remain independent of the persistence mechanism.
 * 
 * DDD Concept: Repositories are part of the domain layer but are implemented
 * in the infrastructure layer. They provide collection-like access to aggregates.
 * 
 * OOP Principles demonstrated:
 * - Abstraction: Hides data access implementation details
 * - Interface Segregation: Defines only the methods needed by the domain
 * - Dependency Inversion: High-level modules depend on this abstraction
 * 
 * SOLID Principles:
 * - Interface Segregation: Clients only depend on methods they use
 * - Dependency Inversion: Domain depends on abstractions, not concrete implementations
 * 
 * Benefits:
 * - Decouples domain logic from persistence logic
 * - Makes it easy to switch between different storage mechanisms
 * - Facilitates testing by allowing mock implementations
 */
public interface OrderRepository {
    
    /**
     * Save an order (create or update)
     * 
     * @param order the order to save
     * @return the saved order
     */
    Order save(Order order);
    
    /**
     * Find an order by its ID
     * 
     * @param orderId the order ID
     * @return Optional containing the order if found, empty otherwise
     */
    Optional<Order> findById(OrderId orderId);
    
    /**
     * Find all orders
     * 
     * @return list of all orders
     */
    List<Order> findAll();
    
    /**
     * Find orders by status
     * 
     * @param status the order status
     * @return list of orders with the given status
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * Find orders by type
     * 
     * @param type the order type
     * @return list of orders with the given type
     */
    List<Order> findByType(OrderType type);
    
    /**
     * Find orders created within a date range
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of orders created in the date range
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Delete an order by its ID
     * 
     * @param orderId the order ID
     * @return true if the order was deleted, false if not found
     */
    boolean deleteById(OrderId orderId);
    
    /**
     * Check if an order exists by its ID
     * 
     * @param orderId the order ID
     * @return true if the order exists, false otherwise
     */
    boolean existsById(OrderId orderId);
}
