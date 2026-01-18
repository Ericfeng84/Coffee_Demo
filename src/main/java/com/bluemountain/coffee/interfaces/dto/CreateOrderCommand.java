package com.bluemountain.coffee.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Command DTO for creating a new order.
 * 
 * Command Pattern: Encapsulates a request as an object, allowing
 * parameterization of clients with different requests.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Groups all data needed to create an order
 * - Validation: Uses Bean Validation annotations for input validation
 * 
 * SOLID Principles:
 * - Single Responsibility: Only represents order creation data
 * - Interface Segregation: Minimal interface for order creation
 * 
 * Benefits:
 * - Clear contract for order creation
 * - Automatic validation through Bean Validation
 * - Type-safe data transfer
 */
public class CreateOrderCommand {
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @NotNull(message = "Order type is required")
    private String orderType;
    
    @NotEmpty(message = "At least one item is required")
    private List<OrderItemDTO> items;
    
    private String street;
    private String city;
    private String postalCode;
    private String country;
    
    /**
     * Default constructor for JSON deserialization
     */
    public CreateOrderCommand() {
    }
    
    /**
     * Get the customer name
     * 
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * Set the customer name
     * 
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    /**
     * Get the order type (DINE_IN or DELIVERY)
     * 
     * @return orderType
     */
    public String getOrderType() {
        return orderType;
    }
    
    /**
     * Set the order type
     * 
     * @param orderType the order type
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    
    /**
     * Get the list of order items
     * 
     * @return items
     */
    public List<OrderItemDTO> getItems() {
        return items;
    }
    
    /**
     * Set the list of order items
     * 
     * @param items the list of order items
     */
    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
    
    /**
     * Get the street address (required for delivery orders)
     * 
     * @return street
     */
    public String getStreet() {
        return street;
    }
    
    /**
     * Set the street address
     * 
     * @param street the street address
     */
    public void setStreet(String street) {
        this.street = street;
    }
    
    /**
     * Get the city (required for delivery orders)
     * 
     * @return city
     */
    public String getCity() {
        return city;
    }
    
    /**
     * Set the city
     * 
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /**
     * Get the postal code (required for delivery orders)
     * 
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    /**
     * Set the postal code
     * 
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    /**
     * Get the country (required for delivery orders)
     * 
     * @return country
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * Set the country
     * 
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
