package com.fedex.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedex.assignment.model.ShipmentsTrackingDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fedex.assignment.util.Constants.AGG_URI;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class AggregationServiceIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAggregationWithEmptyDataShouldReturnOk() {
        // Make the HTTP request
        String url = buildUri(emptyList(), emptyList(), emptyList());
        assertTimeout(Duration.ofSeconds(5), () -> {
            webClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .consumeWith(response -> {
                        // Verify the response body
                        byte[] responseBody = response.getResponseBody();
                        assertNotNull(responseBody);
                        ShipmentsTrackingDTO actualDTO = null;
                        try {
                            actualDTO = objectMapper.readValue(responseBody, ShipmentsTrackingDTO.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assertThat(actualDTO).isNotNull();
                        assertThat(actualDTO.getPricing()).isEmpty();
                        assertThat(actualDTO.getTrack()).isEmpty();
                        assertThat(actualDTO.getShipments()).isEmpty();
                    });
        });
    }

    @Test
    void whenGetAggregationWithOnlyPricingDataShouldReturnOk() {
        String uri = buildUri(emptyList(), emptyList(), List.of("RO"));
        assertTimeout(Duration.ofSeconds(5), () -> {
            webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(response -> {
                    // Verify the response body
                    byte[] responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    ShipmentsTrackingDTO actualDTO = null;
                    try {
                        actualDTO = objectMapper.readValue(responseBody, ShipmentsTrackingDTO.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assertThat(actualDTO).isNotNull();
                    assertThat(actualDTO.getPricing()).isNotNull();
                    assertThat(actualDTO.getTrack()).isEmpty();
                    assertThat(actualDTO.getShipments()).isEmpty();
                    if (!actualDTO.getPricing().isEmpty()) {
                        Set<String> pricingKeys = actualDTO.getPricing().keySet();
                        assertThat(pricingKeys.stream().findAny()).isPresent();
                        assertThat(pricingKeys.stream().findAny().get()).isEqualTo("RO");
                    }
                });
        });


    }

    @Test
    void whenGetAggregationWithOnlyShipmentDataShouldReturnOk() {
        String uri = buildUri(emptyList(), List.of("236554"), emptyList());
        assertTimeout(Duration.ofSeconds(5), () -> {
        webClient.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith(response -> {
                // Verify the response body
                byte[] responseBody = response.getResponseBody();
                assertNotNull(responseBody);
                ShipmentsTrackingDTO actualDTO = null;
                try {
                    actualDTO = objectMapper.readValue(responseBody, ShipmentsTrackingDTO.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assertThat(actualDTO).isNotNull();
                assertThat(actualDTO.getShipments()).isNotNull();
                assertThat(actualDTO.getTrack()).isEmpty();
                assertThat(actualDTO.getShipments()).isEmpty();
                if (!actualDTO.getShipments().isEmpty()) {
                    Set<String> shipmentsKeys = actualDTO.getShipments().keySet();
                    assertThat(shipmentsKeys.stream().findAny()).isPresent();
                    assertThat(shipmentsKeys.stream().findAny().get()).isEqualTo("236554");
                }
            });
        });
    }

    @Test
    void whenGetAggregationWithOnlyTrackingDataShouldReturnOk() {
        String uri = buildUri(List.of("236554"), emptyList(), emptyList());
        assertTimeout(Duration.ofSeconds(5), () -> {
            webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(response -> {
                    // Verify the response body
                    byte[] responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    ShipmentsTrackingDTO actualDTO = null;
                    try {
                        actualDTO = objectMapper.readValue(responseBody, ShipmentsTrackingDTO.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assertThat(actualDTO).isNotNull();
                    assertThat(actualDTO.getTrack()).isNotNull();
                    assertThat(actualDTO.getTrack()).isEmpty();
                    assertThat(actualDTO.getShipments()).isEmpty();
                    if (!actualDTO.getTrack().isEmpty()) {
                        Set<String> trackKeys = actualDTO.getTrack().keySet();
                        assertThat(trackKeys.stream().findAny()).isPresent();
                        assertThat(trackKeys.stream().findAny().get()).isEqualTo("236554");
                    }
                });
        });
    }

    @Test
    void whenGetAggregationWithAllDataShouldReturnOk() {
        String uri = buildUri(List.of("236554"), List.of("12385214"), List.of("RO"));
        assertTimeout(Duration.ofSeconds(5), () -> {
            webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(response -> {
                    // Verify the response body
                    byte[] responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    ShipmentsTrackingDTO actualDTO = null;
                    try {
                        actualDTO = objectMapper.readValue(responseBody, ShipmentsTrackingDTO.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assertThat(actualDTO).isNotNull();
                    assertThat(actualDTO.getPricing()).isNotNull();
                    assertThat(actualDTO.getShipments()).isNotNull();
                    assertThat(actualDTO.getTrack()).isNotNull();

                    if (!actualDTO.getPricing().isEmpty()) {
                        Set<String> pricingKeys = actualDTO.getPricing().keySet();
                        assertThat(pricingKeys.stream().findAny()).isPresent();
                        assertThat(pricingKeys.stream().findAny().get()).isEqualTo("RO");
                    }

                    if (!actualDTO.getShipments().isEmpty()) {
                        Set<String> shipmentsKeys = actualDTO.getShipments().keySet();
                        assertThat(shipmentsKeys.stream().findAny()).isPresent();
                        assertThat(shipmentsKeys.stream().findAny().get()).isEqualTo("236554");
                    }

                    if (!actualDTO.getTrack().isEmpty()) {
                        Set<String> trackKeys = actualDTO.getTrack().keySet();
                        assertThat(trackKeys.stream().findAny()).isPresent();
                        assertThat(trackKeys.stream().findAny().get()).isEqualTo("12385214");
                    }
                });
        });
    }

    private String buildUri(List<String> shipmentOrderNo, List<String> trackOrderNo, List<String> countryCodes) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        String shipmentOrderNoStr = shipmentOrderNo.stream().collect(Collectors.joining(","));
        String trackOrderNoStr = trackOrderNo.stream().collect(Collectors.joining(","));
        String countryCodesString = countryCodes.stream().collect(Collectors.joining(","));
        queryParams.add("shipmentsOrderNumbers", shipmentOrderNoStr);
        queryParams.add("trackOrderNumbers", trackOrderNoStr);
        queryParams.add("pricingCountryCodes", countryCodesString);
        return UriComponentsBuilder.fromUriString(AGG_URI)
                .queryParams(queryParams).toUriString();
    }

}
