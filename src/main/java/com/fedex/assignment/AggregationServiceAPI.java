package com.fedex.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class AggregationServiceAPI {

    public static void main(String[] args) {
        SpringApplication.run(AggregationServiceAPI.class, args);
    }
}
