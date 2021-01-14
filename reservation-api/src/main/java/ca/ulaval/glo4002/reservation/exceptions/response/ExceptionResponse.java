package ca.ulaval.glo4002.reservation.exceptions.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionResponse {
  public final String error;
  public final String description;

  @JsonCreator
  public ExceptionResponse(@JsonProperty(required = true) String error,
                           @JsonProperty(required = true) String description) {
    this.error = error;
    this.description = description;
  }
}
