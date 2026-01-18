package com.bluemountain.coffee.infrastructure.persistence;

import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.model.valobj.OrderId;
import com.bluemountain.coffee.domain.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of OrderRepository.
 * 
 * Design Pattern: Repository Pattern (Concrete Implementation)
 * Implements the OrderRepository interface using an in-memory storage.
 * 
 * Uses ConcurrentHashMap for thread-safe operations without explicit synchronization.
 * 
 * DDD Concept: Infrastructure layer implementation of domain repository interface.
 * 
 * OOP Principles demonstrated:
 * - Implementation: Provides concrete implementation of OrderRepository
 * - Thread Safety: Uses ConcurrentHashMap for concurrent access
 * 
 * SOLID Principles:
 * - Single Responsibility: Only handles in-memory storage
 * - Liskov Substitution: Can be used wherever OrderRepository is expected
 * 
 * Benefits:
 * - Simple and fast for development/testing
 * - No database setup required
 * - Thread-safe operations
 * 
 * Limitations:
 * - Data is lost when application restarts
 * - Not suitable for production
 */
@Repository
public class InMemoryOrderRepository implements OrderRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(InMemoryOrderRepository.class);
    
    /**
     * Thread-safe map to store orders by their ID
     */
    private final ConcurrentHashMap<OrderId, Order> orders = new ConcurrentHashMap<>();
    
    /**
     * Save an order (create or update)
     * 
     * @param order the order to save
     * @return the saved order
     */
    @Override
    public Order save(Order order) {
        logger.info("Saving order with ID: {}", order.getId());
        orders.put(order.getId(), order);
        return order;
    }
    
    /**
     * Find an order by its ID
     * 
     * @param orderId the order ID
     * @return Optional containing the order if found, empty otherwise
     */
    @Override
    public Optional<Order> findById(OrderId orderId) {
        logger.debug("Finding order by ID: {}", orderId);
        return Optional.ofNullable(orders.get(orderId));
    }
    
    /**
     * Find all orders
     * 
     * @return list of all orders
     */
    @Override
    public List<Order> findAll() {
        logger.debug("Finding all orders");
        return List.copyOf(orders.values());
    }
    
    /**
     * Find orders by status
     * 
     * @param status the order status
     * @return list of orders with the given status
     */
    @Override
    public List<Order> findByStatus(OrderStatus status) {
        logger.debug("Finding orders by status: {}", status);
        return orders.values().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Find orders by type
     * 
     * @param type the order type
     * @return list of orders with the given type
     */
    @Override
    public List<Order> findByType(OrderType type) {
        logger.debug("Finding orders by type: {}", type);
        return orders.values().stream()
                .filter(order -> order.getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Find orders created within a date range
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of orders created in the date range
     */
    @Override
    public List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Finding orders created between {} and {}", startDate, endDate);
        return orders.values().stream()
                .filter(order -> {
                    LocalDateTime createdAt = order.getCreatedAt();
                    return !createdAt.isBefore(startDate) && !createdAt.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Delete an order by its ID
     * 
     * @param orderId the order ID
     * @return true if the order was deleted, false if not found
     */
    @Override
    public boolean deleteById(OrderId orderId) {
        logger.info("Deleting order with ID: {}", orderId);
        return orders.remove(orderId) != null;
    }
    
    /**
     * Check if an order exists by its ID
     * 
     * @param orderId the order ID
     * @return true if the order exists, false otherwise
     */
    @Override
    public boolean existsById(OrderId orderId) {
        return orders.containsKey(orderId);
    }
}
