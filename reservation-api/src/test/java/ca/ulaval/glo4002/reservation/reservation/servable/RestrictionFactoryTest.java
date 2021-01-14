package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestrictionFactoryTest {
  private static final String VEGAN_RESTRICTION = "vegan";
  private static final String ALLERGY_RESTRICTION = "allergies";
  private static final String VEGETARIAN_RESTRICTION = "vegetarian";
  private static final String ILLNESS_RESTRICTION = "illness";

  private static final double VEGAN_AMOUNT = 1000.00;
  private static final double ALLERGY_AMOUNT = 0.00;
  private static final double VEGETARIAN_AMOUNT = 500.00;
  private static final double ILLNESS_AMOUNT = 0.00;

  private static final Money VEGAN_PRICE = new Money(VEGAN_AMOUNT);
  private static final Money ALLERGY_PRICE = new Money(ALLERGY_AMOUNT);
  private static final Money VEGETARIAN_PRICE = new Money(VEGETARIAN_AMOUNT);
  private static final Money ILLNESS_PRICE = new Money(ILLNESS_AMOUNT);

  private RestrictionFactory restrictionFactory;

  @BeforeEach
  void setup() {
    restrictionFactory = new RestrictionFactory();
  }

  @Test
  void givenRestrictionVegan_whenCreatingRestriction_thenCorrespondingRestrictionIsCreated() {
    Restriction restriction = restrictionFactory.create(VEGAN_RESTRICTION, Optional.of(VEGAN_AMOUNT));

    assertEquals(VEGAN_RESTRICTION, restriction.getName());
    assertTrue(VEGAN_PRICE.equals(restriction.getPrice()));
  }

  @Test
  void givenRestrictionAllergies_whenCreatingRestriction_thenCorrespondingRestrictionIsCreated() {
    Restriction restriction = restrictionFactory.create(ALLERGY_RESTRICTION, Optional.of(ALLERGY_AMOUNT));

    assertEquals(ALLERGY_RESTRICTION, restriction.getName());
    assertTrue(ALLERGY_PRICE.equals(restriction.getPrice()));
  }

  @Test
  void givenRestrictionVegetarian_whenCreatingRestriction_thenCorrespondingRestrictionIsCreated() {
    Restriction restriction = restrictionFactory.create(VEGETARIAN_RESTRICTION, Optional.of(VEGETARIAN_AMOUNT));

    assertEquals(VEGETARIAN_RESTRICTION, restriction.getName());
    assertTrue(VEGETARIAN_PRICE.equals(restriction.getPrice()));
  }
  @Test
  void givenRestrictionIllness_whenCreatingRestriction_thenCorrespondingRestrictionIsCreated() {
    Restriction restriction = restrictionFactory.create(ILLNESS_RESTRICTION, Optional.of(ILLNESS_AMOUNT));

    assertEquals(ILLNESS_RESTRICTION, restriction.getName());
    assertTrue(ILLNESS_PRICE.equals(restriction.getPrice()));
  }
}