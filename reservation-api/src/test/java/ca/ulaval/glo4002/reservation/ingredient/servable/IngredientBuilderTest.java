package ca.ulaval.glo4002.reservation.ingredient.servable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import ca.ulaval.glo4002.reservation.ingredient.domain.Ingredient;
import ca.ulaval.glo4002.reservation.ingredient.rest.dto.IngredientDto;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngredientBuilderTest {
  private static final String INGREDIENT_NAME = "Lettuce";
  private static final String PRICE_PER_KG = "25.0";
  private static final Money EXPECTED_PRICE_PER_KG = new Money(25.0);

  private IngredientBuilder ingredientBuilder;
  private IngredientDto ingredientDto;
  private Ingredient expectedIngredient;

  @BeforeEach
  void setUp() {
    ingredientBuilder = new IngredientBuilder();
    ingredientDto = new IngredientDto(INGREDIENT_NAME, PRICE_PER_KG);
    expectedIngredient = new Ingredient(INGREDIENT_NAME, EXPECTED_PRICE_PER_KG);
  }

  @Test
  void givenIngredientBuilder_whenCreatingWithIngredientDto_ThenIngredientNameIsMapped() {
    Ingredient ingredient = ingredientBuilder.create(ingredientDto);

    assertEquals(expectedIngredient.getName(), ingredient.getName());
  }

  @Test
  void givenIngredientBuilder_whenCreatingWithIngredientDto_ThenPricePerKgIsMapped() {
    Ingredient ingredient = ingredientBuilder.create(ingredientDto);

    assertTrue(ingredient.getPricePerKg().equals(expectedIngredient.getPricePerKg()));
  }
}