package com.fedex.assignment.util;

import org.springframework.web.util.UriComponentsBuilder;

public class Constants {
    public static final String AGG_URI = "/aggregation";
    public static final String PRICING_URI = "/pricing";
    public static final String COUNTRY_CODE_PARAM = "countryCode";
    public static final String TRACK_URI = "/track-status";
    public static final String SHIPMENT_URI = "/shipment-products";
    public static final String ORDERNUMBER_PARAM = "orderNumber";

    public static String buildUri(String uri, String queryParam, String queryValue) {
        return UriComponentsBuilder
                .fromUriString(uri)
                .queryParam(queryParam, queryValue)
                .build().toUriString();
    }
}
