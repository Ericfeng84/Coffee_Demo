package com.bluemountain.coffee.domain.exception;

/**
 * Base exception class for domain-specific errors.
 * 
 * DDD Concept: Domain exceptions represent business rule violations
 * and should be handled appropriately at the application layer.
 * 
 * OOP Principles demonstrated:
 * - Inheritance: Base class for domain-specific exceptions
 * - Abstraction: Common interface for all domain exceptions
 */
public class DomainException extends RuntimeException {
    
    /**
     * Constructor with message
     * 
     * @param message the error message
     */
    public DomainException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     * 
     * @param message the error message
     * @param cause the underlying cause
     */
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
