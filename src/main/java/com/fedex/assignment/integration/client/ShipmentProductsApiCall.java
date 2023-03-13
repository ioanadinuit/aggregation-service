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
public class ShipmentProductsApiCall {
    public static final String SHIPMENT_PROD_URI = "/shipment-products";
    public static final String ORDER_NUMBER = "orderNumber";

    private final WebfluxClient<List<String>> webfluxClient;

    public Mono<Map<String, List<String>>> getShipmentProducts(List<String> orderNos)
        throws ExecutionException, InterruptedException {

        return webfluxClient.getResources(SHIPMENT_PROD_URI, ORDER_NUMBER, orderNos);
    }

}
