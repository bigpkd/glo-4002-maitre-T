package ca.ulaval.glo4002.reservation.report.domain.ingredient;

import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.BigDecimal;

public class IngredientReportItem implements Cloneable {
  private String ingredientName;
  private BigDecimal quantity;
  private Money totalPrice;

  public IngredientReportItem(String ingredientName, BigDecimal quantity, Money totalPrice) {
    this.ingredientName = ingredientName;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  @Override
  public Object clone() {
    return new IngredientReportItem(this.ingredientName, this.quantity, this.totalPrice);
  }

  public String getIngredientName() {
    return ingredientName;
  }

  public void incrementQuantity(BigDecimal quantity) {
    this.quantity = this.quantity.add(quantity);
  }

  public void incrementTotalPrice(Money totalPrice) {
    this.totalPrice = this.totalPrice.add(totalPrice);
  }

  public Money getTotalPrice() {
    return this.totalPrice;
  }

  public BigDecimal getQuantity() {
    return this.quantity;
  }

  public IngredientReportItem combineQuantityAndPrice(IngredientReportItem otherIngredientReportItem) {
    this.incrementQuantity(otherIngredientReportItem.getQuantity());
    this.incrementTotalPrice(otherIngredientReportItem.getTotalPrice());

    return this;
  }
}
