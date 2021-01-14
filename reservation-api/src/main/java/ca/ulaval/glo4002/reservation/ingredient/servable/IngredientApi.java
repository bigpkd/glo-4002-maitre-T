package ca.ulaval.glo4002.reservation.ingredient.servable;

import ca.ulaval.glo4002.reservation.ingredient.rest.dto.IngredientDto;
import java.util.List;
import javax.ws.rs.GET;

public interface IngredientApi {
  @GET
  List<IngredientDto> fetchIngredients();
}
