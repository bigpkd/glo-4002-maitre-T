package ca.ulaval.glo4002.reservation.reservation.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TableDto {
  @NotNull
  @Valid
  public List<CustomerDto> customers;

  public TableDto(@JsonProperty(value = "customers") List<CustomerDto> customers) {
    this.customers = customers;
  }
}
