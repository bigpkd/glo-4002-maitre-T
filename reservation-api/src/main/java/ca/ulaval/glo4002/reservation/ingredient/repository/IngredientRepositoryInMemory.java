package ca.ulaval.glo4002.reservation.ingredient.repository;

import ca.ulaval.glo4002.reservation.ingredient.domain.Ingredient;
import ca.ulaval.glo4002.reservation.ingredient.domain.IngredientRepository;
import ca.ulaval.glo4002.reservation.ingredient.rest.dto.IngredientDto;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientApi;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientBuilder;

import java.util.HashMap;
import java.util.List;

public class IngredientRepositoryInMemory implements IngredientRepository {
  private final HashMap<String, Ingredient> availableIngredients;
  private final IngredientApi ingredientApi;
  private final IngredientBuilder ingredientBuilder;

  public IngredientRepositoryInMemory(IngredientApi ingredientApi,
                                      IngredientBuilder ingredientBuilder) {
    this.ingredientApi = ingredientApi;
    this.ingredientBuilder = ingredientBuilder;
    this.availableIngredients = new HashMap<>();
  }

  public Ingredient findByName(String name) {
    updateIngredients();
    return availableIngredients.get(name);
  }

  private void updateIngredients() {
    List<IngredientDto> ingredientDtos = ingredientApi.fetchIngredients();
    List<Ingredient> ingredients = ingredientBuilder.create(ingredientDtos);
    for (Ingredient ingredient : ingredients) {
      availableIngredients.put(ingredient.getName(), ingredient);
    }
  }

}
