package com.bluemountain.coffee.domain.model.enums;

/**
 * Enumeration representing the type of order.
 * 
 * This enum demonstrates the use of enumerations for type safety
 * and to represent a fixed set of values in the domain.
 * 
 * OOP Principle: Encapsulation - related constants grouped together
 */
public enum OrderType {
    /**
     * Dine-in order - customer eats at the coffee shop
     */
    DINE_IN,
    
    /**
     * Delivery order - coffee is delivered to customer's address
     */
    DELIVERY
}
