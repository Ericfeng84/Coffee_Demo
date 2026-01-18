package com.bluemountain.coffee.infrastructure.persistence;

import com.bluemountain.coffee.domain.model.aggregate.Delivery;
import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;
import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import com.bluemountain.coffee.domain.model.valobj.OrderId;
import com.bluemountain.coffee.domain.repository.DeliveryRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of DeliveryRepository.
 * 
 * DDD Concept: Infrastructure implementation of repository interface.
 * Provides persistence using in-memory storage for development/testing.
 * 
 * Design Patterns:
 * - Repository Pattern: Implements the repository interface
 * - Singleton Pattern: Single instance manages all deliveries
 */
public class InMemoryDeliveryRepository implements DeliveryRepository {
    
    private final Map<DeliveryId, Delivery> storage = new ConcurrentHashMap<>();
    
    @Override
    public Delivery save(Delivery delivery) {
        Objects.requireNonNull(delivery, "Delivery cannot be null");
        storage.put(delivery.getDeliveryId(), delivery);
        return delivery;
    }
    
    @Override
    public Optional<Delivery> findById(DeliveryId deliveryId) {
        Objects.requireNonNull(deliveryId, "Delivery ID cannot be null");
        return Optional.ofNullable(storage.get(deliveryId));
    }
    
    @Override
    public List<Delivery> findByStatus(DeliveryStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        return storage.values().stream()
            .filter(delivery -> delivery.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Delivery> findByRiderId(String riderId) {
        Objects.requireNonNull(riderId, "Rider ID cannot be null");
        return storage.values().stream()
            .filter(delivery -> delivery.getRiderInfo() != null && 
                               delivery.getRiderInfo().getRiderId().equals(riderId))
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Delivery> findByOrderId(OrderId orderId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        return storage.values().stream()
            .filter(delivery -> delivery.getOrderIds().contains(orderId))
            .findFirst();
    }
    
    @Override
    public List<Delivery> findActiveDeliveries() {
        return storage.values().stream()
            .filter(Delivery::isActive)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Delivery> findDeliveriesBetween(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "Start date cannot be null");
        Objects.requireNonNull(end, "End date cannot be null");
        return storage.values().stream()
            .filter(delivery -> !delivery.getCreatedAt().isBefore(start) && 
                               !delivery.getCreatedAt().isAfter(end))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Delivery> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void deleteById(DeliveryId deliveryId) {
        Objects.requireNonNull(deliveryId, "Delivery ID cannot be null");
        storage.remove(deliveryId);
    }
    
    @Override
    public boolean existsById(DeliveryId deliveryId) {
        Objects.requireNonNull(deliveryId, "Delivery ID cannot be null");
        return storage.containsKey(deliveryId);
    }
    
    /**
     * Clear all deliveries (useful for testing)
     */
    public void clear() {
        storage.clear();
    }
    
    /**
     * Get the count of all deliveries
     * 
     * @return the count
     */
    public int count() {
        return storage.size();
    }
}
