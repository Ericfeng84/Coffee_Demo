package com.bluemountain.coffee.interfaces.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for order items.
 * 
 * DTO Pattern: Used to transfer data between layers without exposing
 * domain model details to the interface layer.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Groups related data together
 * - Data Transfer: Lightweight object for data transfer
 * 
 * Benefits:
 * - Decouples interface layer from domain model
 * - Allows customizing data representation for API responses
 * - Prevents over-fetching or under-fetching of data
 */
public class OrderItemDTO {
    
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    
    /**
     * Default constructor for JSON deserialization
     */
    public OrderItemDTO() {
    }
    
    /**
     * Constructor with all fields
     * 
     * @param productName the name of the product
     * @param quantity the quantity ordered
     * @param unitPrice the price per unit
     * @param totalPrice the total price (quantity * unitPrice)
     */
    public OrderItemDTO(String productName, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
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
     * Set the product name
     * 
     * @param productName the product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
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
     * Set the quantity
     * 
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    /**
     * Get the unit price
     * 
     * @return unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    /**
     * Set the unit price
     * 
     * @param unitPrice the unit price
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    /**
     * Get the total price
     * 
     * @return totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * Set the total price
     * 
     * @param totalPrice the total price
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
