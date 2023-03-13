package com.fedex.assignment.service;

import com.fedex.assignment.integration.client.PricingResourceApiCall;
import com.fedex.assignment.integration.client.ShipmentProductsApiCall;
import com.fedex.assignment.integration.client.StatusTrackingApiCall;
import com.fedex.assignment.model.ShipmentsTrackingDTO;
import com.fedex.assignment.service.exception.AggregationServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.emptyMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;


@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationService {
    private final PricingResourceApiCall pricingApiCall;
    private final ShipmentProductsApiCall shipmentProductsApiCall;
    private final StatusTrackingApiCall statusTrackingApiCall;

    public Mono<ShipmentsTrackingDTO> getShipmentTrackingInformation(List<String> shipmentsProducts,
                                                                     List<String> trackOrderNumbers,
                                                                     List<String> pricingCountryCodes)  {
        try {
            Mono<Map<String, List<String>>> shipmentsMono = Mono.empty();
            if (isNotEmpty(shipmentsProducts)) {
                shipmentsMono = shipmentProductsApiCall.getShipmentProducts(shipmentsProducts);
            }
            Mono<Map<String, String>> trackMono = Mono.empty();
            if (isNotEmpty(trackOrderNumbers)) {
                trackMono = statusTrackingApiCall.getStatusTracking(trackOrderNumbers);
            }
            Mono<Map<String, Double>> pricingMono = Mono.empty();
            if (isNotEmpty(pricingCountryCodes)) {
                pricingMono = pricingApiCall.getPricing(pricingCountryCodes);
            }

            return Mono.zip(shipmentsMono.defaultIfEmpty(emptyMap()),
                        trackMono.defaultIfEmpty(emptyMap()),
                        pricingMono.defaultIfEmpty(emptyMap()))
                    .map(tuple -> ShipmentsTrackingDTO.builder()
                            .shipments(tuple.getT1())
                            .track(tuple.getT2())
                            .pricing(tuple.getT3())
                            .build());
        } catch (ExecutionException e) {
            log.error("Failed to execute call: {}", e.getMessage());
            throw  new AggregationServiceException(e.getMessage());
        } catch (InterruptedException e) {
            log.error("Failed to execute call: {}", e.getMessage());
            throw  new AggregationServiceException(e.getMessage());
        }
    }
}
