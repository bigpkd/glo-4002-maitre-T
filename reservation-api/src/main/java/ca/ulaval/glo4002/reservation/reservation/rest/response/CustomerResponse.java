package ca.ulaval.glo4002.reservation.reservation.rest.response;

import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import java.util.List;

public class CustomerResponse {
  public String name;
  public List<String> restrictions;

  public CustomerResponse(Customer customer) {
    this.name = customer.getName();
    this.restrictions = customer.getRestrictionNames();
  }
}
