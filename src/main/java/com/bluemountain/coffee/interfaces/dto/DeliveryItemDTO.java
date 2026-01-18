package com.bluemountain.coffee.interfaces.dto;

import com.bluemountain.coffee.domain.model.enums.DeliveryItemStatus;

import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for DeliveryItem.
 * Used for transferring delivery item data between layers.
 */
public class DeliveryItemDTO {
    private String orderId;
    private String customerName;
    private String address;
    private List<String> productNames;
    private int itemCount;
    private DeliveryItemStatus itemStatus;

    public DeliveryItemDTO() {
    }

    public DeliveryItemDTO(String orderId, String customerName, String address,
                          List<String> productNames, int itemCount, DeliveryItemStatus itemStatus) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.productNames = productNames;
        this.itemCount = itemCount;
        this.itemStatus = itemStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public void setProductNames(List<String> productNames) {
        this.productNames = productNames;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public DeliveryItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(DeliveryItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryItemDTO that = (DeliveryItemDTO) o;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "DeliveryItemDTO{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", itemCount=" + itemCount +
                ", itemStatus=" + itemStatus +
                '}';
    }
}
