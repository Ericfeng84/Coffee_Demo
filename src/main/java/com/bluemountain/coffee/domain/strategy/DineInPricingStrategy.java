package com.bluemountain.coffee.domain.strategy;

import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.aggregate.OrderItem;
import com.bluemountain.coffee.domain.model.valobj.Money;
import com.bluemountain.coffee.domain.service.PricingStrategy;
import org.springframework.stereotype.Component;

/**
 * Pricing strategy for dine-in orders.
 * 
 * Design Pattern: Strategy Pattern (Concrete Strategy)
 * Implements the PricingStrategy interface to provide dine-in specific pricing logic.
 * 
 * Dine-in orders have no additional fees - the customer pays only for the items ordered.
 * 
 * OOP Principles demonstrated:
 * - Implementation: Provides concrete implementation of PricingStrategy interface
 * - Single Responsibility: Only handles dine-in pricing logic
 * 
 * SOLID Principles:
 * - Single Responsibility: This class has one reason to change (dine-in pricing rules)
 * - Open/Closed: Can be extended without modifying existing code
 * - Liskov Substitution: Can be used wherever PricingStrategy is expected
 */
@Component
public class DineInPricingStrategy implements PricingStrategy {
    
    /**
     * Calculate the total price for a dine-in order.
     * For dine-in orders, we only sum the prices of all items.
     * No additional fees are applied.
     * 
     * @param order the order to calculate price for
     * @return the total price (sum of all item prices)
     */
    @Override
    public Money calculate(Order order) {
        // Sum up the total price of all items in the order
        return order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Money.zero(), Money::add);
    }
}
