package com.bluemountain.coffee.infrastructure.service;

import com.bluemountain.coffee.domain.model.aggregate.Delivery;
import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.repository.DeliveryRepository;
import com.bluemountain.coffee.domain.repository.OrderRepository;
import com.bluemountain.coffee.domain.service.DeliveryBatchService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of DeliveryBatchService.
 * 
 * Batching Rules:
 * 1. Geographic Proximity: Group orders with nearby addresses
 * 2. Time Window: Group orders within a specific time window (e.g., 15 minutes)
 * 3. Capacity Limit: Maximum orders per delivery (e.g., 5 orders)
 * 4. Same Address Priority: Orders to same address should be in same delivery
 */
@Service
public class DeliveryBatchServiceImpl implements DeliveryBatchService {
    
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    // Batching configuration
    private static final int MAX_ORDERS_PER_DELIVERY = 5;
    private static final int BATCHING_TIME_WINDOW_MINUTES = 15;
    
    public DeliveryBatchServiceImpl(OrderRepository orderRepository,
                                     DeliveryRepository deliveryRepository,
                                     ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public Delivery createDeliveryBatch(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("Orders cannot be null or empty");
        }
        
        if (orders.size() > MAX_ORDERS_PER_DELIVERY) {
            throw new IllegalArgumentException(
                "Cannot batch more than " + MAX_ORDERS_PER_DELIVERY + " orders in a single delivery"
            );
        }
        
        // Validate all orders are delivery orders and in READY state
        for (Order order : orders) {
            if (order.getType() != OrderType.DELIVERY) {
                throw new IllegalArgumentException(
                    "Order " + order.getId() + " is not a delivery order"
                );
            }
            if (order.getStatus() != OrderStatus.READY) {
                throw new IllegalArgumentException(
                    "Order " + order.getId() + " is not in READY state. Current status: " + order.getStatus()
                );
            }
        }
        
        // Check if any order is already in a delivery
        for (Order order : orders) {
            if (deliveryRepository.findByOrderId(order.getId()).isPresent()) {
                throw new IllegalArgumentException(
                    "Order " + order.getId() + " is already in a delivery"
                );
            }
        }
        
        // Create and save the delivery
        Delivery delivery = Delivery.create(orders, eventPublisher);
        deliveryRepository.save(delivery);
        
        return delivery;
    }
    
    @Override
    public List<Order> findBatchableOrders() {
        // Find all delivery orders that are in READY state
        List<Order> readyDeliveryOrders = orderRepository.findByStatus(OrderStatus.READY).stream()
            .filter(order -> order.getType() == OrderType.DELIVERY)
            .collect(Collectors.toList());
        
        // Filter out orders that are already in a delivery
        List<Order> batchableOrders = readyDeliveryOrders.stream()
            .filter(order -> !deliveryRepository.findByOrderId(order.getId()).isPresent())
            .collect(Collectors.toList());
        
        return batchableOrders;
    }
    
    @Override
    public List<Delivery> autoBatchOrders() {
        List<Delivery> createdDeliveries = new ArrayList<>();
        List<Order> batchableOrders = findBatchableOrders();
        
        if (batchableOrders.isEmpty()) {
            return createdDeliveries;
        }
        
        // Sort orders by creation time
        batchableOrders.sort(Comparator.comparing(Order::getCreatedAt));
        
        // Group orders by address (same address priority)
        Map<String, List<Order>> ordersByAddress = batchableOrders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getAddress() != null ? order.getAddress().toString() : "UNKNOWN"
            ));
        
        // Create deliveries for each address group
        for (List<Order> addressGroup : ordersByAddress.values()) {
            List<Order> batch = new ArrayList<>();
            LocalDateTime batchStartTime = addressGroup.get(0).getCreatedAt();
            
            for (Order order : addressGroup) {
                // Check if order is within time window
                if (isWithinTimeWindow(order.getCreatedAt(), batchStartTime)) {
                    batch.add(order);
                    
                    // Create delivery if batch is full
                    if (batch.size() >= MAX_ORDERS_PER_DELIVERY) {
                        try {
                            Delivery delivery = createDeliveryBatch(new ArrayList<>(batch));
                            createdDeliveries.add(delivery);
                            batch.clear();
                            batchStartTime = order.getCreatedAt();
                        } catch (IllegalArgumentException e) {
                            // Skip if batching fails
                            batch.clear();
                        }
                    }
                } else {
                    // Create delivery for current batch and start new batch
                    if (!batch.isEmpty()) {
                        try {
                            Delivery delivery = createDeliveryBatch(new ArrayList<>(batch));
                            createdDeliveries.add(delivery);
                        } catch (IllegalArgumentException e) {
                            // Skip if batching fails
                        }
                        batch.clear();
                    }
                    batch.add(order);
                    batchStartTime = order.getCreatedAt();
                }
            }
            
            // Create delivery for remaining orders in batch
            if (!batch.isEmpty()) {
                try {
                    Delivery delivery = createDeliveryBatch(batch);
                    createdDeliveries.add(delivery);
                } catch (IllegalArgumentException e) {
                    // Skip if batching fails
                }
            }
        }
        
        return createdDeliveries;
    }
    
    @Override
    public boolean canBatchWith(Order order, List<Order> otherOrders) {
        if (order == null || otherOrders == null || otherOrders.isEmpty()) {
            return false;
        }
        
        // Check if order is a delivery order
        if (order.getType() != OrderType.DELIVERY) {
            return false;
        }
        
        // Check if order is in READY state
        if (order.getStatus() != OrderStatus.READY) {
            return false;
        }
        
        // Check if order is already in a delivery
        if (deliveryRepository.findByOrderId(order.getId()).isPresent()) {
            return false;
        }
        
        // Check if batch size limit is exceeded
        if (otherOrders.size() >= MAX_ORDERS_PER_DELIVERY) {
            return false;
        }
        
        // Check if order is within time window of other orders
        LocalDateTime earliestTime = otherOrders.stream()
            .map(Order::getCreatedAt)
            .min(LocalDateTime::compareTo)
            .orElse(order.getCreatedAt());
        
        if (!isWithinTimeWindow(order.getCreatedAt(), earliestTime)) {
            return false;
        }
        
        // Check if address is compatible (same address or nearby)
        // For simplicity, we require same address
        String orderAddress = order.getAddress() != null ? order.getAddress().toString() : "UNKNOWN";
        String otherAddress = otherOrders.get(0).getAddress() != null ? 
            otherOrders.get(0).getAddress().toString() : "UNKNOWN";
        
        return orderAddress.equals(otherAddress);
    }
    
    @Override
    public List<Order> findBatchableOrdersFor(Order referenceOrder, List<Order> allOrders) {
        if (referenceOrder == null || allOrders == null) {
            return new ArrayList<>();
        }
        
        return allOrders.stream()
            .filter(order -> !order.getId().equals(referenceOrder.getId()))
            .filter(order -> canBatchWith(order, Collections.singletonList(referenceOrder)))
            .collect(Collectors.toList());
    }
    
    /**
     * Check if a time is within the batching time window of a reference time
     * 
     * @param time the time to check
     * @param referenceTime the reference time
     * @return true if within time window
     */
    private boolean isWithinTimeWindow(LocalDateTime time, LocalDateTime referenceTime) {
        LocalDateTime windowStart = referenceTime.minusMinutes(BATCHING_TIME_WINDOW_MINUTES);
        LocalDateTime windowEnd = referenceTime.plusMinutes(BATCHING_TIME_WINDOW_MINUTES);
        return !time.isBefore(windowStart) && !time.isAfter(windowEnd);
    }
}
