package com.fedex.assignment.integration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PricingResourceApiCall {
    public static final String PRICING_URI = "/pricing";
    public static final String COUNTRY_CODE = "countryCode";

    private final WebfluxClient<Double> webfluxClient;

    public Mono<Map<String, Double>> getPricing(List<String> countryCodes)
        throws ExecutionException, InterruptedException {

        return webfluxClient.getResources(PRICING_URI, COUNTRY_CODE, countryCodes);
    }

}
