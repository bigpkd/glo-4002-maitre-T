package ca.ulaval.glo4002.reservation.report.rest.response.ingredient;

import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportItem;
import ca.ulaval.glo4002.reservation.report.domain.ingredient.IngredientReportRegister;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientReportTotalResponse implements IngredientReportResponse {
  public List<IngredientReportItemResponse> ingredients;
  public BigDecimal totalPrice;

  public IngredientReportTotalResponse(List<HoppeningDay> dates) {
    super();

    Money totalPriceForAllDays = new Money(0);
    HashMap<String, IngredientReportItem> totalIngredientsForAllDays = new HashMap<>();

    for (HoppeningDay hoppeningDay : dates) {
      IngredientReportRegister ingredientReportRegister =
          hoppeningDay.getIngredientReportRegister();
      Collection<IngredientReportItem>
          temporaryIngredientReportItems =
          ingredientReportRegister.getIngredientReportItems().values()
              .stream()
              .map(e -> (IngredientReportItem) e.clone())
              .collect(Collectors.toList());

      for (IngredientReportItem ingredientReportItem : temporaryIngredientReportItems) {
        totalIngredientsForAllDays
            .merge(ingredientReportItem.getIngredientName(), ingredientReportItem,
                IngredientReportItem::combineQuantityAndPrice);
        totalPriceForAllDays = totalPriceForAllDays.add(ingredientReportItem.getTotalPrice());
      }
    }

    this.ingredients = totalIngredientsForAllDays.values().stream()
        .map(IngredientReportItemResponse::new)
        .sorted(Comparator.comparing(response -> response.name))
        .collect(Collectors.toList());

    this.totalPrice = totalPriceForAllDays.getAmount();
  }
}
