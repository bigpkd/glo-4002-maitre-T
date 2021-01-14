package ca.ulaval.glo4002.reservation.reservation.domain;

import ca.ulaval.glo4002.reservation.utils.Money;
import java.util.ArrayList;
import java.util.Optional;

public class Chef {
  private String name;
  private ArrayList<String> restrictionNames;
  private Money salary;
  private int maximumNumberOfCustomersServed;
  private int currentlyServedCustomers;

  public Chef(String name, ArrayList<String> restrictionNames, Money salary, int maximumNumberOfCustomersServed) {
    this.name = name;
    this.restrictionNames = restrictionNames;
    this.salary = salary;
    this.maximumNumberOfCustomersServed = maximumNumberOfCustomersServed;
    this.currentlyServedCustomers = 0;
  }

  public String getName() {
    return name;
  }

  public boolean canCookForRestriction(String restrictionName) {
    return restrictionNames.stream().anyMatch(restrictionName::equals);
  }

  public Optional<String> getDifferentRestriction(String restrictionName) {
    for (String restriction : restrictionNames) {
      if (!restriction.equals(restrictionName)) {
        return Optional.of(restriction);
      }
    }

    return Optional.empty();
  }

  public void setCurrentlyServedCustomers(int number) {
    currentlyServedCustomers = number;
  }

  public int getAvailableCustomersCount() {
    return maximumNumberOfCustomersServed - currentlyServedCustomers;
  }

  public int getCurrentlyServedCustomers() {
    return currentlyServedCustomers;
  }

  public Money getSalary() {
    return salary;
  }

  public int getMaximumNumberOfCustomersServed() {
    return maximumNumberOfCustomersServed;
  }

  public boolean canCookForAnAdditionalCustomer() {
    return getAvailableCustomersCount() > 0;
  }
}
