package ca.ulaval.glo4002.reservation.reservation.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class CountryDto {
  @NotNull
  public String code;

  @NotNull
  public String fullname;

  @NotNull
  public String currency;

  public CountryDto(@JsonProperty(value = "code") String code,
                    @JsonProperty(value = "fullname") String fullname,
                    @JsonProperty(value = "currency") String currency) {
    this.code = code;
    this.fullname = fullname;
    this.currency = currency;
  }
}
