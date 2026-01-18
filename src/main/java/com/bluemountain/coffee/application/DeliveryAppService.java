package com.bluemountain.coffee.application;

import com.bluemountain.coffee.domain.model.aggregate.Delivery;
import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;
import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import com.bluemountain.coffee.domain.model.valobj.OrderId;
import com.bluemountain.coffee.domain.model.valobj.RiderInfo;
import com.bluemountain.coffee.domain.repository.DeliveryRepository;
import com.bluemountain.coffee.domain.repository.OrderRepository;
import com.bluemountain.coffee.domain.service.DeliveryBatchService;
import com.bluemountain.coffee.interfaces.dto.DeliveryDTO;
import com.bluemountain.coffee.interfaces.dto.DeliveryItemDTO;
import com.bluemountain.coffee.interfaces.dto.RiderInfoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Application Service for Delivery.
 * 
 * DDD Concept: Application Service coordinates use cases and orchestrates
 * domain objects to fulfill business requirements.
 * 
 * Design Patterns:
 * - Service Pattern: Provides application-level operations
 * - DTO Pattern: Uses DTOs for data transfer
 */
@Service
public class DeliveryAppService {
    
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryBatchService deliveryBatchService;
    
    public DeliveryAppService(DeliveryRepository deliveryRepository,
                              OrderRepository orderRepository,
                              DeliveryBatchService deliveryBatchService) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.deliveryBatchService = deliveryBatchService;
    }
    
    /**
     * Create a delivery batch from multiple orders
     * 
     * @param orderIds the list of order IDs to batch
     * @return the created delivery DTO
     */
    public DeliveryDTO createDeliveryBatch(List<String> orderIds) {
        List<Order> orders = orderIds.stream()
            .map(id -> orderRepository.findById(OrderId.of(id)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        
        if (orders.size() != orderIds.size()) {
            throw new IllegalArgumentException("One or more orders not found");
        }
        
        Delivery delivery = deliveryBatchService.createDeliveryBatch(orders);
        return toDTO(delivery);
    }
    
    /**
     * Automatically batch orders based on business rules
     * 
     * @return list of created delivery DTOs
     */
    public List<DeliveryDTO> autoBatchOrders() {
        List<Delivery> deliveries = deliveryBatchService.autoBatchOrders();
        return deliveries.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Assign a rider to a delivery
     * 
     * @param deliveryId the delivery ID
     * @param riderInfoDTO the rider information
     * @return the updated delivery DTO
     */
    public DeliveryDTO assignRider(String deliveryId, RiderInfoDTO riderInfoDTO) {
        Delivery delivery = deliveryRepository.findById(DeliveryId.of(deliveryId))
            .orElseThrow(() -> new IllegalArgumentException("Delivery not found: " + deliveryId));
        
        RiderInfo riderInfo = new RiderInfo(
            riderInfoDTO.getRiderId(),
            riderInfoDTO.getRiderName(),
            riderInfoDTO.getPhoneNumber(),
            riderInfoDTO.getVehicleType()
        );
        
        delivery.assignRider(riderInfo);
        deliveryRepository.save(delivery);
        
        return toDTO(delivery);
    }
    
    /**
     * Mark a delivery as picked up
     * 
     * @param deliveryId the delivery ID
     * @return the updated delivery DTO
     */
    public DeliveryDTO markAsPickedUp(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(DeliveryId.of(deliveryId))
            .orElseThrow(() -> new IllegalArgumentException("Delivery not found: " + deliveryId));
        
        delivery.markAsPickedUp();
        deliveryRepository.save(delivery);
        
        return toDTO(delivery);
    }
    
    /**
     * Mark a delivery as in transit
     * 
     * @param deliveryId the delivery ID
     * @return the updated delivery DTO
     */
    public DeliveryDTO markAsInTransit(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(DeliveryId.of(deliveryId))
            .orElseThrow(() -> new IllegalArgumentException("Delivery not found: " + deliveryId));
        
        delivery.markAsInTransit();
        deliveryRepository.save(delivery);
        
        return toDTO(delivery);
    }
    
    /**
     * Mark a delivery as delivered
     * 
     * @param deliveryId the delivery ID
     * @return the updated delivery DTO
     */
    public DeliveryDTO markAsDelivered(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(DeliveryId.of(deliveryId))
            .orElseThrow(() -> new IllegalArgumentException("Delivery not found: " + deliveryId));
        
        delivery.markAsDelivered();
        deliveryRepository.save(delivery);
        
        return toDTO(delivery);
    }
    
    /**
     * Complete a delivery
     * 
     * @param deliveryId the delivery ID
     * @return the updated delivery DTO
     */
    public DeliveryDTO completeDelivery(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(DeliveryId.of(deliveryId))
            .orElseThrow(() -> new IllegalArgumentException("Delivery not found: " + deliveryId));
        
        delivery.complete();
        deliveryRepository.save(delivery);
        
        return toDTO(delivery);
    }
    
    /**
     * Cancel a delivery
     * 
     * @param deliveryId the delivery ID
     * @return the updated delivery DTO
     */
    public DeliveryDTO cancelDelivery(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(DeliveryId.of(deliveryId))
            .orElseThrow(() -> new IllegalArgumentException("Delivery not found: " + deliveryId));
        
        delivery.cancel();
        deliveryRepository.save(delivery);
        
        return toDTO(delivery);
    }
    
    /**
     * Find delivery by ID
     * 
     * @param deliveryId the delivery ID
     * @return the delivery DTO
     */
    public DeliveryDTO findById(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(DeliveryId.of(deliveryId))
            .orElseThrow(() -> new IllegalArgumentException("Delivery not found: " + deliveryId));
        
        return toDTO(delivery);
    }
    
    /**
     * Find deliveries by status
     * 
     * @param status the delivery status
     * @return list of delivery DTOs
     */
    public List<DeliveryDTO> findByStatus(DeliveryStatus status) {
        List<Delivery> deliveries = deliveryRepository.findByStatus(status);
        return deliveries.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Find deliveries by rider ID
     * 
     * @param riderId the rider ID
     * @return list of delivery DTOs
     */
    public List<DeliveryDTO> findByRiderId(String riderId) {
        List<Delivery> deliveries = deliveryRepository.findByRiderId(riderId);
        return deliveries.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Find delivery containing a specific order
     * 
     * @param orderId the order ID
     * @return the delivery DTO
     */
    public Optional<DeliveryDTO> findByOrderId(String orderId) {
        Optional<Delivery> delivery = deliveryRepository.findByOrderId(OrderId.of(orderId));
        return delivery.map(this::toDTO);
    }
    
    /**
     * Find all active deliveries
     * 
     * @return list of active delivery DTOs
     */
    public List<DeliveryDTO> findActiveDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findActiveDeliveries();
        return deliveries.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Find deliveries in date range
     * 
     * @param start the start date/time
     * @param end the end date/time
     * @return list of delivery DTOs
     */
    public List<DeliveryDTO> findDeliveriesBetween(LocalDateTime start, LocalDateTime end) {
        List<Delivery> deliveries = deliveryRepository.findDeliveriesBetween(start, end);
        return deliveries.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Find all deliveries
     * 
     * @return list of all delivery DTOs
     */
    public List<DeliveryDTO> findAll() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Convert Delivery aggregate to DTO
     * 
     * @param delivery the delivery aggregate
     * @return the delivery DTO
     */
    private DeliveryDTO toDTO(Delivery delivery) {
        List<DeliveryItemDTO> items = delivery.getItems().stream()
            .map(this::toDeliveryItemDTO)
            .collect(Collectors.toList());
        
        RiderInfoDTO riderInfoDTO = null;
        if (delivery.getRiderInfo() != null) {
            riderInfoDTO = new RiderInfoDTO(
                delivery.getRiderInfo().getRiderId(),
                delivery.getRiderInfo().getRiderName(),
                delivery.getRiderInfo().getPhoneNumber(),
                delivery.getRiderInfo().getVehicleType()
            );
        }
        
        return new DeliveryDTO(
            delivery.getDeliveryId().getValue(),
            items,
            riderInfoDTO,
            delivery.getStatus(),
            delivery.getPickupTime(),
            delivery.getDeliveryTime(),
            delivery.getCreatedAt(),
            delivery.getUpdatedAt()
        );
    }
    
    /**
     * Convert DeliveryItem entity to DTO
     * 
     * @param item the delivery item entity
     * @return the delivery item DTO
     */
    private DeliveryItemDTO toDeliveryItemDTO(com.bluemountain.coffee.domain.model.aggregate.DeliveryItem item) {
        return new DeliveryItemDTO(
            item.getOrderId().getValue().toString(),
            item.getOrder().getCustomerName(),
            item.getOrder().getAddress() != null ? item.getOrder().getAddress().toString() : null,
            item.getOrder().getItems().stream()
                .map(orderItem -> orderItem.getProductName())
                .collect(Collectors.toList()),
            item.getOrder().getItems().stream()
                .mapToInt(com.bluemountain.coffee.domain.model.aggregate.OrderItem::getQuantity)
                .sum(),
            item.getItemStatus()
        );
    }
}
