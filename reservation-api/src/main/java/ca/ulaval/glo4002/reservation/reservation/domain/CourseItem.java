package ca.ulaval.glo4002.reservation.reservation.domain;

import java.math.BigDecimal;

public class CourseItem {
  private String ingredientName;
  private BigDecimal weightInKg;

  public CourseItem(String ingredientName, double weight) {
    this.ingredientName = ingredientName;
    this.weightInKg = BigDecimal.valueOf(weight);
  }

  public String getIngredientName() {
    return ingredientName;
  }

  public BigDecimal getWeightInKg() {
    return weightInKg;
  }

}
