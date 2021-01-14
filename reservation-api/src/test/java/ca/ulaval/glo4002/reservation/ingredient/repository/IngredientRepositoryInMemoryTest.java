package ca.ulaval.glo4002.reservation.ingredient.repository;

import ca.ulaval.glo4002.reservation.ingredient.domain.Ingredient;
import ca.ulaval.glo4002.reservation.ingredient.rest.dto.IngredientDto;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientApi;
import ca.ulaval.glo4002.reservation.ingredient.servable.IngredientBuilder;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IngredientRepositoryInMemoryTest {
  private static final String INGREDIENT_NAME = "Tomato";
  private static final String ANOTHER_INGREDIENT_NAME = "Potato";
  private static final Money PRICE_PER_KG = new Money(25.0);
  private static final Ingredient INGREDIENT = new Ingredient(INGREDIENT_NAME, PRICE_PER_KG);
  private static final Ingredient ANOTHER_INGREDIENT = new Ingredient(ANOTHER_INGREDIENT_NAME, PRICE_PER_KG);
  private static final String PRICE_PER_KG_LITTERAL = "25.0";

  private IngredientRepositoryInMemory ingredientRepositoryInMemory;
  private IngredientApi ingredientApi;
  private IngredientBuilder ingredientBuilder;
  private IngredientDto ingredientDto;
  private IngredientDto anotherIngredientDto;
  private List<IngredientDto> ingredientsDtos;
  private List<Ingredient> ingredients;

  @BeforeEach
  void setUp() {
    ingredientApi = mock(IngredientApi.class);
    ingredientBuilder = mock(IngredientBuilder.class);
    ingredientRepositoryInMemory = new IngredientRepositoryInMemory(ingredientApi, ingredientBuilder);
    givenNonEmptyIngredientRepositoryInMemory();
  }

  @Test
  void givenIngredientRepositoryInMemory_whenFindingIngredientByName_thenIngredientsAreFetched() {
    ingredientRepositoryInMemory.findByName(INGREDIENT_NAME);

    verify(ingredientApi).fetchIngredients();
  }

  @Test
  void givenIngredientRepositoryInMemory_whenFindingIngredientByName_thenIngredientsAreCreated() {
    ingredientRepositoryInMemory.findByName(INGREDIENT_NAME);

    verify(ingredientBuilder).create(ingredientsDtos);
  }

  @Test
  void givenIngredientServiceWithUpdatedIngredientRegister_whenFindPricePerKgForIngredient_thenPricePerKgIsObtained() {
    Ingredient ingredient = ingredientRepositoryInMemory.findByName(INGREDIENT_NAME);

    assertEquals(INGREDIENT_NAME, ingredient.getName());
    assertEquals(PRICE_PER_KG.getAmount().doubleValue(), ingredient.getPricePerKg().getAmount().doubleValue());
  }

  private void givenNonEmptyIngredientRepositoryInMemory() {
    ingredientDto = new IngredientDto(INGREDIENT_NAME, PRICE_PER_KG_LITTERAL);
    anotherIngredientDto = new IngredientDto(ANOTHER_INGREDIENT_NAME, PRICE_PER_KG_LITTERAL);
    ingredientsDtos = List.of(ingredientDto, anotherIngredientDto);
    ingredients = List.of(INGREDIENT, ANOTHER_INGREDIENT);
    when(ingredientApi.fetchIngredients()).thenReturn(ingredientsDtos);
    when(ingredientBuilder.create(ingredientsDtos)).thenReturn(ingredients);
  }
}