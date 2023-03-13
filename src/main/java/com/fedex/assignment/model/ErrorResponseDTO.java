package com.fedex.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {

  @JsonProperty("message")
  private String message;

  @JsonProperty("status")
  private Integer status;

}