package com.bluemountain.coffee.domain.service;

import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.valobj.Money;

/**
 * Strategy interface for calculating order prices.
 * 
 * Design Pattern: Strategy Pattern
 * Defines a family of algorithms (pricing strategies), encapsulates each one,
 * and makes them interchangeable. This lets the algorithm vary independently
 * from clients that use it.
 * 
 * OOP Principles demonstrated:
 * - Abstraction: Defines contract for pricing calculations
 * - Polymorphism: Multiple implementations can be used interchangeably
 * - Open/Closed Principle: Open for extension (new strategies), closed for modification
 * - Dependency Inversion: High-level modules depend on abstractions, not concrete implementations
 * 
 * Use Cases:
 * - DineInPricingStrategy: Calculates price for dine-in orders (no additional fees)
 * - DeliveryPricingStrategy: Calculates price for delivery orders (includes packaging and delivery fees)
 */
public interface PricingStrategy {
    
    /**
     * Calculate the total price for an order
     * 
     * @param order the order to calculate price for
     * @return the total price including any applicable fees
     */
    Money calculate(Order order);
}
