package ca.ulaval.glo4002.reservation.ingredient.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

public class IngredientDto {

  @NotNull
  @NotEmpty
  public String name;

  @NotNull
  @NumberFormat
  public String pricePerKg;

  public IngredientDto(@JsonProperty(value = "name") String name,
                       @JsonProperty(value = "pricePerKg") String pricePerKg) {
    this.name = name;
    this.pricePerKg = pricePerKg;
  }
}
