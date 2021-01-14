package ca.ulaval.glo4002.reservation.ingredient.domain;

public interface IngredientRepository {

  Ingredient findByName(String name);
}
