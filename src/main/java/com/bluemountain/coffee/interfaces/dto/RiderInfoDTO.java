package com.bluemountain.coffee.interfaces.dto;

import java.util.Objects;

/**
 * Data Transfer Object for RiderInfo.
 * Used for transferring rider information between layers.
 */
public class RiderInfoDTO {
    private String riderId;
    private String riderName;
    private String phoneNumber;
    private String vehicleType;

    public RiderInfoDTO() {
    }

    public RiderInfoDTO(String riderId, String riderName, String phoneNumber, String vehicleType) {
        this.riderId = riderId;
        this.riderName = riderName;
        this.phoneNumber = phoneNumber;
        this.vehicleType = vehicleType;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiderInfoDTO that = (RiderInfoDTO) o;
        return Objects.equals(riderId, that.riderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riderId);
    }

    @Override
    public String toString() {
        return "RiderInfoDTO{" +
                "riderId='" + riderId + '\'' +
                ", riderName='" + riderName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }
}
