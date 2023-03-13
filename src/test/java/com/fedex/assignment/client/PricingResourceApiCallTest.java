package com.fedex.assignment.client;

import com.fedex.assignment.integration.client.PricingResourceApiCall;
import com.fedex.assignment.integration.client.WebfluxClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.fedex.assignment.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PricingResourceApiCallTest {

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    private WebfluxClient<Double> reactiveApiClient;

    private PricingResourceApiCall pricingResourceApiCall;

    @BeforeEach
    void init() {
        reactiveApiClient = new WebfluxClient<>(webClientMock);
        pricingResourceApiCall = new PricingResourceApiCall(reactiveApiClient);
    }

    @Test
    void whenGetPricingMonoOk() throws ExecutionException, InterruptedException {
        List<String> countryCodes = List.of("RO");

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(buildUri(PRICING_URI, COUNTRY_CODE_PARAM, countryCodes.get(0))))
                .thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.onStatus(any(), any())).thenReturn(responseMock);
        when(responseMock.bodyToMono(ArgumentMatchers.<ParameterizedTypeReference<Double>>any()))
                .thenReturn(Mono.just(10.5));

        Mono<Map<String, Double>> result = pricingResourceApiCall.getPricing(List.of("RO"));
        StepVerifier.create(result)
                .expectNext(Map.of("RO", 10.5))
                .expectComplete()
                .verify();
    }
}
