package ca.ulaval.glo4002.reservation.report.servable.ingredient;

import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import ca.ulaval.glo4002.reservation.report.domain.exception.InvalidReportTypeException;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientReportResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientReportTotalResponse;
import ca.ulaval.glo4002.reservation.report.rest.response.ingredient.IngredientReportUnitResponse;
import java.util.List;

public class IngredientReportResponseFactory {

  public IngredientReportResponse create(List<HoppeningDay> hoppeningDaysForTimePeriod,
                                         String reportTypeString) {
    IngredientReportResponseType ingredientReportResponseType;
    try {
      ingredientReportResponseType = IngredientReportResponseType.valueOf(reportTypeString.toUpperCase());
    } catch (Exception e) {
      throw new InvalidReportTypeException();
    }

    switch (ingredientReportResponseType) {
      case UNIT:
        return new IngredientReportUnitResponse(hoppeningDaysForTimePeriod);
      case TOTAL:
        return new IngredientReportTotalResponse(hoppeningDaysForTimePeriod);
      default:
        throw new InvalidReportTypeException();
    }
  }
}
