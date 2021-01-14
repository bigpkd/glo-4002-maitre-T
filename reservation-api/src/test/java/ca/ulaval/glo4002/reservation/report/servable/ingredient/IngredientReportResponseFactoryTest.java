package ca.ulaval.glo4002.reservation.report.servable.ingredient;

import ca.ulaval.glo4002.reservation.report.domain.exception.InvalidReportTypeException;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientReportResponseFactoryTest {
  private static final String INVALID_REPORT_TYPE = "invalid report type";
  private IngredientReportResponseFactory ingredientReportResponseFactory = new IngredientReportResponseFactory();

  @Test
  void whenCreatingWithInvalidReportType_thenInvalidReportTypeExceptionIsThrown() {
    assertThrows(InvalidReportTypeException.class, () -> ingredientReportResponseFactory
        .create(Collections.emptyList(), INVALID_REPORT_TYPE));
  }

}