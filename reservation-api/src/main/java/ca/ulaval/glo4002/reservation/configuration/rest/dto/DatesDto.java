package ca.ulaval.glo4002.reservation.configuration.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatesDto {
  public LocalDate beginDate;
  public LocalDate endDate;

  @JsonCreator
  public DatesDto(@JsonProperty(value = "beginDate") String beginDate,
                  @JsonProperty(value = "endDate") String endDate) {
    this.beginDate = parseDate(beginDate);
    this.endDate = parseDate(endDate);
  }

  private LocalDate parseDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(date, formatter);
  }
}
