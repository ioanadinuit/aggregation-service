package com.fedex.assignment.integration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebfluxClient<T> {
    private static final int MAX_ATTEMPTS = 3;
    private static final int RETRY_MILLIS = 50;
    private static final int FLUX_TIMEOUT = 40;
    private static final int MONO_TIMEOUT = 25;
    public static final int CACHE_DURATION_MIN = 1;

    private final WebClient webClient;

    public Mono<Map<String, T>> getResources(String uri,
                                       String param,
                                       List<String> items) throws ExecutionException, InterruptedException {
        return Flux.fromIterable(items)
                .cache(Duration.ofMinutes(CACHE_DURATION_MIN))
                .timeout(Duration.ofMillis(FLUX_TIMEOUT))
                .flatMap(orderNo -> callApi(uri, param, orderNo)
                        .map(response -> new AbstractMap.SimpleEntry<>(String.valueOf(orderNo), response)
                        ))
                .collectMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue);
    }

    private Mono<T> callApi(String uri, String param, String item) {
        return webClient.get()
                .uri(buildUri(uri, param, item))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> handleError(response))
                .onStatus(HttpStatus::is5xxServerError, response -> handleError(response))
                .bodyToMono(new ParameterizedTypeReference<T>() {})
                .publishOn(Schedulers.parallel())
                .timeout(Duration.ofMillis(MONO_TIMEOUT), Mono.empty())
                .retryWhen(Retry.backoff(MAX_ATTEMPTS, Duration.ofMillis(RETRY_MILLIS)))
                .onErrorResume(error -> {
                    log.error("Shipment service responded with error: {} ", error.getStackTrace());
                    return Mono.empty();
                })
                .filter(Objects::nonNull)
                .cache(Duration.ofMinutes(CACHE_DURATION_MIN));
    }

    private String buildUri(String uri, String queryParam, String queryValue) {
        return UriComponentsBuilder
                .fromUriString(uri)
                .queryParam(queryParam, queryValue)
                .build().toUriString();
    }

    private Mono handleError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.error(body);
                    return Mono.empty();
                });
    }

}
