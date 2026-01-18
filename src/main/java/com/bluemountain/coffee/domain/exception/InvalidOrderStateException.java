package com.bluemountain.coffee.domain.exception;

/**
 * Exception thrown when an invalid order state transition is attempted.
 * 
 * DDD Concept: Domain exceptions represent business rule violations.
 * This exception is thrown when trying to transition an order to an
 * invalid state based on the current state.
 * 
 * OOP Principles demonstrated:
 * - Inheritance: Extends DomainException
 * - Specialization: Specific exception for state transition errors
 */
public class InvalidOrderStateException extends DomainException {
    
    /**
     * Constructor with message
     * 
     * @param message the error message
     */
    public InvalidOrderStateException(String message) {
        super(message);
    }
    
    /**
     * Constructor with current and target status
     * 
     * @param currentStatus the current order status
     * @param targetStatus the attempted target status
     */
    public InvalidOrderStateException(String currentStatus, String targetStatus) {
        super(String.format("Cannot transition order from %s to %s", currentStatus, targetStatus));
    }
}
