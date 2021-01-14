package ca.ulaval.glo4002.reservation.reservation.domain;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.utils.Money;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerTest {
  private static final String CUSTOMER_NAME = "John";

  private static final Money THREE_DOLLAR = new Money(3L);
  private static final Money TWO_DOLLAR = new Money(2);
  private static final Money ONE_DOLLAR = new Money(1);
  private static final Money BASE_PRICE = new Money(1000);

  private static final String FIRST_RESTRICTION_NAME = "FIRST_RESTRICTION_NAME";
  private static final String INEXISTING_RESTRICTION_NAME = "INEXISTING_RESTRICTION_NAME";

  private static final String INGREDIENT_NAME = "ANY_INGREDIENT_NAME";
  private static final double ANY_WEIGHT = 0;
  private static final String A_NAME = "NAME";

  private List<Restriction> restrictions;
  private List<Meal> meals;

  @BeforeEach
  void setUp() {
    restrictions = new ArrayList<>();
    meals = new ArrayList<>();
  }

  @Test
  void givenCustomerWithRestrictions_whenCustomerIsCreated_thenRestrictionsAreAddedToCustomer() {
    Restriction restriction = new Restriction(A_NAME, ONE_DOLLAR);
    restrictions.add(restriction);

    Customer customer = new Customer(CUSTOMER_NAME, restrictions, meals);

    assertFalse(customer.getRestrictions().isEmpty());
  }

  @Test
  void givenCustomerWithoutRestriction_whenGettingCustomerPrice_thenCustomerPriceIsBasePrice() {
    Customer customer = new Customer(CUSTOMER_NAME, restrictions, meals);

    assertTrue(customer.getCustomerPrice().equals(BASE_PRICE));
  }

  @Test
  void givenCustomerWithRestrictions_whenGettingCustomerPrice_thenCustomerPriceIsSumOfRestrictionsPrices() {
    Restriction firstRestriction = new Restriction(A_NAME, ONE_DOLLAR);
    Restriction secondRestriction = new Restriction(A_NAME, TWO_DOLLAR);
    restrictions.add(firstRestriction);
    restrictions.add(secondRestriction);

    Customer customer = new Customer(CUSTOMER_NAME, restrictions, meals);

    Money expectedPrice = BASE_PRICE.add(THREE_DOLLAR);
    assertTrue(customer.getCustomerPrice().equals(expectedPrice));
  }

  @Test
  void givenCustomerWithRestrictions_whenAskingForExistingRestriction_thenShouldHaveIt(){
    Restriction firstRestriction = new Restriction(FIRST_RESTRICTION_NAME, ONE_DOLLAR);
    restrictions.add(firstRestriction);
    Customer customer = new Customer(CUSTOMER_NAME, restrictions, meals);

    boolean haveRestriction = customer.hasRestriction(FIRST_RESTRICTION_NAME);

    assertTrue(haveRestriction);
  }

  @Test
  void givenCustomerWithRestrictions_whenAskingForInexistingRestriction_thenShouldNotHaveIt(){
    Restriction firstRestriction = new Restriction(FIRST_RESTRICTION_NAME, ONE_DOLLAR);
    restrictions.add(firstRestriction);
    Customer customer = new Customer(CUSTOMER_NAME, restrictions, meals);

    boolean haveRestriction = customer.hasRestriction(INEXISTING_RESTRICTION_NAME);

    assertFalse(haveRestriction);
  }

  @Test
  void givenCustomerWithIngredient_whenAskingIfContainsIngredient_thenShouldHaveIt() {
    Meal meal = mock(Meal.class);
    CourseItem courseItem = new CourseItem(INGREDIENT_NAME, ANY_WEIGHT);
    when(meal.getCourseItems()).thenReturn(Arrays.asList(courseItem));
    meals.add(meal);
    Customer customer = new Customer(CUSTOMER_NAME, restrictions, meals);

    boolean hasIngredient = customer.hasIngredient(INGREDIENT_NAME);

    assertTrue(hasIngredient);
  }

  @Test
  void givenCustomerWithoutIngredient_whenAskingIfContainsIngredient_thenShouldNotHaveIt() {
    Customer customer = new Customer(CUSTOMER_NAME, restrictions, meals);

    boolean hasIngredient = customer.hasIngredient(INGREDIENT_NAME);

    assertFalse(hasIngredient);
  }
}