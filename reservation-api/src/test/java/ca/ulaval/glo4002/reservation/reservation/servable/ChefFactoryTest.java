package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.Chef;
import ca.ulaval.glo4002.reservation.reservation.repository.exceptions.ChefNotFoundException;
import ca.ulaval.glo4002.reservation.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

class ChefFactoryTest {
  private static final int MAXIMUM_NUMBER_OF_CUSTOMERS = 5;
  private static final Money CHEF_SALARY = new Money(6000.00);
  private static final String CHEF_THIERRY_AKI = "Thierry Aki";
  private static final String CHEF_BOB_SMARTIES = "Bob Smarties";
  private static final String CHEF_BOB_ROSSBEEF = "Bob Rossbeef";
  private static final String CHEF_BILL_ADICION = "Bill Adicion";
  private static final String CHEF_OMAR_CALMAR = "Omar Calmar";
  private static final String CHEF_AMELIE_MELO = "Amélie Mélo";
  private static final String CHEF_ECHARLOTTE_CARDIN = "Écharlotte Cardin";
  private static final String CHEF_ERIC_ARDO = "Éric Ardo";
  private static final String CHEF_HANS_RIZ = "Hans Riz";
  private static final String VEGAN_RESTRICTION = "vegan";
  private static final String ALLERGY_RESTRICTION = "allergies";
  private static final String EMPTY_RESTRICTION = "no restriction";
  private static final String VEGETARIAN_RESTRICTION = "vegetarian";
  private static final String ILLNESS_RESTRICTION = "illness";
  public static final Map<String, List<String>> RESTRICTIONS_PER_CHEF = Map.ofEntries(
      entry(CHEF_THIERRY_AKI, List.of(EMPTY_RESTRICTION)),
      entry(CHEF_BOB_SMARTIES, List.of(VEGAN_RESTRICTION)),
      entry(CHEF_BOB_ROSSBEEF, List.of(VEGETARIAN_RESTRICTION)),
      entry(CHEF_BILL_ADICION, List.of(ALLERGY_RESTRICTION)),
      entry(CHEF_OMAR_CALMAR, List.of(ILLNESS_RESTRICTION)),
      entry(CHEF_AMELIE_MELO, List.of(VEGAN_RESTRICTION, ALLERGY_RESTRICTION)),
      entry(CHEF_ECHARLOTTE_CARDIN, List.of(VEGAN_RESTRICTION, ALLERGY_RESTRICTION)),
      entry(CHEF_ERIC_ARDO, List.of(VEGETARIAN_RESTRICTION, ILLNESS_RESTRICTION)),
      entry(CHEF_HANS_RIZ, List.of(EMPTY_RESTRICTION, ILLNESS_RESTRICTION))
  );

  private ChefFactory chefFactory;

  @BeforeEach
  void setup() {
    chefFactory = new ChefFactory();
  }

  @Test
  void givenChefThierryAki_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_THIERRY_AKI, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_THIERRY_AKI)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_THIERRY_AKI, chef.getName());
    assertTrue(chef.canCookForRestriction(EMPTY_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefBobSmarties_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_BOB_SMARTIES, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_BOB_SMARTIES)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_BOB_SMARTIES, chef.getName());
    assertTrue(chef.canCookForRestriction(VEGAN_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefBobRossbeef_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_BOB_ROSSBEEF, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_BOB_ROSSBEEF)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_BOB_ROSSBEEF, chef.getName());
    assertTrue(chef.canCookForRestriction(VEGETARIAN_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefBillAdicion_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_BILL_ADICION, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_BILL_ADICION)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_BILL_ADICION, chef.getName());
    assertTrue(chef.canCookForRestriction(ALLERGY_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefOmarCalmar_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_OMAR_CALMAR, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_OMAR_CALMAR)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_OMAR_CALMAR, chef.getName());
    assertTrue(chef.canCookForRestriction(ILLNESS_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefAmelieMelo_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_AMELIE_MELO, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_AMELIE_MELO)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_AMELIE_MELO, chef.getName());
    assertTrue(chef.canCookForRestriction(VEGAN_RESTRICTION));
    assertTrue(chef.canCookForRestriction(ALLERGY_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefEchalotteCardin_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_ECHARLOTTE_CARDIN, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_ECHARLOTTE_CARDIN)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_ECHARLOTTE_CARDIN, chef.getName());
    assertTrue(chef.canCookForRestriction(VEGAN_RESTRICTION));
    assertTrue(chef.canCookForRestriction(ALLERGY_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefEricArdo_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_ERIC_ARDO, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_ERIC_ARDO)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_ERIC_ARDO, chef.getName());
    assertTrue(chef.canCookForRestriction(VEGETARIAN_RESTRICTION));
    assertTrue(chef.canCookForRestriction(ILLNESS_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenChefHansRiz_whenCreatingChef_thenCorrespondingChefCreated() {
    Chef chef = chefFactory.create(CHEF_HANS_RIZ, Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_HANS_RIZ)), Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS), Optional.of(CHEF_SALARY));

    assertEquals(CHEF_HANS_RIZ, chef.getName());
    assertTrue(chef.canCookForRestriction(EMPTY_RESTRICTION));
    assertTrue(chef.canCookForRestriction(ILLNESS_RESTRICTION));
    assertTrue(CHEF_SALARY.equals(chef.getSalary()));
    assertEquals(MAXIMUM_NUMBER_OF_CUSTOMERS, chef.getMaximumNumberOfCustomersServed());
  }

  @Test
  void givenNonexistentChefRestrictionsList_whenCreatingChef_thenChefNotFoundExceptionIsThrown() {
    assertThrows(ChefNotFoundException.class, () -> chefFactory.create(CHEF_HANS_RIZ,
        Optional.empty(),
        Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS),
        Optional.of(CHEF_SALARY)));
  }

  @Test
  void givenNonexistentChefMaximumNumberOfCustomers_whenCreatingChef_thenChefNotFoundExceptionIsThrown() {
    assertThrows(ChefNotFoundException.class, () -> chefFactory.create(CHEF_HANS_RIZ,
        Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_HANS_RIZ)),
        Optional.empty(),
        Optional.of(CHEF_SALARY)));
  }

  @Test
  void givenNonexistentChefSalary_whenCreatingChef_thenChefNotFoundExceptionIsThrown() {
    assertThrows(ChefNotFoundException.class, () -> chefFactory.create(CHEF_HANS_RIZ,
        Optional.of(RESTRICTIONS_PER_CHEF.get(CHEF_HANS_RIZ)),
        Optional.of(MAXIMUM_NUMBER_OF_CUSTOMERS),
        Optional.empty()));
  }
}