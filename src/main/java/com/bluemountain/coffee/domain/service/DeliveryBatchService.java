package com.bluemountain.coffee.domain.service;

import com.bluemountain.coffee.domain.model.aggregate.Delivery;
import com.bluemountain.coffee.domain.model.aggregate.Order;

import java.util.List;

/**
 * Domain Service for batching orders into deliveries.
 * 
 * DDD Concept: Domain Service contains business logic that doesn't naturally
 * fit within an aggregate or value object.
 * 
 * Design Patterns:
 * - Strategy Pattern: Different batching strategies can be implemented
 * - Service Pattern: Provides domain-level operations
 */
public interface DeliveryBatchService {
    
    /**
     * Create a delivery batch from multiple orders
     * 
     * @param orders the orders to batch
     * @return the created delivery
     */
    Delivery createDeliveryBatch(List<Order> orders);
    
    /**
     * Find orders that are ready for batching
     * 
     * @return list of orders ready for batching
     */
    List<Order> findBatchableOrders();
    
    /**
     * Automatically batch orders based on business rules
     * 
     * @return list of created deliveries
     */
    List<Delivery> autoBatchOrders();
    
    /**
     * Check if an order can be batched with other orders
     * 
     * @param order the order to check
     * @param otherOrders the other orders to batch with
     * @return true if the order can be batched
     */
    boolean canBatchWith(Order order, List<Order> otherOrders);
    
    /**
     * Find orders that can be batched together based on proximity
     * 
     * @param referenceOrder the reference order
     * @param allOrders all available orders
     * @return list of orders that can be batched with the reference order
     */
    List<Order> findBatchableOrdersFor(Order referenceOrder, List<Order> allOrders);
}
