package com.fedex.assignment.service;

import com.fedex.assignment.integration.client.PricingResourceApiCall;
import com.fedex.assignment.integration.client.ShipmentProductsApiCall;
import com.fedex.assignment.integration.client.StatusTrackingApiCall;
import com.fedex.assignment.model.ShipmentsTrackingDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AggregationServiceTest {

    @Mock
    private PricingResourceApiCall pricingApiCall;
    @Mock
    private ShipmentProductsApiCall shipmentProductsApiCall;
    @Mock
    private StatusTrackingApiCall statusTrackingApiCall;
    @Mock
    private WebClient webClient;

    @InjectMocks
    private AggregationService aggregationService;

    @Test
    void shouldReturnShipmentsTrackingInformation() throws ExecutionException, InterruptedException {
        // given
        List<String> shipmentsProducts = List.of("product1", "product2");
        List<String> trackOrderNumbers = List.of("order1", "order2");
        List<String> pricingCountryCodes = List.of("country1", "country2");

        Map<String, List<String>> shipmentProductsMap = new HashMap<>();
        shipmentProductsMap.put("product1", List.of("BOX"));
        shipmentProductsMap.put("product2", List.of("PALLET"));
        when(shipmentProductsApiCall.getShipmentProducts(shipmentsProducts))
                .thenReturn(Mono.just(shipmentProductsMap));

        Map<String, String> statusTrackingMap = new HashMap<>();
        statusTrackingMap.put("order1", "IN_TRANSIT");
        statusTrackingMap.put("order2", "DELIVERED");
        when(statusTrackingApiCall.getStatusTracking(trackOrderNumbers))
                .thenReturn(Mono.just(statusTrackingMap));

        Map<String, Double> pricingMap = new HashMap<>();
        pricingMap.put("country1", 1.5);
        pricingMap.put("country2", 2.5);
        when(pricingApiCall.getPricing(pricingCountryCodes))
                .thenReturn(Mono.just(pricingMap));

        // when
        Mono<ShipmentsTrackingDTO> shipmentsTrackingDTO =
                aggregationService.getShipmentTrackingInformation(
                        shipmentsProducts, trackOrderNumbers, pricingCountryCodes);

        StepVerifier.create(shipmentsTrackingDTO)
                .expectNextMatches(dto ->
                        dto.getShipments().equals(shipmentProductsMap)
                        && dto.getTrack().equals(statusTrackingMap)
                        && dto.getPricing().equals(pricingMap))
                .expectComplete();

        verify(shipmentProductsApiCall).getShipmentProducts(shipmentsProducts);
        verify(statusTrackingApiCall).getStatusTracking(trackOrderNumbers);
        verify(pricingApiCall).getPricing(pricingCountryCodes);
    }

    @Test
    void shouldReturnEmptyShipmentsTrackingInformation() throws ExecutionException, InterruptedException {
        // given
        List<String> shipmentsProducts = Collections.emptyList();
        List<String> trackOrderNumbers = Collections.emptyList();
        List<String> pricingCountryCodes = Collections.emptyList();

        // when
        Mono<ShipmentsTrackingDTO> shipmentsTrackingDTO = aggregationService
                .getShipmentTrackingInformation(shipmentsProducts, trackOrderNumbers, pricingCountryCodes);

        StepVerifier.create(shipmentsTrackingDTO)
                .expectNextMatches(dto ->
                        dto.getShipments().isEmpty()
                        && dto.getTrack().isEmpty()
                        && dto.getPricing().isEmpty())
                .expectComplete();

        verify(shipmentProductsApiCall, never()).getShipmentProducts(shipmentsProducts);
        verify(statusTrackingApiCall, never()).getStatusTracking(trackOrderNumbers);
        verify(pricingApiCall, never()).getPricing(pricingCountryCodes);
    }
}
