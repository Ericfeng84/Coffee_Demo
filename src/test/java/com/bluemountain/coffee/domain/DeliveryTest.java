package com.bluemountain.coffee.domain;

import com.bluemountain.coffee.domain.model.aggregate.Delivery;
import com.bluemountain.coffee.domain.model.aggregate.Order;
import com.bluemountain.coffee.domain.model.enums.DeliveryStatus;
import com.bluemountain.coffee.domain.model.enums.DeliveryItemStatus;
import com.bluemountain.coffee.domain.model.enums.OrderStatus;
import com.bluemountain.coffee.domain.model.enums.OrderType;
import com.bluemountain.coffee.domain.model.valobj.Address;
import com.bluemountain.coffee.domain.model.valobj.DeliveryId;
import com.bluemountain.coffee.domain.model.valobj.Money;
import com.bluemountain.coffee.domain.model.valobj.OrderId;
import com.bluemountain.coffee.domain.model.valobj.RiderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Delivery aggregate.
 */
public class DeliveryTest {
    
    private Order order1;
    private Order order2;
    private RiderInfo riderInfo;
    
    @BeforeEach
    void setUp() {
        // Create test orders
        order1 = Order.create(
            "John Doe",
            OrderType.DELIVERY,
            new ArrayList<>(List.of(
                com.bluemountain.coffee.domain.model.aggregate.OrderItem.of("Latte", 2, Money.of(5.50))
            )),
            Address.of("123 Main St", "Springfield", "62701", "USA")
        );
        order1.settle(new com.bluemountain.coffee.domain.strategy.DeliveryPricingStrategy());
        order1.startPreparing();
        order1.markAsReady();
        
        order2 = Order.create(
            "Jane Smith",
            OrderType.DELIVERY,
            new ArrayList<>(List.of(
                com.bluemountain.coffee.domain.model.aggregate.OrderItem.of("Cappuccino", 1, Money.of(4.50))
            )),
            Address.of("125 Main St", "Springfield", "62701", "USA")
        );
        order2.settle(new com.bluemountain.coffee.domain.strategy.DeliveryPricingStrategy());
        order2.startPreparing();
        order2.markAsReady();
        
        // Create test rider info
        riderInfo = RiderInfo.of(
            "RIDER-001",
            "Mike Johnson",
            "555-123-4567",
            "BICYCLE"
        );
    }
    
    @Test
    void testDeliveryCreation() {
        // Create delivery with orders
        List<Order> orders = List.of(order1, order2);
        Delivery delivery = Delivery.create(orders);
        
        // Verify delivery was created
        assertNotNull(delivery);
        assertNotNull(delivery.getDeliveryId());
        assertEquals(DeliveryStatus.CREATED, delivery.getStatus());
        assertEquals(2, delivery.getItems().size());
        assertNotNull(delivery.getCreatedAt());
    }
    
    @Test
    void testDeliveryCreationWithEventPublisher() {
        // Create delivery with event publisher
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders, null);
        
        // Verify delivery was created
        assertNotNull(delivery);
        assertEquals(DeliveryStatus.CREATED, delivery.getStatus());
        assertEquals(1, delivery.getItems().size());
    }
    
    @Test
    void testAssignRider() {
        // Create delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        
        // Assign rider
        delivery.assignRider(riderInfo);
        
        // Verify rider was assigned
        assertEquals(DeliveryStatus.ASSIGNED, delivery.getStatus());
        assertNotNull(delivery.getRiderInfo());
        assertEquals("RIDER-001", delivery.getRiderInfo().getRiderId());
        assertEquals("Mike Johnson", delivery.getRiderInfo().getRiderName());
    }
    
    @Test
    void testAssignRiderWhenNotCreated() {
        // Create delivery and assign rider
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        
        // Try to assign another rider (should fail)
        RiderInfo newRider = RiderInfo.of("RIDER-002", "Tom Wilson", "555-987-6543", "MOTORCYCLE");
        assertThrows(IllegalStateException.class, () -> delivery.assignRider(newRider));
    }
    
    @Test
    void testMarkAsPickedUp() {
        // Create and assign delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        
        // Mark as picked up
        delivery.markAsPickedUp();
        
        // Verify delivery was picked up
        assertEquals(DeliveryStatus.PICKED_UP, delivery.getStatus());
        assertNotNull(delivery.getPickupTime());
        
        // Verify items were marked as picked up
        for (com.bluemountain.coffee.domain.model.aggregate.DeliveryItem item : delivery.getItems()) {
            assertEquals(DeliveryItemStatus.PICKED_UP, item.getItemStatus());
        }
    }
    
    @Test
    void testMarkAsPickedUpWithoutRider() {
        // Create delivery without rider
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        
        // Try to mark as picked up (should fail)
        assertThrows(IllegalStateException.class, delivery::markAsPickedUp);
    }
    
    @Test
    void testMarkAsInTransit() {
        // Create, assign, and pick up delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        delivery.markAsPickedUp();
        
        // Mark as in transit
        delivery.markAsInTransit();
        
        // Verify delivery is in transit
        assertEquals(DeliveryStatus.IN_TRANSIT, delivery.getStatus());
    }
    
    @Test
    void testMarkAsDelivered() {
        // Create, assign, pick up, and mark in transit
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        delivery.markAsPickedUp();
        delivery.markAsInTransit();
        
        // Mark as delivered
        delivery.markAsDelivered();
        
        // Verify delivery was delivered
        assertEquals(DeliveryStatus.DELIVERED, delivery.getStatus());
        assertNotNull(delivery.getDeliveryTime());
        
        // Verify items were marked as delivered
        for (com.bluemountain.coffee.domain.model.aggregate.DeliveryItem item : delivery.getItems()) {
            assertEquals(DeliveryItemStatus.DELIVERED, item.getItemStatus());
        }
    }
    
    @Test
    void testMarkAsDeliveredWhenNotInTransit() {
        // Create and assign delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        
        // Try to mark as delivered (should fail)
        assertThrows(IllegalStateException.class, delivery::markAsDelivered);
    }
    
    @Test
    void testCompleteDelivery() {
        // Create, assign, pick up, mark in transit, and deliver
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        delivery.markAsPickedUp();
        delivery.markAsInTransit();
        delivery.markAsDelivered();
        
        // Complete delivery
        delivery.complete();
        
        // Verify delivery was completed
        assertEquals(DeliveryStatus.COMPLETED, delivery.getStatus());
    }
    
    @Test
    void testCompleteDeliveryWhenNotDelivered() {
        // Create and assign delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        
        // Try to complete (should fail)
        assertThrows(IllegalStateException.class, delivery::complete);
    }
    
    @Test
    void testCancelDelivery() {
        // Create delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        
        // Cancel delivery
        delivery.cancel();
        
        // Verify delivery was cancelled
        assertEquals(DeliveryStatus.CANCELLED, delivery.getStatus());
    }
    
    @Test
    void testCancelDeliveryAfterPickup() {
        // Create, assign, and pick up delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        delivery.markAsPickedUp();
        
        // Try to cancel (should fail)
        assertThrows(IllegalStateException.class, delivery::cancel);
    }
    
    @Test
    void testIsActive() {
        // Create and assign delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        delivery.assignRider(riderInfo);
        
        // Verify delivery is active
        assertTrue(delivery.isActive());
        
        // Complete delivery
        delivery.markAsPickedUp();
        delivery.markAsInTransit();
        delivery.markAsDelivered();
        delivery.complete();
        
        // Verify delivery is not active
        assertFalse(delivery.isActive());
    }
    
    @Test
    void testIsTerminal() {
        // Create delivery
        List<Order> orders = List.of(order1);
        Delivery delivery = Delivery.create(orders);
        
        // Verify delivery is not terminal
        assertFalse(delivery.isTerminal());
        
        // Complete delivery
        delivery.complete();
        
        // Verify delivery is terminal
        assertTrue(delivery.isTerminal());
    }
    
    @Test
    void testGetOrders() {
        // Create delivery with multiple orders
        List<Order> orders = List.of(order1, order2);
        Delivery delivery = Delivery.create(orders);
        
        // Get orders from delivery
        List<Order> retrievedOrders = delivery.getOrders();
        
        // Verify orders
        assertEquals(2, retrievedOrders.size());
        assertTrue(retrievedOrders.contains(order1));
        assertTrue(retrievedOrders.contains(order2));
    }
    
    @Test
    void testGetOrderIds() {
        // Create delivery with multiple orders
        List<Order> orders = List.of(order1, order2);
        Delivery delivery = Delivery.create(orders);
        
        // Get order IDs from delivery
        List<OrderId> orderIds = delivery.getOrderIds();
        
        // Verify order IDs
        assertEquals(2, orderIds.size());
        assertTrue(orderIds.contains(order1.getId()));
        assertTrue(orderIds.contains(order2.getId()));
    }
    
}
