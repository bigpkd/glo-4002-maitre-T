package ca.ulaval.glo4002.reservation.reservation.repository.exceptions;

public class MealNotFoundException extends RuntimeException {
  private static final String message = "Meal %s doesn't exist";
  private static final String error = "MEAL_NOT_FOUND";

  public MealNotFoundException(String name) {
    super(String.format(message, name));
  }

  public String getError() {
    return error;
  }
}
