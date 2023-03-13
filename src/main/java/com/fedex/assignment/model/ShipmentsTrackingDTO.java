package com.fedex.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Data
@Builder
public class ShipmentsTrackingDTO {

  @JsonProperty("shipments")
  private Map<String, List<String>> shipments = emptyMap();

  @JsonProperty("track")
  private Map<String, String> track = emptyMap();

  @JsonProperty("pricing")
  private Map<String, Double> pricing = emptyMap();

}