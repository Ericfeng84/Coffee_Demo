package com.bluemountain.coffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Coffee Shop Demo.
 * 
 * This application demonstrates:
 * - Domain-Driven Design (DDD) principles
 * - Design patterns (Strategy, Factory, State, Builder, Repository)
 * - Object-Oriented Programming principles
 * - Spring Boot features (DI, REST API, Event handling)
 * 
 * @author Blue Mountain Coffee Shop Team
 */
@SpringBootApplication
public class CoffeeShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeShopApplication.class, args);
    }
}
