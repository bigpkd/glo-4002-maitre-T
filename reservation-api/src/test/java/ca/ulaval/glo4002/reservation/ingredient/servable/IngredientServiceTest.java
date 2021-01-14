package ca.ulaval.glo4002.reservation.ingredient.servable;

import ca.ulaval.glo4002.reservation.ingredient.repository.exceptions.IngredientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class IngredientServiceTest {
  private static final String INGREDIENT_NAME = "Tomato";

  private IngredientService ingredientService;
  private IngredientApi ingredientApi;
  private IngredientBuilder ingredientBuilder;

  @BeforeEach
  void setUp() {
    ingredientApi = mock(IngredientApi.class);
    ingredientBuilder = mock(IngredientBuilder.class);
    ingredientService = new IngredientService(ingredientApi,
        ingredientBuilder);
  }

  @Test
  void givenIngredientServiceWithEmptyRepository_whenFindingPricePerKgForIngredient_thenIngredientNotFoundExceptionIsThrown() {
    assertThrows(IngredientNotFoundException.class,
        () -> ingredientService.findPricePerKgForIngredient(INGREDIENT_NAME));
  }
}