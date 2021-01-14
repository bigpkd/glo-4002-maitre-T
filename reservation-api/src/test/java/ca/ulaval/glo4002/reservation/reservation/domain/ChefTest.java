package ca.ulaval.glo4002.reservation.reservation.domain;

import static org.junit.jupiter.api.Assertions.*;


import ca.ulaval.glo4002.reservation.utils.Money;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChefTest {
  private static final int DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS = 5;

  private static final Money DEFAULT_SALARY = new Money(6000.00);
  private static final String CHEF_NAME = "Lorem Ipsum";
  private static final String A_RESTRICTION_NAME = "vegan";
  private static final String A_DIFFERENT_RESTRICTION_NAME = "allergies";
  private static final String KNOWN_RESTRICTION_NAME = "allergies";
  private static final String UNKNOWN_RESTRICTION_NAME = "BAD";
  private static final int A_NUMBER = 4;
  private static final int SAME_NUMBER = 4;
  private static final int EXPECTED_AVAILABLE_COUNT = 1;
  private static final int INITIAL_VALUE = 0;

  private ArrayList<String> restrictionNames;
  private Chef chef;

  @BeforeEach
  void setUp() {
    restrictionNames = new ArrayList<>();
    restrictionNames.add(A_RESTRICTION_NAME);
    restrictionNames.add(KNOWN_RESTRICTION_NAME);
    chef = new Chef(CHEF_NAME, restrictionNames, DEFAULT_SALARY, DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS);
  }

  @Test
  void givenANewChef_afterCreated_thenCurentlyServedCustomerIsInitialValue() {
    assertEquals(chef.getCurrentlyServedCustomers(), INITIAL_VALUE);
  }

  @Test
  void givenRestrictionChefCannotCookFor_whenAskingIfChefCanCookForSaidRestriction_thenFalseIsReturned() {
    boolean canChefCookThis = chef.canCookForRestriction(UNKNOWN_RESTRICTION_NAME);

    assertFalse(canChefCookThis);
  }

  @Test
  void givenRestrictionChefCannotCookFor_whenAskingIfChefCanCookForSaidRestriction_thenTrueIsReturned() {
    boolean canChefCookThis = chef.canCookForRestriction(KNOWN_RESTRICTION_NAME);

    assertTrue(canChefCookThis);
  }

  @Test
  void givenAChef_whenSettingCurentlyServedCustomer_thenCountIsUpdated() {
    chef.setCurrentlyServedCustomers(A_NUMBER);

    assertEquals(chef.getCurrentlyServedCustomers(), SAME_NUMBER);
  }

  @Test
  void givenAChefWithMultipleRestrictions_whenAskingForDifferentRestriction_thenOtherRestrictionIsReturned() {
    Optional<String> differentRestriction = chef.getDifferentRestriction(A_RESTRICTION_NAME);

    assertEquals(differentRestriction.get(), A_DIFFERENT_RESTRICTION_NAME);
  }

  @Test
  void givenAChefWithOnlyOneRestriction_whenAskingForDifferentRestriction_thenNoRestrictionIsReturned() {
    restrictionNames = new ArrayList<>();
    restrictionNames.add(A_RESTRICTION_NAME);
    Chef chefWithOneRestriction = new Chef(CHEF_NAME, restrictionNames, DEFAULT_SALARY, DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS);
    Optional<String> differentRestriction = chefWithOneRestriction.getDifferentRestriction(A_RESTRICTION_NAME);

    assertTrue(differentRestriction.isEmpty());
  }

  @Test
  void givenAChefWithCurrentlyServedCustomers_whenAskingforAvailableCustomerCount_thenAvailableCountIsReturned() {
    chef.setCurrentlyServedCustomers(A_NUMBER);

    int availableCount = chef.getAvailableCustomersCount();

    assertEquals(availableCount, EXPECTED_AVAILABLE_COUNT);
  }

  @Test
  void givenAChefWithoutCurrentlyServedCustomers_whenAskingforAvailableCustomerCount_thenMaximumNumberAvailableIsReturned() {
    int availableCount = chef.getAvailableCustomersCount();

    assertEquals(availableCount, DEFAULT_MAXIMUM_NUMBER_OF_CUSTOMERS);
  }

}