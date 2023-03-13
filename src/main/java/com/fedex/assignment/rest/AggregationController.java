package com.fedex.assignment.rest;

import com.fedex.assignment.model.ShipmentsTrackingDTO;
import com.fedex.assignment.service.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AggregationController {
    private final AggregationService aggregationService;

    @GetMapping(value = "/aggregation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ShipmentsTrackingDTO> getAggregationResult(
            @RequestParam(value = "shipmentsOrderNumbers", required = false) List<String> shipments,
            @RequestParam(value = "trackOrderNumbers", required = false) List<String> track,
            @RequestParam(value = "pricingCountryCodes", required = false) List<String> pricing) {

        return aggregationService.getShipmentTrackingInformation(shipments, track, pricing);
    }

}
