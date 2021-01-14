package ca.ulaval.glo4002.reservation.ingredient.servable.exceptions;

import ca.ulaval.glo4002.reservation.exceptions.InfrastructureException;

public class IngredientExternalApiFailureException extends InfrastructureException {

  private static final String error = "INGREDIENT_EXTERNAL_API_FAILURE";

  public IngredientExternalApiFailureException(String message) {
    super(message);
  }

  @Override
  public String getError() {
    return error;
  }
}
