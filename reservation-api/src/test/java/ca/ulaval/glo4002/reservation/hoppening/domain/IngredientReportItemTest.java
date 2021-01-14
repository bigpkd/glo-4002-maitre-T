package ca.ulaval.glo4002.reservation.hoppening.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportItem;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IngredientReportItemTest {
  private static final String INGREDIENT_NAME = "ingredient";

  private static final BigDecimal A_QUANTITY = new BigDecimal(5);
  private static final BigDecimal EXPECTED_QUANTITY = new BigDecimal(10);

  private static final Money PRICE = new Money(10);
  private static final Money EXPECTED_PRICE = new Money(20);

  private IngredientReportItem ingredientReportItem;

  @BeforeEach
  void setup() {
    ingredientReportItem = new IngredientReportItem(INGREDIENT_NAME, A_QUANTITY, PRICE);
  }

  @Test
  void givenIngredientReportItem_whenIncrementingQuantity_thenQuantityIsIncreasedByIncrementQuantity() {
    ingredientReportItem.incrementQuantity(A_QUANTITY);

    BigDecimal incrementedQuantity = ingredientReportItem.getQuantity();
    assertEquals(EXPECTED_QUANTITY, incrementedQuantity);
  }

  @Test
  void givenIngredientReportItem_whenIncrementingTotalPrice_thenTotalPriceIsIncreasedByIncrementAmount() {
    ingredientReportItem.incrementTotalPrice(PRICE);

    Money incrementedPrice = ingredientReportItem.getTotalPrice();
    assertTrue(incrementedPrice.equals(EXPECTED_PRICE));
  }

  @Test
  void givenTwoIngredientReportItems_whenCombiningQuantityAndPrice_thenQuantityAndPriceAreIncremented() {
    IngredientReportItem otherIngredientReportItem = new IngredientReportItem(INGREDIENT_NAME,
        A_QUANTITY, PRICE);

    ingredientReportItem.combineQuantityAndPrice(otherIngredientReportItem);

    BigDecimal incrementedQuantity = ingredientReportItem.getQuantity();
    Money incrementedPrice = ingredientReportItem.getTotalPrice();
    assertEquals(EXPECTED_QUANTITY, incrementedQuantity);
    assertTrue(incrementedPrice.equals(EXPECTED_PRICE));
  }

  @Test
  void givenTwoIngredienReportItems_whenCombiningQuantityAndPrice_thenOnlyTheReportCallingForTheCombiningIsModified(){
    IngredientReportItem otherIngredientReportItem = new IngredientReportItem(INGREDIENT_NAME,
        A_QUANTITY, PRICE);

    ingredientReportItem.combineQuantityAndPrice(otherIngredientReportItem);

    assertEquals(PRICE, otherIngredientReportItem.getTotalPrice());
    assertEquals(A_QUANTITY, otherIngredientReportItem.getQuantity());
  }
}
