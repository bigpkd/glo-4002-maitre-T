package ca.ulaval.glo4002.reservation.reservation.domain;

import ca.ulaval.glo4002.reservation.utils.Money;
import java.util.List;

public class Table {
  private List<Customer> customers;
  private Money tablePrice;

  public Table(List<Customer> customers) {
    this.customers = customers;
    this.tablePrice = this.calculateTablePrice();
  }

  public List<Customer> getCustomers() {
    return customers;
  }

  public Money getTablePrice() {
    return tablePrice;
  }

  private Money calculateTablePrice() {
    Money tablePrice = new Money(0);

    for (Customer customer : customers) {
      tablePrice = tablePrice.add(customer.getCustomerPrice());
    }

    return tablePrice;
  }

  public int countCustomers() {
    return customers.size();
  }

  public boolean containsRestriction(String restrictionName) {
    for (Customer customer : customers) {
      if (customer.hasRestriction(restrictionName)) {
        return true;
      }
    }

    return false;
  }
}
