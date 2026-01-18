package com.bluemountain.coffee.domain.model.valobj;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for a delivery.
 * This is an immutable value object.
 */
public class DeliveryId {
    private final String value;

    public DeliveryId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("DeliveryId cannot be null or empty");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryId deliveryId = (DeliveryId) o;
        return Objects.equals(value, deliveryId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "DeliveryId{" + value + "}";
    }

    /**
     * Factory method to generate a new unique delivery ID.
     * In production, this would use a proper ID generation strategy.
     */
    public static DeliveryId generate() {
        return new DeliveryId("DLV-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000));
    }

    /**
     * Factory method to create a DeliveryId from a string value.
     */
    public static DeliveryId of(String value) {
        return new DeliveryId(value);
    }
}
