package com.bluemountain.coffee.domain.model.aggregate;

import com.bluemountain.coffee.domain.model.valobj.Money;
import java.util.Objects;

/**
 * Entity representing an item in an order.
 * 
 * DDD Concept: Entities have identity and lifecycle. OrderItems are
 * part of the Order aggregate and don't have their own repository.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Internal state protected
 * - Immutability: Once created, cannot be modified (create new instance for changes)
 */
public final class OrderItem {
    private final String productName;
    private final int quantity;
    private final Money unitPrice;
    private final Money totalPrice;
    
    /**
     * Private constructor to enforce use of factory method
     * 
     * @param productName the name of the product
     * @param quantity the quantity ordered
     * @param unitPrice the price per unit
     */
    private OrderItem(String productName, int quantity, Money unitPrice) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price cannot be null");
        }
        
        this.productName = productName.trim();
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(quantity);
    }
    
    /**
     * Factory method to create an OrderItem
     * 
     * @param productName the name of the product
     * @param quantity the quantity ordered
     * @param unitPrice the price per unit
     * @return new OrderItem instance
     */
    public static OrderItem of(String productName, int quantity, Money unitPrice) {
        return new OrderItem(productName, quantity, unitPrice);
    }
    
    /**
     * Get the product name
     * 
     * @return productName
     */
    public String getProductName() {
        return productName;
    }
    
    /**
     * Get the quantity
     * 
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Get the unit price
     * 
     * @return unitPrice
     */
    public Money getUnitPrice() {
        return unitPrice;
    }
    
    /**
     * Get the total price (quantity * unitPrice)
     * 
     * @return totalPrice
     */
    public Money getTotalPrice() {
        return totalPrice;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
               Objects.equals(productName, orderItem.productName) &&
               Objects.equals(unitPrice, orderItem.unitPrice);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productName, quantity, unitPrice);
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
