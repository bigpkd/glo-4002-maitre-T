package ca.ulaval.glo4002.reservation.ingredient.repository.exceptions;

import ca.ulaval.glo4002.reservation.exceptions.InfrastructureException;

public class IngredientNotFoundException extends InfrastructureException {
  private static final String message = "Ingredient not found";
  private static final String error = "INGREDIENT_NOT_FOUND";

  public IngredientNotFoundException() {
    super(message);
  }

  public String getError() {
    return error;
  }
}
