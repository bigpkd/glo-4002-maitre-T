package ca.ulaval.glo4002.reservation.reservation.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ReservationDetailDto {
  @NotNull
  @Valid
  public CountryDto country;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ss.SSSZ")
  public String reservationDate;

  public ReservationDetailDto(@JsonProperty(value = "country") @Valid CountryDto country,
                              @JsonProperty(value = "reservationDate") @Valid String reservationDate) {
    this.country = country;
    this.reservationDate = reservationDate;
  }
}