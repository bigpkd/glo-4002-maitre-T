package ca.ulaval.glo4002.reservation.report.rest.response.ingredient;

import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportItem;
import java.math.BigDecimal;

public class IngredientReportItemResponse {
  public String name;
  public BigDecimal totalPrice;
  public BigDecimal quantity;

  public IngredientReportItemResponse(IngredientReportItem ingredientReportItem) {
    this.name = ingredientReportItem.getIngredientName();
    this.quantity = ingredientReportItem.getQuantity();
    this.totalPrice = ingredientReportItem.getTotalPrice().getAmount();
  }
}
