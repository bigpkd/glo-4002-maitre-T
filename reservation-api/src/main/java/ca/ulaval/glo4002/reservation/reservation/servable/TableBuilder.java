package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooManyPeopleException;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.reservation.rest.dto.TableDto;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class TableBuilder {
  private CustomerFactory customerFactory;
  private int maximumCustomersPerTable = 4;

  @Inject
  public TableBuilder(CustomerFactory customerFactory) {
    this.customerFactory = customerFactory;
  }

  public Table create(TableDto tableDto) {
    List<CustomerDto> customersDto = tableDto.customers;
    List<Customer> customers = createCustomers(customersDto);
    validateCustomersPerTable(customers.size());
    return new Table(customers);
  }

  private List<Customer> createCustomers(List<CustomerDto> customerDtoList) {
    if (customerDtoList.isEmpty()) {
      throw new InvalidReservationQuantityException();
    }

    List<Customer> customers = new ArrayList<>();
    for (CustomerDto customerDto : customerDtoList) {
      customers.add(customerFactory.create(customerDto));
    }

    return customers;
  }

  private void validateCustomersPerTable(int numberOfCustomer) {
    if (numberOfCustomer > maximumCustomersPerTable) {
      throw new TooManyPeopleException();
    }
  }
}
