package ca.ulaval.glo4002.reservation.ingredient.servable;

import ca.ulaval.glo4002.reservation.ingredient.domain.Ingredient;
import ca.ulaval.glo4002.reservation.ingredient.domain.IngredientRepository;
import ca.ulaval.glo4002.reservation.ingredient.repository.IngredientRepositoryInMemory;
import ca.ulaval.glo4002.reservation.ingredient.repository.exceptions.IngredientNotFoundException;
import ca.ulaval.glo4002.reservation.utils.Money;

import javax.inject.Inject;

public class IngredientService {
  private final IngredientRepository ingredientRepository;

  @Inject
  public IngredientService(IngredientApi ingredientApi,
                           IngredientBuilder ingredientBuilder/*,
                           IngredientRepository ingredientRepository*/) {
    this.ingredientRepository = new IngredientRepositoryInMemory(ingredientApi, ingredientBuilder);
  }

  public Money findPricePerKgForIngredient(String ingredientName) {
    Ingredient ingredient = ingredientRepository.findByName(ingredientName);

    if (ingredient == null) {
      throw new IngredientNotFoundException();
    }

    return ingredient.getPricePerKg();
  }
}
