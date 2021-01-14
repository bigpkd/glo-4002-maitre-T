package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class CustomerFactoryTest {
  private static final String CUSTOMER_NAME = "Name";
  public static final String VEGETARIAN = "vegetarian";
  public static final String VEGAN = "vegan";
  public static final String ALLERGIES = "allergies";

  private static final List<String> VALID_CUSTOMER_RESTRICTION = new ArrayList<>(Arrays.asList(VEGETARIAN));
  private static final List<String> DOUBLES_CUSTOMER_RESTRICTION = new ArrayList<>(Arrays.asList(VEGAN, VEGAN));
  private static final List<String> MANY_UNORDERED_VALID_CUSTOMER_RESTRICTIONS =
      new ArrayList<>(Arrays.asList(VEGETARIAN, ALLERGIES));
  private static final List<String> MANY_ORDERED_VALID_CUSTOMER_RESTRICTIONS =
      new ArrayList<>(Arrays.asList(ALLERGIES, VEGETARIAN));

  private static final int DOUBLES_RESTRICTIONS_SIZE = 1;
  private static final Money RESTRICTION_PRICE = new Money(200);
  private static final Optional<Double> OTHER_RESTRICTION_PRICE = Optional.of(200d);

  private CustomerFactory customerFactory;
  private CustomerDto customerDto;
  private RestrictionFactory restrictionFactory;
  private RestrictionBooklet restrictionBooklet;
  private Restriction restriction;
  private Restriction otherRestriction;
  private Restriction someRestriction;
  private MealFactory mealFactory;
  private MealBooklet mealBooklet;

  @BeforeEach
  void setUp() {
    restriction = mock(Restriction.class);
    otherRestriction = mock(Restriction.class);
    restrictionFactory = mock(RestrictionFactory.class);
    mealFactory = mock(MealFactory.class);
    restrictionBooklet = mock(RestrictionBooklet.class);
    mealBooklet = mock(MealBooklet.class);
    customerFactory = new CustomerFactory(restrictionFactory, mealFactory, restrictionBooklet, mealBooklet);
    customerDto = new CustomerDto(CUSTOMER_NAME, VALID_CUSTOMER_RESTRICTION);
  }

  @Test
  void givenCustomerFactory_whenCreatingCustomer_thenRestrictionsAreCreated() {
    givenAVegetarianRestriction();

    customerFactory.create(customerDto);

    verify(restrictionFactory).create(VEGETARIAN, OTHER_RESTRICTION_PRICE);
  }

  @Test
  void givenCustomerFactory_whenCreatingCustomerWithValidRestrictions_ThenRestrictionsAreMappedIntoCustomer() {
    givenAVegetarianRestriction();
    customerDto.restrictions = VALID_CUSTOMER_RESTRICTION;

    Customer customer = customerFactory.create(customerDto);

    assertFalse(customer.getRestrictions().isEmpty());
  }

  @Test
  void whenCreatingCustomerWithRepeatingRestrictions_ThenOnlyOneInstanceOfEachRestrictionIsMappedIntoCustomer() {
    givenAVeganRestriction();
    customerDto.restrictions = DOUBLES_CUSTOMER_RESTRICTION;

    Customer customer = customerFactory.create(customerDto);

    assertEquals(DOUBLES_RESTRICTIONS_SIZE, customer.getRestrictions().size());
  }

  @Test
  void givenCustomerFactory_whenCreatingCustomerWithSeveralRestrictions_ThenRestrictionsAreMappedInAlphabeticalOrder() {
    givenAVegetarianRestriction();
    givenAnAllergiesRestriction();
    customerDto.restrictions = MANY_UNORDERED_VALID_CUSTOMER_RESTRICTIONS;

    Customer customer = customerFactory.create(customerDto);
    List<Restriction> restrictions = customer.getRestrictions();
    List<String> result = restrictions.stream().map(Restriction::getName).collect(Collectors.toList());

    assertEquals(MANY_ORDERED_VALID_CUSTOMER_RESTRICTIONS, result);
  }

  void givenAVegetarianRestriction() {
    when(restriction.getName()).thenReturn(VEGETARIAN);
    when(restriction.getPrice()).thenReturn(RESTRICTION_PRICE);
    when(restrictionBooklet.findPriceByName(VEGETARIAN)).thenReturn(OTHER_RESTRICTION_PRICE);
    when(restrictionFactory.create(VEGETARIAN, OTHER_RESTRICTION_PRICE)).thenReturn(restriction);

    when(mealBooklet.findCourseItemsNamesByMeal(VEGETARIAN)).thenReturn(VEGETARIAN_COURSE_ITEMS);
  }

  void givenAVeganRestriction() {
    when(otherRestriction.getName()).thenReturn(VEGAN);
    when(otherRestriction.getPrice()).thenReturn(RESTRICTION_PRICE);
    when(restrictionBooklet.findPriceByName(VEGAN)).thenReturn(OTHER_RESTRICTION_PRICE);
    when(restrictionFactory.create(VEGAN, OTHER_RESTRICTION_PRICE)).thenReturn(otherRestriction);

    when(mealBooklet.findCourseItemsNamesByMeal(VEGAN)).thenReturn(VEGAN_COURSE_ITEMS);
  }

  void givenAnAllergiesRestriction() {
    someRestriction = mock(Restriction.class);
    when(someRestriction.getName()).thenReturn(ALLERGIES);
    when(someRestriction.getPrice()).thenReturn(RESTRICTION_PRICE);
    when(restrictionBooklet.findPriceByName(ALLERGIES)).thenReturn(OTHER_RESTRICTION_PRICE);
    when(restrictionFactory.create(ALLERGIES, OTHER_RESTRICTION_PRICE)).thenReturn(someRestriction);

    when(mealBooklet.findCourseItemsNamesByMeal(ALLERGIES)).thenReturn(ALLERGIES_COURSE_ITEMS);
  }
  private static final Optional<List<CourseItem>> VEGETARIAN_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Pumpkin", 5),
      new CourseItem("Chocolate", 8),
      new CourseItem("Tuna", 10),
      new CourseItem("Mozzarella", 5),
      new CourseItem("Water", 0.1)
      )
  );
  private static final Optional<List<CourseItem>> VEGAN_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Tomato", 5),
      new CourseItem("Kiwi", 8),
      new CourseItem("Kimchi", 10),
      new CourseItem("Worcestershire sauce", 5),
      new CourseItem("Water", 0.1)
      )
  );
  private static final Optional<List<CourseItem>> ALLERGIES_COURSE_ITEMS = Optional.of(List.of(
      new CourseItem("Marmalade", 5),
      new CourseItem("Plantain", 8),
      new CourseItem("Tofu", 10),
      new CourseItem("Bacon", 5),
      new CourseItem("Water", 0.1)
      )
  );

}