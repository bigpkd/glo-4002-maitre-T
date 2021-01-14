package ca.ulaval.glo4002.reservation.configuration.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HoppeningDatesDto {
  public DatesDto reservationPeriodDto;
  public DatesDto hoppening;

  public HoppeningDatesDto(@JsonProperty(value = "reservationPeriod") DatesDto reservationPeriod,
                           @JsonProperty(value = "hoppening") DatesDto hoppening) {
    this.reservationPeriodDto = reservationPeriod;
    this.hoppening = hoppening;
  }
}
