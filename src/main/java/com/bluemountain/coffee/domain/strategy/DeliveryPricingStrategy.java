package com.bluemountain.coffee.domain.strategy;

import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.aggregate.OrderItem;
import com.bluemountain.coffee.domain.model.valobj.Money;
import com.bluemountain.coffee.domain.service.PricingStrategy;
import org.springframework.stereotype.Component;

/**
 * Pricing strategy for delivery orders.
 * 
 * Design Pattern: Strategy Pattern (Concrete Strategy)
 * Implements the PricingStrategy interface to provide delivery-specific pricing logic.
 * 
 * Delivery orders include:
 * - Packaging fee: $2.00 for packaging materials
 * - Delivery fee: $5.00 for delivery service
 * - Item prices: Sum of all ordered items
 * 
 * OOP Principles demonstrated:
 * - Implementation: Provides concrete implementation of PricingStrategy interface
 * - Single Responsibility: Only handles delivery pricing logic
 * 
 * SOLID Principles:
 * - Single Responsibility: This class has one reason to change (delivery pricing rules)
 * - Open/Closed: Can be extended without modifying existing code
 * - Liskov Substitution: Can be used wherever PricingStrategy is expected
 */
@Component
public class DeliveryPricingStrategy implements PricingStrategy {
    
    /**
     * Fixed packaging fee for delivery orders
     */
    private static final Money PACKAGING_FEE = Money.of(2.0);
    
    /**
     * Fixed delivery fee for delivery orders
     */
    private static final Money DELIVERY_FEE = Money.of(5.0);
    
    /**
     * Calculate the total price for a delivery order.
     * For delivery orders, we sum the prices of all items and add
     * packaging and delivery fees.
     * 
     * Formula: Total = (Sum of item prices) + Packaging Fee + Delivery Fee
     * 
     * @param order the order to calculate price for
     * @return the total price including all fees
     */
    @Override
    public Money calculate(Order order) {
        // Sum up the total price of all items in the order
        Money itemsTotal = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Money.zero(), Money::add);
        
        // Add packaging and delivery fees
        Money total = itemsTotal.add(PACKAGING_FEE).add(DELIVERY_FEE);
        
        return total;
    }
}
