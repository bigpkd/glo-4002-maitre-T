package ca.ulaval.glo4002.reservation.ingredient.servable;

import ca.ulaval.glo4002.reservation.ingredient.domain.Ingredient;
import ca.ulaval.glo4002.reservation.ingredient.rest.dto.IngredientDto;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.ArrayList;
import java.util.List;

public class IngredientBuilder {
  public Ingredient create(IngredientDto ingredientDto) {
    String ingredientName = ingredientDto.name;
    Money pricePerKg = new Money(Double.parseDouble(ingredientDto.pricePerKg));

    return new Ingredient(ingredientName, pricePerKg);
  }

  public List<Ingredient> create(List<IngredientDto> ingredientsDto) {
    List<Ingredient> ingredients = new ArrayList<>();

    for (IngredientDto ingredientDto : ingredientsDto) {
      ingredients.add(create(ingredientDto));
    }

    return ingredients;
  }
}
