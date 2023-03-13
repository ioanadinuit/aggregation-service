package com.fedex.assignment.client;

import com.fedex.assignment.integration.client.StatusTrackingApiCall;
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
public class StatusTrackingApiCallTest {

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    private WebfluxClient<String> reactiveApiClient;

    private StatusTrackingApiCall statusTrackingApiCall;

    @BeforeEach
    void init() {
        reactiveApiClient = new WebfluxClient<>(webClientMock);
        statusTrackingApiCall = new StatusTrackingApiCall(reactiveApiClient);
    }

    @Test
    void whenGetStatusTracking() throws ExecutionException, InterruptedException {
        List<String> orderNos = List.of("12345");

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(buildUri(TRACK_URI, ORDERNUMBER_PARAM, orderNos.get(0))))
                .thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.onStatus(any(), any())).thenReturn(responseMock);
        when(responseMock.bodyToMono(ArgumentMatchers.<ParameterizedTypeReference<String>>any()))
                .thenReturn(Mono.just("IN_TRANSIT"));

        Mono<Map<String, String>> response =
                statusTrackingApiCall.getStatusTracking(orderNos);

        StepVerifier.create(response)
                .expectNext(Map.of("12345", "IN_TRANSIT"))
                .expectComplete()
                .verify();
    }
}
