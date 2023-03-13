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
public class StatusTrackingApiCall {
    public static final String TRACK_STATUS_URI = "/track-status";
    public static final String ORDER_NUMBER = "orderNumber";

    private final WebfluxClient<String> webfluxClient;

    public Mono<Map<String, String>> getStatusTracking(List<String> orderNos)
        throws ExecutionException, InterruptedException {

        return webfluxClient.getResources(TRACK_STATUS_URI, ORDER_NUMBER, orderNos);
    }
}
