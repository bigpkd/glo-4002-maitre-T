package ca.ulaval.glo4002.reservation.reservation.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotNull;

public class CustomerDto {
  @NotNull
  public String name;

  @NotNull
  public List<String> restrictions;

  public CustomerDto(@JsonProperty(value = "name") String name,
                     @JsonProperty(value = "restrictions") List<String> restrictions) {
    this.name = name;
    this.restrictions = restrictions;
  }
}
