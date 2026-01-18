package com.bluemountain.coffee.domain.repository;

import com.bluemountain.coffee.domain.model.aggregate.Delivery;
import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;
import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import com.bluemountain.coffee.domain.model.valobj.OrderId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Delivery aggregates.
 * 
 * DDD Concept: Repository provides collection-like access to domain objects.
 * Acts as an in-memory collection of aggregates, abstracting away persistence details.
 * 
 * Design Patterns:
 * - Repository Pattern: Encapsulates storage, retrieval, and search behavior
 */
public interface DeliveryRepository {
    
    /**
     * Save or update a delivery
     * 
     * @param delivery the delivery to save
     * @return the saved delivery
     */
    Delivery save(Delivery delivery);
    
    /**
     * Find delivery by ID
     * 
     * @param deliveryId the delivery ID
     * @return optional containing the delivery if found
     */
    Optional<Delivery> findById(DeliveryId deliveryId);
    
    /**
     * Find deliveries by status
     * 
     * @param status the delivery status
     * @return list of deliveries with the given status
     */
    List<Delivery> findByStatus(DeliveryStatus status);
    
    /**
     * Find deliveries by rider ID
     * 
     * @param riderId the rider ID
     * @return list of deliveries assigned to the rider
     */
    List<Delivery> findByRiderId(String riderId);
    
    /**
     * Find delivery containing a specific order
     * 
     * @param orderId the order ID
     * @return optional containing the delivery if found
     */
    Optional<Delivery> findByOrderId(OrderId orderId);
    
    /**
     * Find all active (not completed) deliveries
     * 
     * @return list of active deliveries
     */
    List<Delivery> findActiveDeliveries();
    
    /**
     * Find deliveries in date range
     * 
     * @param start the start date/time
     * @param end the end date/time
     * @return list of deliveries in the date range
     */
    List<Delivery> findDeliveriesBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Find all deliveries
     * 
     * @return list of all deliveries
     */
    List<Delivery> findAll();
    
    /**
     * Delete a delivery
     * 
     * @param deliveryId the delivery ID
     */
    void deleteById(DeliveryId deliveryId);
    
    /**
     * Check if a delivery exists
     * 
     * @param deliveryId the delivery ID
     * @return true if the delivery exists
     */
    boolean existsById(DeliveryId deliveryId);
}
