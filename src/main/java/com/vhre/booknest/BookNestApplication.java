package com.vhre.booknest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the BookNest application.
 *
 * <p>Standalone Spring Boot project (no shared base library): every module
 * carries its own entity, repository, mapper, service, service implementation,
 * DTO and controller, plus a common package for basic error handling.</p>
 */
@SpringBootApplication
public class BookNestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNestApplication.class, args);
    }
}
