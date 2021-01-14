package ca.ulaval.glo4002.reservation.reservation.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.utils.Money;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationTest {
  private static final String VENDOR_CODE = "TEAM";
  private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
  private static final String INGREDIENT_NAME = "INGREDIENT_NAME";

  private static final String RESTRICTION_NAME = "RESTRICTION_NAME";
  private static final String SECOND_RESTRICTION_NAME = "SECOND_RESTRICTION_NAME";
  private static final Money RESTRICTION_PRICE = new Money(1.0);
  private static final Money SECOND_RESTRICTION_PRICE = new Money(15.2);

  private static final Restriction FIRST_RESTRICTION = new Restriction(RESTRICTION_NAME, RESTRICTION_PRICE);
  private static final Restriction SECOND_RESTRICTION = new Restriction(SECOND_RESTRICTION_NAME, SECOND_RESTRICTION_PRICE);

  private static final Customer FIRST_CUSTOMER = new Customer(CUSTOMER_NAME, Arrays.asList(FIRST_RESTRICTION), new ArrayList<>());
  private static final Customer SECOND_CUSTOMER = new Customer(CUSTOMER_NAME, Arrays.asList(SECOND_RESTRICTION), new ArrayList<>());

  private static final Country COUNTRY = new Country("CAN", "Canada", "$CAN");

  private static final OffsetDateTime RESERVATION_DINNER_DATE =
      OffsetDateTime.of(2150, 7, 21, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime RESERVATION_DATE =
      OffsetDateTime.of(2150, 4, 21, 15, 23, 20, 0, ZoneOffset.UTC);

  private static final Money THREE_DOLLAR = new Money(3);
  private static final Money TWO_DOLLAR = new Money(2);
  private static final Money ONE_DOLLAR = new Money(1);
  private static final Money ZERO_DOLLAR = new Money(0);
  private static final Money ANY_AMOUNT = new Money(0);

  private static final int A_NUMBER_OF_CUSTOMERS = 155;
  private static final int ANOTHER_NUMBER_OF_CUSTOMERS = 26;

  private List<Table> tables;

  private Table table;
  private Table first_table;
  private Table second_table;
  private Customer customer;
  private Customer anotherCustomer;

  @BeforeEach
  public void setUp() {
    tables = new ArrayList<>();
    table = mock(Table.class);
    first_table = mock(Table.class);
    second_table = mock(Table.class);
    customer = mock(Customer.class);
    anotherCustomer = mock(Customer.class);
    when(first_table.getTablePrice()).thenReturn(ONE_DOLLAR);
    when(second_table.getTablePrice()).thenReturn(ONE_DOLLAR);
  }

  @Test
  void givenReservationWithNoTables_whenCreatingReservation_thenReservationPriceIsPriceOfNoRestrictions() {
    Reservation reservation = new Reservation(VENDOR_CODE, RESERVATION_DINNER_DATE, RESERVATION_DATE, COUNTRY, tables);

    assertTrue(reservation.getReservationPrice().equals(ZERO_DOLLAR));
  }

  @Test
  void givenReservation_whenCreatingReservationWithTables_thenReservationPriceIsSumOfTablesPrices() {
    tables.add(first_table);
    tables.add(second_table);
    when(first_table.getTablePrice()).thenReturn(ONE_DOLLAR);
    when(second_table.getTablePrice()).thenReturn(TWO_DOLLAR);

    Reservation reservation = new Reservation(VENDOR_CODE, RESERVATION_DINNER_DATE, RESERVATION_DATE, COUNTRY, tables);

    verify(first_table).getTablePrice();
    assertTrue(reservation.getReservationPrice().equals(THREE_DOLLAR));
  }

  @Test
  void givenReservation_whenGettingCustomers_thenCustomersFromAllTablesAreObtained() {
    List<Customer> customers = Arrays.asList(customer);
    List<Customer> otherCustomers = Arrays.asList(anotherCustomer);
    tables = Arrays.asList(first_table, second_table);
    when(first_table.getCustomers()).thenReturn(customers);
    when(second_table.getCustomers()).thenReturn(otherCustomers);
    Reservation reservation = new Reservation(VENDOR_CODE, RESERVATION_DINNER_DATE, RESERVATION_DATE, COUNTRY, tables);

    List<Customer> reservationCustomers = reservation.getCustomers();

    verify(first_table).getCustomers();
    int expectedSize = customers.size() + otherCustomers.size();
    assertEquals(expectedSize, reservationCustomers.size());
  }

  @Test
  void givenReservation_whenCountingCustomers_thenTotalNumberOfCustomersIsObtained() {
    tables = Arrays.asList(first_table, second_table);
    when(first_table.countCustomers()).thenReturn(A_NUMBER_OF_CUSTOMERS);
    when(second_table.countCustomers()).thenReturn(ANOTHER_NUMBER_OF_CUSTOMERS);
    Reservation reservation = new Reservation(VENDOR_CODE, RESERVATION_DINNER_DATE, RESERVATION_DATE, COUNTRY, tables);

    int customersCount = reservation.countCustomers();

    int expectedCustomersCount = A_NUMBER_OF_CUSTOMERS + ANOTHER_NUMBER_OF_CUSTOMERS;
    assertEquals(expectedCustomersCount, customersCount);
  }

  @Test
  void givenAReservationWithRestrictions_whenValidatingIfContainsAnExistingRestriction_thenTablesShouldVerifyIfContainRestrictions() {
    tables = Arrays.asList(first_table, second_table);
    Reservation reservation = new Reservation(VENDOR_CODE, RESERVATION_DINNER_DATE, RESERVATION_DATE, COUNTRY, tables);

    reservation.containsRestriction(RESTRICTION_NAME);

    verify(first_table).containsRestriction(RESTRICTION_NAME);
    verify(second_table).containsRestriction(RESTRICTION_NAME);
  }

  @Test
  void givenAReservationWithRestrictions_whenObtainingRestrictions_thenShouldConcatAllCustomerRestrictions(){
    List<Customer> firstTableCustomers = Arrays.asList(FIRST_CUSTOMER, SECOND_CUSTOMER);
    List<Customer> secondTableCustomers = Arrays.asList(SECOND_CUSTOMER);
    when(first_table.getCustomers()).thenReturn(firstTableCustomers);
    when(second_table.getCustomers()).thenReturn(secondTableCustomers);
    tables = Arrays.asList(first_table, second_table);
    Reservation reservation = new Reservation(VENDOR_CODE, RESERVATION_DINNER_DATE, RESERVATION_DATE, COUNTRY, tables);
    List<Restriction> expectedRestrictions = Arrays.asList(FIRST_RESTRICTION, SECOND_RESTRICTION, SECOND_RESTRICTION);

    List<Restriction> restrictions = reservation.getRestrictions();

    assertEquals(expectedRestrictions, restrictions);
  }

  @Test
  void givenAReservationWithCustomers_whenValidatingIfContainsIngredient_thenCustomersShouldVerifyIfContainIngredient() {
    List<Customer> tableCustomers = Arrays.asList(customer, anotherCustomer);
    when(table.getCustomers()).thenReturn(tableCustomers);
    when(table.getTablePrice()).thenReturn(ANY_AMOUNT);
    tables = Arrays.asList(table);
    when(first_table.getCustomers()).thenReturn(tableCustomers);
    Reservation reservation = new Reservation(VENDOR_CODE, RESERVATION_DINNER_DATE, RESERVATION_DATE, COUNTRY, tables);

    reservation.containsIngredient(INGREDIENT_NAME);

    verify(customer).hasIngredient(INGREDIENT_NAME);
    verify(anotherCustomer).hasIngredient(INGREDIENT_NAME);
  }
}