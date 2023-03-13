package com.fedex.assignment.integration;

import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebfluxClientConfig implements WebFluxConfigurer {

    @Value("${shipment.service.url}")
    private String url;

    @Value("${shipment.service.maxMemorySize}")
    private Integer maxMemory;

    @Bean
    public WebClient getWebClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(maxMemory))
                .build();

        return  WebClient.builder()
                .baseUrl(url)
                .exchangeStrategies(strategies)
                .clientConnector(new HttpComponentsClientHttpConnector(
                        HttpAsyncClients.createDefault()))
                .build();
    }
}
