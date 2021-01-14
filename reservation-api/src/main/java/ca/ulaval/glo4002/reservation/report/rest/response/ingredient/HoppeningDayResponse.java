package ca.ulaval.glo4002.reservation.report.rest.response.ingredient;

import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportItem;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HoppeningDayResponse {
  public LocalDate date;
  public List<IngredientReportItemResponse> ingredients;
  public BigDecimal totalPrice;

  public HoppeningDayResponse(HoppeningDay hoppeningDay) {
    this.date = hoppeningDay.getDate();
    Collection<IngredientReportItem> ingredientReportItems = hoppeningDay.getIngredientReportRegister()
        .getIngredientReportItems().values();

    this.ingredients = ingredientReportItems.stream()
        .map(IngredientReportItemResponse::new)
        .sorted(Comparator.comparing(response -> response.name))
        .collect(Collectors.toList());

    this.totalPrice = hoppeningDay.calculateIngredientReportTotal().getAmount();
  }
}
