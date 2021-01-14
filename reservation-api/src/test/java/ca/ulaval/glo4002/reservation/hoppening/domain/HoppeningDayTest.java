package ca.ulaval.glo4002.reservation.hoppening.domain;

import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportRegister;
import ca.ulaval.glo4002.reservation.reservation.domain.Chef;
import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.servable.ChefBooklet;
import ca.ulaval.glo4002.reservation.reservation.servable.ChefFactory;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HoppeningDayTest {
  private static final String INGREDIENT_NAME = "INGREDIENT_NAME";

  private static final int THREE_CUSTOMERS = 3;
  private static final int ZERO_CUSTOMER = 0;

  private static final String JUNIOR_FIRST_SECOND_SPECIALTY_CHEF = "Écharlotte Cardin";
  private static final String SENIOR_SECOND_SPECIALTY_CHEF = "Bill Adicion";
  private static final String FIRST_SPECIALTY = "vegan";
  private static final String SECOND_SPECIALTY = "allergies";
  private static final int A_NUMBER_OF_RESTRICTION_INFERIOR_TO_HALF_MAX_CHEF_CUSTOMER = 2;
  private static final int A_NUMBER_OF_RESTRICTION_SUPERIOR_TO_MAX_CHEF_CUSTOMER = 7;
  private static final int EXPECTED_NUMBER_OF_CHEF_ONE = 1;
  private static final int EXPECTED_NUMBER_OF_CHEF_TWO = 2;

  private static final String NONEXISTENT_RESTRICTION_NAME = "NONEXISTENT_RESTRICTION_NAME";
  private static final String FIRST_RESTRICTION_NAME = "FIRST_RESTRICTION_NAME";
  private static final Money FIRST_RESTRICTION_PRICE = new Money(125.3);
  private static final String SECOND_RESTRICTION_NAME = "SECOND_RESTRICTION_NAME";
  private static final Money SECOND_RESTRICTION_PRICE = new Money(148.6);

  private static final LocalDate A_DATE_IN_SCHEDULE = LocalDate.of(2020, 10, 28);

  private static final String VEGETERIAN_ILLNESS_CHEF = "Éric Ardo";
  private static final String NO_RESTRICTION_ILLNESS_CHEF = "Hans Riz";
  private static final String VEGETARIAN_SPECIALTY = "vegetarian";
  private static final String NO_RESTRICTION_SPECIALTY = "no restriction";

  private static final List<String> CHEF_NAMES = Arrays.asList("Thierry Aki", "Bob Smarties", "Bob Rossbeef",
      "Bill Adicion", "Omar Calmar", "Écharlotte Cardin", "Éric Ardo", "Hans Riz", "Amélie Mélo");
  public static final int MAXIMUM_NUMBER_OF_CUSTOMERS_PER_CHEF = 5;
  public static final Money CHEF_SALARY = new Money(6000.00);

  private IngredientReportRegister ingredientReportRegister;
  private HoppeningDay hoppeningDay;
  private HashMap<String, Integer> restrictionsCount;
  private ChefFactory chefFactory;
  private ChefBooklet chefBooklet;

  @BeforeEach
  void SetUp() {
    ingredientReportRegister = mock(IngredientReportRegister.class);
    chefFactory = new ChefFactory();
    restrictionsCount = new HashMap<>();
    chefBooklet = mock(ChefBooklet.class);
    hoppeningDay = new HoppeningDay(A_DATE_IN_SCHEDULE, ingredientReportRegister, chefFactory, chefBooklet);

    givenAChefBooklet();
  }

  @Test
  void whenHoppeningDayIsCreated_thenCustomersCountIsZero() {
    assertEquals(ZERO_CUSTOMER, hoppeningDay.getCustomerCount());
  }

  @Test
  void whenHoppeningDayIsCreated_thenDoesNotContainReservation() {
    assertFalse(hoppeningDay.hasReservations());
  }

  @Test
  void givenHoppeningDay_whenAddingCustomer_thenCustomerCountIsIncreasedByNumberOfAddedCustomer() {
    int startingAmountOfCustomers = hoppeningDay.getCustomerCount();
    int expectedAmountOfCustomers = startingAmountOfCustomers + THREE_CUSTOMERS;

    hoppeningDay.addNbOfCustomers(THREE_CUSTOMERS);

    assertEquals(expectedAmountOfCustomers, hoppeningDay.getCustomerCount());
  }

  @Test
  void givenHoppeningDayWithReservations_whenCheckingIfHoppeningDayHasReservations_thenTrueIsObtained() {
    hoppeningDay.addNbOfCustomers(THREE_CUSTOMERS);

    assertTrue(hoppeningDay.hasReservations());
  }

  @Test
  void whenLookingForIngredient_thenShouldAskIngredientReportRegister() {
    hoppeningDay.containsIngredient(INGREDIENT_NAME);

    verify(ingredientReportRegister).containsIngredient(INGREDIENT_NAME);
  }

  @Test
  void whenAddingRestrictions_thenAllRestrictionsShouldBeAddedToDailyRestrictions() {
    Restriction firstRestriction = new Restriction(FIRST_RESTRICTION_NAME, FIRST_RESTRICTION_PRICE);
    Restriction secondRestriction = new Restriction(SECOND_RESTRICTION_NAME, SECOND_RESTRICTION_PRICE);
    List<Restriction> restrictions = Arrays.asList(firstRestriction, secondRestriction);

    hoppeningDay.addRestriction(restrictions);

    assertTrue(hoppeningDay.restrictionExistsInDailyRestriction(FIRST_RESTRICTION_NAME));
    assertTrue(hoppeningDay.restrictionExistsInDailyRestriction(SECOND_RESTRICTION_NAME));
  }

  @Test
  void whenSearchingForInexistingRestriction_thenHoppeningDayShouldNotContainIt() {
    boolean restrictionExists = hoppeningDay.restrictionExistsInDailyRestriction(NONEXISTENT_RESTRICTION_NAME);

    assertFalse(restrictionExists);
  }

  @Test
  void whenAddingMultipleRestriction_thenRestrictionCountIsIncreased() {
    Restriction firstRestriction = new Restriction(FIRST_RESTRICTION_NAME, FIRST_RESTRICTION_PRICE);
    Restriction secondRestriction =
        new Restriction(SECOND_RESTRICTION_NAME, SECOND_RESTRICTION_PRICE);
    List<Restriction> restrictions = Arrays.asList(firstRestriction, secondRestriction);
    int expectedRestrictionCount = 2;

    hoppeningDay.addRestriction(restrictions);

    assertEquals(expectedRestrictionCount, hoppeningDay.countRestrictionsForMaterialReport());
  }


  @Test
  void whenAddingSameRestriction_thenRestrictionCountIsIncreased() {
    Restriction firstRestriction = new Restriction(FIRST_RESTRICTION_NAME, FIRST_RESTRICTION_PRICE);
    List<Restriction> restrictions = Arrays.asList(firstRestriction, firstRestriction);
    int expectedRestrictionCount = 2;

    hoppeningDay.addRestriction(restrictions);

    assertEquals(expectedRestrictionCount, hoppeningDay.countRestrictionsForMaterialReport());
  }

  @Test
  void givenMultipleSeniorChefsAndJuniorChefHavingTwoRequiredSpecialties_whenGettingAssignedChefs_thenOnlyJuniorChefIsAssigned() {
    setUpHoppeningDayReservationRequiringTwoSpecialtyThatOneChefCanHandleAlone();

    List<Chef> assignedChef = hoppeningDay.getAssignedChefs(restrictionsCount);

    assertEquals(EXPECTED_NUMBER_OF_CHEF_ONE, assignedChef.size());
    assertEquals(JUNIOR_FIRST_SECOND_SPECIALTY_CHEF, assignedChef.get(0).getName());
  }

  @Test
  void whenHoppeningDayHasOneReservationRequiringOneSpecialty_thenSeniorChefHavingSpecialtyIsAssigned() {
    setUpHoppeningDayReservationRequiringOneSpecialtyThatOneChefCanHandleAlone();

    List<Chef> assignedChef = hoppeningDay.getAssignedChefs(restrictionsCount);

    assertEquals(EXPECTED_NUMBER_OF_CHEF_ONE, assignedChef.size());
    assertEquals(SENIOR_SECOND_SPECIALTY_CHEF, assignedChef.get(0).getName());
  }

  @Test
  void whenHoppeningDayHasOneReservationRequiringOneSpecialty_thenTwoChefHavingSpecialtyAreAssignedInTheCorrectOrder() {
    setUpHoppeningDayReservationRequiringOneSpecialtyThatOneChefCannotHandleAlone();

    List<Chef> assignedChef = hoppeningDay.getAssignedChefs(restrictionsCount);

    assertEquals(EXPECTED_NUMBER_OF_CHEF_TWO, assignedChef.size());
    assertEquals(SENIOR_SECOND_SPECIALTY_CHEF, assignedChef.get(0).getName());
    assertEquals(JUNIOR_FIRST_SECOND_SPECIALTY_CHEF, assignedChef.get(1).getName());
  }

  @Test
  void whenRegisteringIngredientReport_thenReportIsAddedToRegister(){
    HashMap<CourseItem, Money> pricePerCourseItem = new HashMap<>();

    hoppeningDay.registerIngredientReport(pricePerCourseItem);

    verify(ingredientReportRegister).addToRegister(pricePerCourseItem);
  }

  @Test
  void whenCalculatingIngredientReportTotal_thenMethodOfTheSameNameFromIngredientReportRegisterIsCalled() {
    hoppeningDay.calculateIngredientReportTotal();

    verify(ingredientReportRegister).calculateIngredientReportTotal();
  }

  @Test
  void whenComputingTotalPriceOfChefs_thenTotalPriceOfChefsIsObtained() {
    setUpHoppeningDayWithEnoughRestrictionsToRequireTwoChefs();

    Money computedTotalPrice = hoppeningDay.computeTotalPriceOfChefs();

    Chef expectedChef = chefFactory.create(VEGETERIAN_ILLNESS_CHEF,
        chefBooklet.findRestrictionsByChef(VEGETERIAN_ILLNESS_CHEF),
        chefBooklet.findMaximumOfCustomersByChef(VEGETERIAN_ILLNESS_CHEF),
        chefBooklet.findSalaryByChef(VEGETERIAN_ILLNESS_CHEF));
    Chef otherExpectedChef = chefFactory.create(NO_RESTRICTION_ILLNESS_CHEF,
        chefBooklet.findRestrictionsByChef(NO_RESTRICTION_ILLNESS_CHEF),
        chefBooklet.findMaximumOfCustomersByChef(NO_RESTRICTION_ILLNESS_CHEF),
        chefBooklet.findSalaryByChef(NO_RESTRICTION_ILLNESS_CHEF));
    Money expectedTotalPrice = expectedChef.getSalary().add(otherExpectedChef.getSalary());
    assertTrue(expectedTotalPrice.equals(computedTotalPrice));
  }

  private void setUpHoppeningDayWithEnoughRestrictionsToRequireTwoChefs() {
    List<Restriction> hoppeningDayRestrictions = Arrays.asList(
            new Restriction(VEGETARIAN_SPECIALTY, FIRST_RESTRICTION_PRICE),
            new Restriction(NO_RESTRICTION_SPECIALTY, SECOND_RESTRICTION_PRICE)
    );
    hoppeningDay.addRestriction(hoppeningDayRestrictions);
  }

  void setUpHoppeningDayReservationRequiringTwoSpecialtyThatOneChefCanHandleAlone() {
    restrictionsCount.put(FIRST_SPECIALTY, A_NUMBER_OF_RESTRICTION_INFERIOR_TO_HALF_MAX_CHEF_CUSTOMER);
    restrictionsCount.put(SECOND_SPECIALTY, A_NUMBER_OF_RESTRICTION_INFERIOR_TO_HALF_MAX_CHEF_CUSTOMER);
    Restriction firstRestriction = new Restriction(FIRST_SPECIALTY, FIRST_RESTRICTION_PRICE);
    Restriction secondRestriction = new Restriction(SECOND_SPECIALTY, SECOND_RESTRICTION_PRICE);

    List<Restriction> hoppeningDayRestrictions = Arrays.asList(firstRestriction, secondRestriction);
    hoppeningDay.addRestriction(hoppeningDayRestrictions);
  }

  void setUpHoppeningDayReservationRequiringOneSpecialtyThatOneChefCanHandleAlone() {
    restrictionsCount.put(SECOND_SPECIALTY, A_NUMBER_OF_RESTRICTION_INFERIOR_TO_HALF_MAX_CHEF_CUSTOMER);
    Restriction secondRestriction = new Restriction(SECOND_SPECIALTY, SECOND_RESTRICTION_PRICE);

    List<Restriction> hoppeningDayRestrictions = Collections.singletonList(secondRestriction);
    hoppeningDay.addRestriction(hoppeningDayRestrictions);
  }

  void setUpHoppeningDayReservationRequiringOneSpecialtyThatOneChefCannotHandleAlone() {
    restrictionsCount.put(SECOND_SPECIALTY, A_NUMBER_OF_RESTRICTION_SUPERIOR_TO_MAX_CHEF_CUSTOMER);
    Restriction secondRestriction = new Restriction(SECOND_SPECIALTY, SECOND_RESTRICTION_PRICE);

    List<Restriction> hoppeningDayRestrictions = Collections.singletonList(secondRestriction);
    hoppeningDay.addRestriction(hoppeningDayRestrictions);
  }

  private void givenAChefBooklet() {
    when(chefBooklet.getAllChefNames()).thenReturn(CHEF_NAMES);
    when(chefBooklet.findMaximumOfCustomersByChef(anyString())).thenReturn(Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS_PER_CHEF));
    when(chefBooklet.findSalaryByChef(anyString())).thenReturn(Optional.of(CHEF_SALARY));
    when(chefBooklet.findRestrictionsByChef("Thierry Aki")).thenReturn(Optional.of(List.of("no restriction")));
    when(chefBooklet.findRestrictionsByChef("Bob Smarties")).thenReturn(Optional.of(List.of("vegan")));
    when(chefBooklet.findRestrictionsByChef("Bob Rossbeef")).thenReturn(Optional.of(List.of("vegetarian")));
    when(chefBooklet.findRestrictionsByChef("Bill Adicion")).thenReturn(Optional.of(List.of("allergies")));
    when(chefBooklet.findRestrictionsByChef("Omar Calmar")).thenReturn(Optional.of(List.of("illness")));
    when(chefBooklet.findRestrictionsByChef("Amélie Mélo")).thenReturn(Optional.of(List.of("vegan", "allergies")));
    when(chefBooklet.findRestrictionsByChef("Écharlotte Cardin")).thenReturn(Optional.of(List.of("vegan", "allergies")));
    when(chefBooklet.findRestrictionsByChef("Éric Ardo")).thenReturn(Optional.of(List.of("vegetarian", "illness")));
    when(chefBooklet.findRestrictionsByChef("Hans Riz")).thenReturn(Optional.of(List.of("no restriction", "illness")));
  }
}