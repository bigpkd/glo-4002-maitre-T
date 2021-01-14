package ca.ulaval.glo4002.reservation.report.domain.exception;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;
import java.time.OffsetDateTime;

public class InvalidReportDateException extends DomainException {
  private static final String message = "Dates should be between %tB %te %tY and %tB %te %tY and must be specified.";
  private static final String error = "INVALID_DATE";

  public InvalidReportDateException(OffsetDateTime startDate, OffsetDateTime endDate) {
    super(String.format(message, startDate, startDate, startDate, endDate, endDate, endDate));
  }

  public String getError() {
    return error;
  }
}
