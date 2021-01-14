package ca.ulaval.glo4002.reservation.report.rest.response.ingredient;

import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientReportUnitResponse implements IngredientReportResponse {
  public List<HoppeningDayResponse> dates;

  public IngredientReportUnitResponse(List<HoppeningDay> dates) {
    super();

    this.dates = dates.stream()
        .filter(d -> !d.getIngredientReportRegister().getIngredientReportItems().isEmpty())
        .map(HoppeningDayResponse::new)
        .collect(Collectors.toList());
  }
}
