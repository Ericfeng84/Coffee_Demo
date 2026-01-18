package com.bluemountain.coffee.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for orders.
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
public class OrderDTO {
    
    private String id;
    private String customerName;
    private String orderType;
    private String status;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddressDTO address;
    
    /**
     * Default constructor for JSON deserialization
     */
    public OrderDTO() {
    }
    
    /**
     * Get the order ID
     * 
     * @return id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set the order ID
     * 
     * @param id the order ID
     */
    public void setId(String id) {
        this.id = id;
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
     * Get the order type
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
     * Get the order status
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Set the order status
     * 
     * @param status the order status
     */
    public void setStatus(String status) {
        this.status = status;
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
    
    /**
     * Get the creation timestamp
     * 
     * @return createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Set the creation timestamp
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Get the last update timestamp
     * 
     * @return updatedAt
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Set the last update timestamp
     * 
     * @param updatedAt the last update timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Get the delivery address (for delivery orders)
     * 
     * @return address
     */
    public AddressDTO getAddress() {
        return address;
    }
    
    /**
     * Set the delivery address
     * 
     * @param address the delivery address
     */
    public void setAddress(AddressDTO address) {
        this.address = address;
    }
    
    /**
     * Nested DTO for address information
     */
    public static class AddressDTO {
        private String street;
        private String city;
        private String postalCode;
        private String country;
        
        /**
         * Default constructor
         */
        public AddressDTO() {
        }
        
        /**
         * Constructor with all fields
         */
        public AddressDTO(String street, String city, String postalCode, String country) {
            this.street = street;
            this.city = city;
            this.postalCode = postalCode;
            this.country = country;
        }
        
        public String getStreet() {
            return street;
        }
        
        public void setStreet(String street) {
            this.street = street;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public String getPostalCode() {
            return postalCode;
        }
        
        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
    }
}
