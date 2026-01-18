package com.bluemountain.coffee.domain.model.valobj;

import java.util.Objects;

/**
 * Value Object representing rider information.
 * This is an immutable value object containing details about the assigned rider.
 */
public class RiderInfo {
    private final String riderId;
    private final String riderName;
    private final String phoneNumber;
    private final String vehicleType;

    public RiderInfo(String riderId, String riderName, String phoneNumber, String vehicleType) {
        if (riderId == null || riderId.trim().isEmpty()) {
            throw new IllegalArgumentException("RiderId cannot be null or empty");
        }
        if (riderName == null || riderName.trim().isEmpty()) {
            throw new IllegalArgumentException("RiderName cannot be null or empty");
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("PhoneNumber cannot be null or empty");
        }
        this.riderId = riderId;
        this.riderName = riderName;
        this.phoneNumber = phoneNumber;
        this.vehicleType = vehicleType != null ? vehicleType : "BICYCLE";
    }

    public String getRiderId() {
        return riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiderInfo riderInfo = (RiderInfo) o;
        return Objects.equals(riderId, riderInfo.riderId) &&
                Objects.equals(riderName, riderInfo.riderName) &&
                Objects.equals(phoneNumber, riderInfo.phoneNumber) &&
                Objects.equals(vehicleType, riderInfo.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riderId, riderName, phoneNumber, vehicleType);
    }

    @Override
    public String toString() {
        return "RiderInfo{" +
                "riderId='" + riderId + '\'' +
                ", riderName='" + riderName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }

    /**
     * Factory method to create RiderInfo.
     */
    public static RiderInfo of(String riderId, String riderName, String phoneNumber, String vehicleType) {
        return new RiderInfo(riderId, riderName, phoneNumber, vehicleType);
    }

    /**
     * Factory method to create RiderInfo with default vehicle type.
     */
    public static RiderInfo of(String riderId, String riderName, String phoneNumber) {
        return new RiderInfo(riderId, riderName, phoneNumber, "BICYCLE");
    }
}
