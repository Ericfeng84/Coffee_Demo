package com.bluemountain.coffee.domain.model.aggregate;

import com.bluemountain.coffee.domain.event.CoffeeReadyEvent;
import com.bluemountain.coffee.domain.event.OrderCreatedEvent;
import com.bluemountain.coffee.domain.exception.InvalidOrderStateException;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.model.valobj.Address;
import com.bluemountain.coffee.domain.model.valobj.Money;
import com.bluemountain.coffee.domain.model.valobj.OrderId;
import com.bluemountain.coffee.domain.service.PricingStrategy;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root representing an order in the coffee shop.
 * 
 * DDD Concept: Aggregate Root is the entry point to an aggregate.
 * All access to entities within the aggregate must go through the root.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Protects internal state and enforces invariants
 * - Inheritance: Base class for order-related concepts
 * - State Pattern: Manages order state transitions
 * - Builder Pattern: Provides factory method for complex object creation
 * 
 * SOLID Principles:
 * - Single Responsibility: Manages order lifecycle and invariants
 * - Open/Closed: Open for extension (new behaviors), closed for modification
 * - Liskov Substitution: Can be used wherever Order is expected
 * 
 * Design Patterns Used:
 * - State Pattern: OrderStatus enum with state transition logic
 * - Strategy Pattern: PricingStrategy for calculating prices
 * - Factory Pattern: create() factory method
 * - Domain Events: Publishes events for important state changes
 */
public class Order {
    
    private final OrderId id;
    private final String customerName;
    private final OrderType type;
    private final List<OrderItem> items;
    private final Address address;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderStatus status;
    private Money totalPrice;
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * Private constructor to enforce use of factory method
     * 
     * @param id the order ID
     * @param customerName the customer name
     * @param type the order type
     * @param items the list of order items
     * @param address the delivery address (null for dine-in)
     * @param createdAt the creation timestamp
     * @param eventPublisher the event publisher for domain events
     */
    private Order(OrderId id, String customerName, OrderType type, List<OrderItem> items, 
                  Address address, LocalDateTime createdAt, ApplicationEventPublisher eventPublisher) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        this.customerName = Objects.requireNonNull(customerName, "Customer name cannot be null");
        this.type = Objects.requireNonNull(type, "Order type cannot be null");
        this.items = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(items, "Items cannot be null")));
        this.address = address;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = createdAt;
        this.status = OrderStatus.CREATED;
        this.eventPublisher = eventPublisher;
        
        // Validate that delivery orders have an address
        if (type == OrderType.DELIVERY && address == null) {
            throw new IllegalArgumentException("Delivery orders must have an address");
        }
        
        // Validate that dine-in orders don't have an address
        if (type == OrderType.DINE_IN && address != null) {
            throw new IllegalArgumentException("Dine-in orders should not have an address");
        }
        
        // Validate that order has at least one item
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
    }
    
    /**
     * Factory method to create a new order
     * 
     * Design Pattern: Factory Method
     * Encapsulates complex object creation logic
     * 
     * @param customerName the customer name
     * @param type the order type
     * @param items the list of order items
     * @param address the delivery address (null for dine-in)
     * @return new Order instance
     */
    public static Order create(String customerName, OrderType type, List<OrderItem> items, Address address) {
        Order order = new Order(
                OrderId.generate(),
                customerName,
                type,
                items,
                address,
                LocalDateTime.now(),
                null
        );
        
        // Publish domain event
        if (order.eventPublisher != null) {
            order.eventPublisher.publishEvent(new OrderCreatedEvent(
                    order,
                    order.id,
                    type.name()
            ));
        }
        
        return order;
    }
    
    /**
     * Factory method to create a new order with event publisher
     * 
     * @param customerName the customer name
     * @param type the order type
     * @param items the list of order items
     * @param address the delivery address (null for dine-in)
     * @param eventPublisher the event publisher
     * @return new Order instance
     */
    public static Order create(String customerName, OrderType type, List<OrderItem> items, 
                               Address address, ApplicationEventPublisher eventPublisher) {
        Order order = new Order(
                OrderId.generate(),
                customerName,
                type,
                items,
                address,
                LocalDateTime.now(),
                eventPublisher
        );
        
        // Publish domain event
        order.eventPublisher.publishEvent(new OrderCreatedEvent(
                order,
                order.id,
                type.name()
        ));
        
        return order;
    }
    
    /**
     * Settle the order (process payment)
     * 
     * Design Pattern: Strategy Pattern
     * Uses the provided pricing strategy to calculate the total price
     * 
     * @param pricingStrategy the pricing strategy to use
     */
    public void settle(PricingStrategy pricingStrategy) {
        Objects.requireNonNull(pricingStrategy, "Pricing strategy cannot be null");
        
        // Validate state transition
        if (status != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(status.name(), OrderStatus.SETTLED.name());
        }
        
        // Calculate total price using strategy pattern
        this.totalPrice = pricingStrategy.calculate(this);
        this.status = OrderStatus.SETTLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Start preparing the coffee
     */
    public void startPreparing() {
        // Validate state transition
        if (status != OrderStatus.SETTLED) {
            throw new InvalidOrderStateException(status.name(), OrderStatus.PREPARING.name());
        }
        
        this.status = OrderStatus.PREPARING;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Mark the coffee as ready
     * 
     * Publishes a CoffeeReadyEvent domain event
     */
    public void markAsReady() {
        // Validate state transition
        if (status != OrderStatus.PREPARING) {
            throw new InvalidOrderStateException(status.name(), OrderStatus.READY.name());
        }
        
        this.status = OrderStatus.READY;
        this.updatedAt = LocalDateTime.now();
        
        // Publish domain event
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new CoffeeReadyEvent(
                    this,
                    this.id,
                    this.type.name(),
                    this.customerName
            ));
        }
    }
    
    /**
     * Complete the order
     */
    public void complete() {
        // Validate state transition
        if (status != OrderStatus.READY) {
            throw new InvalidOrderStateException(status.name(), OrderStatus.COMPLETED.name());
        }
        
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Cancel the order
     */
    public void cancel() {
        // Can only cancel if not yet completed
        if (status == OrderStatus.COMPLETED) {
            throw new InvalidOrderStateException("Cannot cancel a completed order");
        }
        
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Transition to a new status
     * 
     * Design Pattern: State Pattern
     * Manages state transitions with validation
     * 
     * @param newStatus the new status
     */
    public void transitionTo(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus, "New status cannot be null");
        
        switch (newStatus) {
            case SETTLED:
                throw new UnsupportedOperationException("Use settle() method instead");
            case PREPARING:
                startPreparing();
                break;
            case READY:
                markAsReady();
                break;
            case COMPLETED:
                complete();
                break;
            case CANCELLED:
                cancel();
                break;
            default:
                throw new InvalidOrderStateException(status.name(), newStatus.name());
        }
    }
    
    // Getters
    
    public OrderId getId() {
        return id;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public OrderType getType() {
        return type;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public Money getTotalPrice() {
        return totalPrice;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
