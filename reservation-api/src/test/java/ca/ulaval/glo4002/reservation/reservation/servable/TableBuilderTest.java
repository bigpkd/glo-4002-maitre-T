package ca.ulaval.glo4002.reservation.reservation.servable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooManyPeopleException;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.TableDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableBuilderTest {
  private final static String CUSTOMER_NAME = "name";

  private final static List<Restriction> EMPTY_CUSTOMER_RESTRICTIONS = new ArrayList<>();
  private final static List<CustomerDto> EMPTY_CUSTOMER_DTO_LIST = new ArrayList<>();
  private static final int MAXIMUM_NUMBER_OF_CUSTOMER_PER_TABLE = 4;

  private final static Customer CUSTOMER_WITHOUT_RESTRICTIONS =
      new Customer(CUSTOMER_NAME, EMPTY_CUSTOMER_RESTRICTIONS, new ArrayList<>());

  private TableBuilder tableBuilder;
  private TableDto tableDto;
  private CustomerFactory customerFactory;

  @BeforeEach
  void setUp() {
    customerFactory = mock(CustomerFactory.class);
    tableBuilder = new TableBuilder(customerFactory);
    tableDto = mock(TableDto.class);
  }

  @Test
  void givenTableFactory_whenCreatingTable_thenCustomersAreMappedIntoTable() {
    CustomerDto customerDto = mock(CustomerDto.class);
    List<CustomerDto> customerDtoList = new ArrayList<>();
    customerDtoList.add(customerDto);
    tableDto.customers = customerDtoList;
    when(customerFactory.create(customerDto)).thenReturn(CUSTOMER_WITHOUT_RESTRICTIONS);

    Table table = tableBuilder.create(tableDto);

    assertTrue(table.getCustomers().contains(CUSTOMER_WITHOUT_RESTRICTIONS));
  }

  @Test
  void givenTableFactory_whenCreatingTableWithNoCustomers_thenInvalidReservationQuantityExceptionIsThrown() {
    tableDto.customers = EMPTY_CUSTOMER_DTO_LIST;

    assertThrows(InvalidReservationQuantityException.class, () -> tableBuilder.create(tableDto));
  }

  @Test
  void whenCreatingTableWithTooManyCustomers_thenTooManyPeopleExceptionIsThrown() {
    List<CustomerDto> customersDto = setUpTooManyCustomersList(MAXIMUM_NUMBER_OF_CUSTOMER_PER_TABLE);
    TableDto tableWithTooManyCustomer = new TableDto(customersDto);

    assertThrows(TooManyPeopleException.class, () -> tableBuilder.create(tableWithTooManyCustomer));
  }

  private List<CustomerDto> setUpTooManyCustomersList(int maxNumberOfCustomer){
    CustomerDto customerDto = mock(CustomerDto.class);
    when(customerFactory.create(customerDto)).thenReturn(CUSTOMER_WITHOUT_RESTRICTIONS);
    List<CustomerDto> customersDto = new ArrayList<>();
    for (int i = 0; i  <= maxNumberOfCustomer; i++){
      customersDto.add(customerDto);
    }
    return customersDto;
  }

}