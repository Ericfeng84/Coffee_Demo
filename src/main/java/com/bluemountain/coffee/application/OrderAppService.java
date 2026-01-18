package com.bluemountain.coffee.application;

import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.valobj.OrderId;
import com.bluemountain.coffee.domain.repository.OrderRepository;
import com.bluemountain.coffee.domain.service.PricingStrategy;
import com.bluemountain.coffee.infrastructure.service.PaymentService;
import com.bluemountain.coffee.interfaces.dto.CreateOrderCommand;
import com.bluemountain.coffee.interfaces.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for order management.
 * 
 * DDD Concept: Application Services orchestrate use cases by coordinating
 * domain objects and infrastructure services. They don't contain business logic.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Hides complex orchestration logic
 * - Single Responsibility: Orchestrates order-related use cases
 * 
 * SOLID Principles:
 * - Single Responsibility: Only orchestrates order operations
 * - Open/Closed: Can be extended with new methods
 * - Dependency Inversion: Depends on abstractions (repository, services)
 * 
 * Design Patterns Used:
 * - Service Layer: Provides application-level operations
 * - Transaction Management: Ensures data consistency
 * - Event Publishing: Publishes domain events
 */
@Service
public class OrderAppService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderAppService.class);
    
    private final OrderRepository orderRepository;
    private final PricingStrategyFactory pricingStrategyFactory;
    private final OrderAssembler orderAssembler;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * Constructor with dependency injection
     * 
     * @param orderRepository the order repository
     * @param pricingStrategyFactory the pricing strategy factory
     * @param orderAssembler the order assembler
     * @param paymentService the payment service
     * @param eventPublisher the event publisher
     */
    public OrderAppService(OrderRepository orderRepository,
                           PricingStrategyFactory pricingStrategyFactory,
                           OrderAssembler orderAssembler,
                           PaymentService paymentService,
                           ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.pricingStrategyFactory = pricingStrategyFactory;
        this.orderAssembler = orderAssembler;
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * Place a new order
     * 
     * Use Case: Customer places an order for coffee
     * 
     * @param command the create order command
     * @return the created order DTO
     */
    @Transactional
    public OrderDTO placeOrder(CreateOrderCommand command) {
        logger.info("Placing order for customer: {}", command.getCustomerName());
        
        // Convert command to domain object
        Order order = orderAssembler.toDomain(command);
        
        // Get the appropriate pricing strategy
        PricingStrategy pricingStrategy = pricingStrategyFactory.getStrategy(order.getType());
        
        // Settle the order (calculate price and process payment)
        order.settle(pricingStrategy);
        
        // Process payment
        boolean paymentSuccess = paymentService.processPayment(order.getId(), order.getTotalPrice());
        if (!paymentSuccess) {
            throw new RuntimeException("Payment processing failed");
        }
        
        // Start preparing the order
        order.startPreparing();
        
        // Save the order
        order = orderRepository.save(order);
        
        logger.info("Order placed successfully with ID: {}", order.getId());
        
        return orderAssembler.toDTO(order);
    }
    
    /**
     * Get an order by ID
     * 
     * @param orderId the order ID
     * @return the order DTO
     * @throws IllegalArgumentException if order not found
     */
    public OrderDTO getOrder(String orderId) {
        logger.debug("Getting order with ID: {}", orderId);
        
        OrderId id = OrderId.of(orderId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        return orderAssembler.toDTO(order);
    }
    
    /**
     * Get all orders
     * 
     * @return list of all order DTOs
     */
    public List<OrderDTO> getAllOrders() {
        logger.debug("Getting all orders");
        
        return orderRepository.findAll().stream()
                .map(orderAssembler::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get orders by status
     * 
     * @param status the order status
     * @return list of order DTOs with the given status
     */
    public List<OrderDTO> getOrdersByStatus(String status) {
        logger.debug("Getting orders by status: {}", status);
        
        OrderStatus orderStatus = orderAssembler.toOrderStatus(status);
        
        return orderRepository.findByStatus(orderStatus).stream()
                .map(orderAssembler::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Update order status
     * 
     * @param orderId the order ID
     * @param status the new status
     * @return the updated order DTO
     */
    @Transactional
    public OrderDTO updateOrderStatus(String orderId, String status) {
        logger.info("Updating order {} status to: {}", orderId, status);
        
        OrderId id = OrderId.of(orderId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        OrderStatus newStatus = orderAssembler.toOrderStatus(status);
        
        // Transition to new status
        order.transitionTo(newStatus);
        
        // Save the updated order
        order = orderRepository.save(order);
        
        logger.info("Order {} status updated to: {}", orderId, newStatus);
        
        return orderAssembler.toDTO(order);
    }
    
    /**
     * Mark coffee as ready for pickup/delivery
     * 
     * @param orderId the order ID
     * @return the updated order DTO
     */
    @Transactional
    public OrderDTO markCoffeeReady(String orderId) {
        logger.info("Marking coffee as ready for order: {}", orderId);
        
        OrderId id = OrderId.of(orderId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        // Mark as ready (this will publish a CoffeeReadyEvent)
        order.markAsReady();
        
        // Save the updated order
        order = orderRepository.save(order);
        
        logger.info("Coffee marked as ready for order: {}", orderId);
        
        return orderAssembler.toDTO(order);
    }
    
    /**
     * Complete an order
     * 
     * @param orderId the order ID
     * @return the updated order DTO
     */
    @Transactional
    public OrderDTO completeOrder(String orderId) {
        logger.info("Completing order: {}", orderId);
        
        OrderId id = OrderId.of(orderId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        // Complete the order
        order.complete();
        
        // Save the updated order
        order = orderRepository.save(order);
        
        logger.info("Order completed: {}", orderId);
        
        return orderAssembler.toDTO(order);
    }
    
    /**
     * Cancel an order
     * 
     * @param orderId the order ID
     * @return the updated order DTO
     */
    @Transactional
    public OrderDTO cancelOrder(String orderId) {
        logger.info("Cancelling order: {}", orderId);
        
        OrderId id = OrderId.of(orderId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        // Cancel the order
        order.cancel();
        
        // Process refund if payment was made
        if (order.getTotalPrice() != null) {
            paymentService.refundPayment(order.getId(), order.getTotalPrice());
        }
        
        // Save the updated order
        order = orderRepository.save(order);
        
        logger.info("Order cancelled: {}", orderId);
        
        return orderAssembler.toDTO(order);
    }
}
