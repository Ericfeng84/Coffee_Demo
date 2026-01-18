package com.bluemountain.coffee.domain.model.valobj;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing monetary amounts.
 * 
 * DDD Concept: Value Objects are immutable objects that are defined
 * by their attributes rather than identity. Two Money objects with
 * the same amount are considered equal.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Internal representation hidden
 * - Immutability: Once created, cannot be modified
 * - Value equality: Based on value, not reference
 */
public final class Money {
    private final BigDecimal amount;
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    
    /**
     * Private constructor to enforce use of factory methods
     * 
     * @param amount the monetary amount
     */
    private Money(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount.setScale(SCALE, ROUNDING);
    }
    
    /**
     * Factory method to create Money from double
     * 
     * @param amount the monetary amount as double
     * @return new Money instance
     */
    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }
    
    /**
     * Factory method to create Money from BigDecimal
     * 
     * @param amount the monetary amount as BigDecimal
     * @return new Money instance
     */
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
    
    /**
     * Factory method to create zero Money
     * 
     * @return Money with zero amount
     */
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
    
    /**
     * Add another Money to this Money
     * 
     * @param other the Money to add
     * @return new Money instance with sum
     */
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }
    
    /**
     * Subtract another Money from this Money
     * 
     * @param other the Money to subtract
     * @return new Money instance with difference
     */
    public Money subtract(Money other) {
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Result cannot be negative");
        }
        return new Money(result);
    }
    
    /**
     * Multiply this Money by a factor
     * 
     * @param multiplier the multiplication factor
     * @return new Money instance with product
     */
    public Money multiply(double multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
    }
    
    /**
     * Get the amount as BigDecimal
     * 
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
    
    @Override
    public String toString() {
        return "Money{" + amount + "}";
    }
}
