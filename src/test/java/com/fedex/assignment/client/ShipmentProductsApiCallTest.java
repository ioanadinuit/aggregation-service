package com.fedex.assignment.client;

import com.fedex.assignment.integration.client.ShipmentProductsApiCall;
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
public class ShipmentProductsApiCallTest {

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    private WebfluxClient<List<String>> reactiveApiClient;

    private ShipmentProductsApiCall shipmentProductsApiCall;

    @BeforeEach
    void init() {
        reactiveApiClient = new WebfluxClient<>(webClientMock);
        shipmentProductsApiCall = new ShipmentProductsApiCall(reactiveApiClient);
    }

    @Test
    void whenGetShipmentProductsMonoOK() throws ExecutionException, InterruptedException {
        List<String> orderNos = List.of("12345");

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(buildUri(SHIPMENT_URI, ORDERNUMBER_PARAM, orderNos.get(0))))
                .thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.onStatus(any(), any())).thenReturn(responseMock);
        when(responseMock.bodyToMono(ArgumentMatchers.<ParameterizedTypeReference<List<String>>>any()))
                .thenReturn(Mono.just(List.of("BOX")));

        Mono<Map<String, List<String>>> response =
                shipmentProductsApiCall.getShipmentProducts(orderNos);

        StepVerifier.create(response)
                .expectNext(Map.of("12345", List.of("BOX")))
                .expectComplete()
                .verify();
    }
}
