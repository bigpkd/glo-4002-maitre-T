package ca.ulaval.glo4002.reservation.reservation.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.utils.Money;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableTest {
  private static final Money TWO_DOLLAR = new Money(2);
  private static final Money ONE_DOLLAR = new Money(1);
  private static final Money ZERO_DOLLAR = new Money(0);
  private static final String RESTRICTION_NAME = "RESTRICTION_NAME";

  private Customer first_customer;
  private Customer second_customer;

  @BeforeEach
  void setUp() {
    first_customer = mock(Customer.class);
    second_customer = mock(Customer.class);
  }

  @Test
  void givenTableWithNoCustomers_whenGettingTablePrice_thenTablePriceTotalIsZero() {
    List<Customer> customers = new ArrayList<>();

    Table table = new Table(customers);

    assertTrue(table.getTablePrice().equals(ZERO_DOLLAR));
  }

  @Test
  void givenATableWithCustomers_whenGettingTablePrice_thenTablePriceIsSumOfCustomerBills() {
    Customer first_customer = mock(Customer.class);
    Customer second_customer = mock(Customer.class);

    when(first_customer.getCustomerPrice()).thenReturn(ONE_DOLLAR);
    when(second_customer.getCustomerPrice()).thenReturn(ONE_DOLLAR);
    List<Customer> customers = new ArrayList<>(Arrays.asList(first_customer, second_customer));

    Table table = new Table(customers);

    verify(first_customer).getCustomerPrice();
    assertTrue(table.getTablePrice().equals(TWO_DOLLAR));
  }

  @Test
  void givenATableWithRestrictions_whenValidatingIfContainsAnExistingRestriction_thenCustomersShouldVerifyIfHaveRestrictions() {
    when(first_customer.getCustomerPrice()).thenReturn(ONE_DOLLAR);
    when(second_customer.getCustomerPrice()).thenReturn(ONE_DOLLAR);
    List<Customer> customers = new ArrayList<>(Arrays.asList(first_customer, second_customer));
    Table table = new Table(customers);

    table.containsRestriction(RESTRICTION_NAME);

    verify(first_customer).hasRestriction(RESTRICTION_NAME);
    verify(second_customer).hasRestriction(RESTRICTION_NAME);
  }
}