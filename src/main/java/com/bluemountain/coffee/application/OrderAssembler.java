package com.bluemountain.coffee.application;

import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.aggregate.OrderItem;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.model.valobj.Address;
import com.bluemountain.coffee.domain.model.valobj.Money;
import com.bluemountain.coffee.interfaces.dto.CreateOrderCommand;
import com.bluemountain.coffee.interfaces.dto.OrderDTO;
import com.bluemountain.coffee.interfaces.dto.OrderItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler for converting between domain objects and DTOs.
 * 
 * Assembler Pattern: Converts domain objects to DTOs and vice versa,
 * providing a clean separation between layers.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Hides conversion logic
 * - Single Responsibility: Only handles DTO conversions
 * 
 * SOLID Principles:
 * - Single Responsibility: Only converts between domain and DTO objects
 * - Open/Closed: Can be extended with new conversion methods
 * 
 * Benefits:
 * - Keeps domain model independent of interface layer
 * - Centralizes conversion logic
 * - Makes it easy to change DTO structure without affecting domain
 */
@Component
public class OrderAssembler {
    
    /**
     * Convert CreateOrderCommand to Order domain object
     * 
     * @param command the create order command
     * @return the Order domain object
     */
    public Order toDomain(CreateOrderCommand command) {
        OrderType orderType = OrderType.valueOf(command.getOrderType());
        
        List<OrderItem> items = command.getItems().stream()
                .map(itemDTO -> OrderItem.of(
                        itemDTO.getProductName(),
                        itemDTO.getQuantity(),
                        Money.of(itemDTO.getUnitPrice())
                ))
                .collect(Collectors.toList());
        
        Address address = null;
        if (orderType == OrderType.DELIVERY) {
            address = Address.of(
                    command.getStreet(),
                    command.getCity(),
                    command.getPostalCode(),
                    command.getCountry()
            );
        }
        
        return Order.create(
                command.getCustomerName(),
                orderType,
                items,
                address
        );
    }
    
    /**
     * Convert Order domain object to OrderDTO
     * 
     * @param order the order domain object
     * @return the OrderDTO
     */
    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId().toString());
        dto.setCustomerName(order.getCustomerName());
        dto.setOrderType(order.getType().name());
        dto.setStatus(order.getStatus().name());
        dto.setTotalPrice(order.getTotalPrice().getAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);
        
        if (order.getAddress() != null) {
            OrderDTO.AddressDTO addressDTO = new OrderDTO.AddressDTO(
                    order.getAddress().getStreet(),
                    order.getAddress().getCity(),
                    order.getAddress().getPostalCode(),
                    order.getAddress().getCountry()
            );
            dto.setAddress(addressDTO);
        }
        
        return dto;
    }
    
    /**
     * Convert OrderItem domain object to OrderItemDTO
     * 
     * @param item the order item domain object
     * @return the OrderItemDTO
     */
    private OrderItemDTO toItemDTO(OrderItem item) {
        return new OrderItemDTO(
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice().getAmount(),
                item.getTotalPrice().getAmount()
        );
    }
    
    /**
     * Convert string to OrderStatus
     * 
     * @param status the status string
     * @return the OrderStatus enum
     */
    public OrderStatus toOrderStatus(String status) {
        return OrderStatus.valueOf(status.toUpperCase());
    }
}
