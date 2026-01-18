package com.bluemountain.coffee.domain.model.valobj;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing a unique order identifier.
 * 
 * DDD Concept: Value Objects are immutable objects that are defined
 * by their attributes rather than identity. Two OrderId objects with
 * the same UUID are considered equal.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Internal representation hidden
 * - Immutability: Once created, cannot be modified
 * - Value equality: Based on value, not reference
 * - Type safety: Prevents mixing IDs of different types
 */
public final class OrderId {
    private final UUID value;
    
    /**
     * Private constructor to enforce use of factory methods
     * 
     * @param value the UUID value
     */
    private OrderId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Order ID value cannot be null");
        }
        this.value = value;
    }
    
    /**
     * Factory method to create a new OrderId with a random UUID
     * 
     * @return new OrderId instance
     */
    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
    
    /**
     * Factory method to create an OrderId from an existing UUID
     * 
     * @param uuid the UUID value
     * @return new OrderId instance
     */
    public static OrderId of(UUID uuid) {
        return new OrderId(uuid);
    }
    
    /**
     * Factory method to create an OrderId from a string representation
     * 
     * @param uuidString the UUID as string
     * @return new OrderId instance
     */
    public static OrderId of(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID string cannot be null or empty");
        }
        try {
            return new OrderId(UUID.fromString(uuidString.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID string: " + uuidString, e);
        }
    }
    
    /**
     * Get the UUID value
     * 
     * @return the UUID
     */
    public UUID getValue() {
        return value;
    }
    
    /**
     * Get the string representation of the UUID
     * 
     * @return UUID as string
     */
    public String toString() {
        return value.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return Objects.equals(value, orderId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
