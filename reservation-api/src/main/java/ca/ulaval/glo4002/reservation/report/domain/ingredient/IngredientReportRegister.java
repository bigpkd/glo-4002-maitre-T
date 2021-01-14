package ca.ulaval.glo4002.reservation.report.domain.ingredient;

import ca.ulaval.glo4002.reservation.reservation.domain.CourseItem;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class IngredientReportRegister {

  private HashMap<String, IngredientReportItem> ingredientReportItems = new HashMap<>();

  public void addToRegister(HashMap<CourseItem, Money> pricePerCourseItems) {
    for (Map.Entry<CourseItem, Money> entry : pricePerCourseItems.entrySet()) {
      CourseItem courseItem = entry.getKey();
      Money pricePerKg = entry.getValue();

      String ingredientName = courseItem.getIngredientName();
      BigDecimal quantity = courseItem.getWeightInKg();
      Money totalPrice = pricePerKg.multiply(quantity);
      setIngredientReportItem(ingredientName, quantity, totalPrice);
    }
  }

  private void setIngredientReportItem(String ingredientName, BigDecimal quantity, Money totalPrice) {
    IngredientReportItem ingredientReportItem = ingredientReportItems.get(ingredientName);

    if (ingredientReportItem == null) {
      ingredientReportItem = new IngredientReportItem(ingredientName, quantity, totalPrice);
    } else {
      ingredientReportItem.incrementQuantity(quantity);
      ingredientReportItem.incrementTotalPrice(totalPrice);
    }

    ingredientReportItems.put(ingredientName, ingredientReportItem);
  }

  public Money calculateIngredientReportTotal() {
    Money total = new Money(0);

    for (Map.Entry<String, IngredientReportItem> entry : ingredientReportItems.entrySet()) {
      IngredientReportItem ingredientReportItem = entry.getValue();
      total = total.add(ingredientReportItem.getTotalPrice());
    }

    return total;
  }

  public HashMap<String, IngredientReportItem> getIngredientReportItems() {
    return ingredientReportItems;
  }

  public boolean isEmpty() {
    return ingredientReportItems.isEmpty();
  }

  public boolean containsIngredient(String ingredientName) {
    return ingredientReportItems.containsKey(ingredientName);
  }
}
