package ca.ulaval.glo4002.reservation.ingredient.domain;

import ca.ulaval.glo4002.reservation.utils.Money;

public class Ingredient {
  private String name;
  private Money pricePerKg;

  public Ingredient(String name, Money pricePerKg) {
    this.name = name;
    this.pricePerKg = pricePerKg;
  }

  public String getName() {
    return name;
  }

  public Money getPricePerKg() {
    return pricePerKg;
  }
}
