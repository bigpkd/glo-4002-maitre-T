package ca.ulaval.glo4002.reservation.hoppening.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportItem;
import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportRegister;
import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngredientReportRegisterTest {

  private static final String INGREDIENT_NAME_1 = "Carrots";
  private static final BigDecimal INGREDIENT_WEIGHT_1 = BigDecimal.valueOf(8);
  private static final Money COURSE_ITEM_PRICE_1 = new Money(5);

  private static final String INGREDIENT_NAME_2 = "Bacon";
  private static final BigDecimal INGREDIENT_WEIGHT_2 = BigDecimal.valueOf(4);
  private static final Money COURSE_ITEM_PRICE_2 = new Money(10);

  private static final Money EMPTY_TOTAL = new Money(0);
  private static final Money ONE_ITEM_TOTAL = new Money(40);
  private static final Money TWO_ITEMS_TOTAL = new Money(80);

  private IngredientReportRegister ingredientReportRegister;
  private CourseItem courseItem1;
  private CourseItem courseItem2;

  private HashMap<CourseItem, Money> ingredientReport;

  @BeforeEach
  void setUp() {
    ingredientReportRegister = new IngredientReportRegister();
    setupMockCourseItems();
    ingredientReport = new HashMap<>();
  }

  @Test
  void givenIngredientReportRegister_whenAddingIngredientReport_thenIngredientReportIsAddedToRegister() {
    ingredientReport.put(courseItem1, COURSE_ITEM_PRICE_1);

    ingredientReportRegister.addToRegister(ingredientReport);

    assertFalse(ingredientReportRegister.isEmpty());
  }

  @Test
  void givenIngredientReportRegister_whenAddingIngredientReport_thenIngredientReportCanBeFetchedFromRegister() {
    ingredientReport.put(courseItem1, COURSE_ITEM_PRICE_1);

    ingredientReportRegister.addToRegister(ingredientReport);
    HashMap<String, IngredientReportItem> returnedIngredientReport =
        ingredientReportRegister.getIngredientReportItems();
    IngredientReportItem ingredientReportItem = returnedIngredientReport.get(courseItem1.getIngredientName());


    assertEquals(courseItem1.getIngredientName(), ingredientReportItem.getIngredientName());
    assertEquals(courseItem1.getWeightInKg(), ingredientReportItem.getQuantity());
  }

  @Test
  void givenEmptyIngredientReportRegister_whenCalculatingIngredientReportTotal_thenIngredientReportTotalIsZero() {
    Money total = ingredientReportRegister.calculateIngredientReportTotal();

    assertTrue(total.equals(EMPTY_TOTAL));
  }

  @Test
  void givenOneIngredientReport_whenCalculatingIngredientReportTotal_thenTotalIsItemPrice() {
    ingredientReport.put(courseItem1, COURSE_ITEM_PRICE_1);
    ingredientReportRegister.addToRegister(ingredientReport);

    Money total = ingredientReportRegister.calculateIngredientReportTotal();

    assertTrue(total.equals(ONE_ITEM_TOTAL));
  }

  @Test
  void givenTwoIngredientReports_whenCalculatingIngredientReportTotal_thenTotalIsTotalPriceOfBothIngredientReports() {
    ingredientReport.put(courseItem1, COURSE_ITEM_PRICE_1);
    ingredientReport.put(courseItem2, COURSE_ITEM_PRICE_2);
    ingredientReportRegister.addToRegister(ingredientReport);

    Money total = ingredientReportRegister.calculateIngredientReportTotal();

    assertTrue(total.equals(TWO_ITEMS_TOTAL));
  }

  void setupMockCourseItems() {
    courseItem1 = mock(CourseItem.class);
    courseItem2 = mock(CourseItem.class);
    when(courseItem1.getIngredientName()).thenReturn(INGREDIENT_NAME_1);
    when(courseItem2.getIngredientName()).thenReturn(INGREDIENT_NAME_2);
    when(courseItem1.getWeightInKg()).thenReturn(INGREDIENT_WEIGHT_1);
    when(courseItem2.getWeightInKg()).thenReturn(INGREDIENT_WEIGHT_2);
  }

}