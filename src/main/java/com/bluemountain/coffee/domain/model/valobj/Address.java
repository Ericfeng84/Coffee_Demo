package com.bluemountain.coffee.domain.model.valobj;

import java.util.Objects;

/**
 * Value Object representing a delivery address.
 * 
 * DDD Concept: Value Objects are immutable objects that are defined
 * by their attributes rather than identity. Two Address objects with
 * the same attributes are considered equal.
 * 
 * OOP Principles demonstrated:
 * - Encapsulation: Internal representation hidden
 * - Immutability: Once created, cannot be modified
 * - Value equality: Based on value, not reference
 */
public final class Address {
    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;
    
    /**
     * Private constructor to enforce use of Builder pattern
     * 
     * @param street the street address
     * @param city the city name
     * @param postalCode the postal/zip code
     * @param country the country name
     */
    private Address(String street, String city, String postalCode, String country) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Postal code cannot be null or empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        
        this.street = street.trim();
        this.city = city.trim();
        this.postalCode = postalCode.trim();
        this.country = country.trim();
    }
    
    /**
     * Static factory method to create an Address
     * 
     * @param street the street address
     * @param city the city name
     * @param postalCode the postal/zip code
     * @param country the country name
     * @return new Address instance
     */
    public static Address of(String street, String city, String postalCode, String country) {
        return new Address(street, city, postalCode, country);
    }
    
    /**
     * Get the street address
     * 
     * @return street
     */
    public String getStreet() {
        return street;
    }
    
    /**
     * Get the city name
     * 
     * @return city
     */
    public String getCity() {
        return city;
    }
    
    /**
     * Get the postal/zip code
     * 
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    /**
     * Get the country name
     * 
     * @return country
     */
    public String getCountry() {
        return country;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
               Objects.equals(city, address.city) &&
               Objects.equals(postalCode, address.postalCode) &&
               Objects.equals(country, address.country);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode, country);
    }
    
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
