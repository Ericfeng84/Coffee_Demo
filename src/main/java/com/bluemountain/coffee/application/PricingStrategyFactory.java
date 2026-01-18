package com.bluemountain.coffee.application;

import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.service.PricingStrategy;
import com.bluemountain.coffee.domain.strategy.DeliveryPricingStrategy;
import com.bluemountain.coffee.domain.strategy.DineInPricingStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory for creating pricing strategies based on order type.
 * 
 * Design Pattern: Factory Pattern
 * Encapsulates the creation logic for pricing strategies, providing
 * a simple interface for obtaining the appropriate strategy.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Hides the complexity of strategy creation
 * - Dependency Injection: Receives strategies through constructor
 * - Single Responsibility: Only responsible for creating strategies
 * 
 * SOLID Principles:
 * - Single Responsibility: Only creates pricing strategies
 * - Open/Closed: New strategies can be added without modifying this class
 * - Dependency Inversion: Depends on abstractions (PricingStrategy interface)
 * 
 * Usage:
 * <pre>
 * PricingStrategy strategy = factory.getStrategy(OrderType.DINE_IN);
 * Money price = strategy.calculate(order);
 * </pre>
 */
@Component
public class PricingStrategyFactory {
    
    /**
     * Map of order types to their corresponding pricing strategies.
     * Using a Map for O(1) lookup time.
     */
    private final Map<OrderType, PricingStrategy> strategies;
    
    /**
     * Constructor with dependency injection.
     * Spring will automatically inject all PricingStrategy implementations.
     * 
     * @param dineInStrategy the dine-in pricing strategy
     * @param deliveryStrategy the delivery pricing strategy
     */
    public PricingStrategyFactory(
            DineInPricingStrategy dineInStrategy,
            DeliveryPricingStrategy deliveryStrategy) {
        this.strategies = Map.of(
            OrderType.DINE_IN, dineInStrategy,
            OrderType.DELIVERY, deliveryStrategy
        );
    }
    
    /**
     * Get the appropriate pricing strategy for the given order type.
     * 
     * @param type the order type
     * @return the corresponding pricing strategy
     * @throws IllegalArgumentException if no strategy exists for the given type
     */
    public PricingStrategy getStrategy(OrderType type) {
        PricingStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No pricing strategy found for order type: " + type);
        }
        return strategy;
    }
}
