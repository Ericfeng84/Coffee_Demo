package com.bluemountain.coffee.interfaces.web;

import com.bluemountain.coffee.application.DeliveryAppService;
import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;
import com.bluemountain.coffee.interfaces.dto.DeliveryDTO;
import com.bluemountain.coffee.interfaces.dto.RiderInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Delivery operations.
 * 
 * Design Patterns:
 * - REST API: Exposes delivery operations via HTTP endpoints
 * - DTO Pattern: Uses DTOs for request/response
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Hides business logic behind API endpoints
 * - Single Responsibility: Only handles HTTP request/response
 */
@RestController
@RequestMapping("/api/deliveries")
@CrossOrigin(origins = "*")
public class DeliveryController {
    
    private final DeliveryAppService deliveryAppService;
    
    public DeliveryController(DeliveryAppService deliveryAppService) {
        this.deliveryAppService = deliveryAppService;
    }
    
    /**
     * Create a delivery batch from multiple orders
     * 
     * @param orderIds list of order IDs to batch
     * @return created delivery
     */
    @PostMapping("/batch")
    public ResponseEntity<DeliveryDTO> createDeliveryBatch(@RequestBody List<String> orderIds) {
        try {
            DeliveryDTO delivery = deliveryAppService.createDeliveryBatch(orderIds);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Automatically batch orders based on business rules
     * 
     * @return list of created deliveries
     */
    @PostMapping("/auto-batch")
    public ResponseEntity<List<DeliveryDTO>> autoBatchOrders() {
        List<DeliveryDTO> deliveries = deliveryAppService.autoBatchOrders();
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * Assign a rider to a delivery
     * 
     * @param deliveryId delivery ID
     * @param riderInfoDTO rider information
     * @return updated delivery
     */
    @PostMapping("/{deliveryId}/assign-rider")
    public ResponseEntity<DeliveryDTO> assignRider(
            @PathVariable String deliveryId,
            @RequestBody RiderInfoDTO riderInfoDTO) {
        try {
            DeliveryDTO delivery = deliveryAppService.assignRider(deliveryId, riderInfoDTO);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mark a delivery as picked up
     * 
     * @param deliveryId delivery ID
     * @return updated delivery
     */
    @PostMapping("/{deliveryId}/pickup")
    public ResponseEntity<DeliveryDTO> markAsPickedUp(@PathVariable String deliveryId) {
        try {
            DeliveryDTO delivery = deliveryAppService.markAsPickedUp(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mark a delivery as in transit
     * 
     * @param deliveryId delivery ID
     * @return updated delivery
     */
    @PostMapping("/{deliveryId}/in-transit")
    public ResponseEntity<DeliveryDTO> markAsInTransit(@PathVariable String deliveryId) {
        try {
            DeliveryDTO delivery = deliveryAppService.markAsInTransit(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mark a delivery as delivered
     * 
     * @param deliveryId delivery ID
     * @return updated delivery
     */
    @PostMapping("/{deliveryId}/deliver")
    public ResponseEntity<DeliveryDTO> markAsDelivered(@PathVariable String deliveryId) {
        try {
            DeliveryDTO delivery = deliveryAppService.markAsDelivered(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Complete a delivery
     * 
     * @param deliveryId delivery ID
     * @return updated delivery
     */
    @PostMapping("/{deliveryId}/complete")
    public ResponseEntity<DeliveryDTO> completeDelivery(@PathVariable String deliveryId) {
        try {
            DeliveryDTO delivery = deliveryAppService.completeDelivery(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancel a delivery
     * 
     * @param deliveryId delivery ID
     * @return updated delivery
     */
    @PostMapping("/{deliveryId}/cancel")
    public ResponseEntity<DeliveryDTO> cancelDelivery(@PathVariable String deliveryId) {
        try {
            DeliveryDTO delivery = deliveryAppService.cancelDelivery(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Find delivery by ID
     * 
     * @param deliveryId delivery ID
     * @return delivery
     */
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> findById(@PathVariable String deliveryId) {
        try {
            DeliveryDTO delivery = deliveryAppService.findById(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Find deliveries by status
     * 
     * @param status delivery status
     * @return list of deliveries
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryDTO>> findByStatus(@PathVariable DeliveryStatus status) {
        List<DeliveryDTO> deliveries = deliveryAppService.findByStatus(status);
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * Find deliveries by rider ID
     * 
     * @param riderId rider ID
     * @return list of deliveries
     */
    @GetMapping("/rider/{riderId}")
    public ResponseEntity<List<DeliveryDTO>> findByRiderId(@PathVariable String riderId) {
        List<DeliveryDTO> deliveries = deliveryAppService.findByRiderId(riderId);
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * Find delivery containing a specific order
     * 
     * @param orderId order ID
     * @return delivery
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryDTO> findByOrderId(@PathVariable String orderId) {
        var delivery = deliveryAppService.findByOrderId(orderId);
        return delivery.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Find all active deliveries
     * 
     * @return list of active deliveries
     */
    @GetMapping("/active")
    public ResponseEntity<List<DeliveryDTO>> findActiveDeliveries() {
        List<DeliveryDTO> deliveries = deliveryAppService.findActiveDeliveries();
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * Find deliveries in date range
     * 
     * @param start start date/time
     * @param end end date/time
     * @return list of deliveries
     */
    @GetMapping("/between")
    public ResponseEntity<List<DeliveryDTO>> findDeliveriesBetween(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<DeliveryDTO> deliveries = deliveryAppService.findDeliveriesBetween(start, end);
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * Find all deliveries
     * 
     * @return list of all deliveries
     */
    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> findAll() {
        List<DeliveryDTO> deliveries = deliveryAppService.findAll();
        return ResponseEntity.ok(deliveries);
    }
}
