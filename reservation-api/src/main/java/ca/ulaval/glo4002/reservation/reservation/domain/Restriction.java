package ca.ulaval.glo4002.reservation.reservation.domain;

import ca.ulaval.glo4002.reservation.utils.Money;

public class Restriction {
  private String name;
  private Money price;
  private static final String NAME_FOR_DUMMY_RESTRICTION = "no restriction" ;

  public Restriction(String name, Money price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public Money getPrice() {
    return price;
  }

  public boolean isAnActualRestriction() {
    return !this.name.equalsIgnoreCase(NAME_FOR_DUMMY_RESTRICTION);
  }
}

