package ca.ulaval.glo4002.reservation.report.domain.exception;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;

public class InvalidReportTypeException extends DomainException {
  private static final String message = "Type must be either total or unit and must be specified.";
  private static final String error = "INVALID_TYPE";

  public InvalidReportTypeException() {
    super(message);
  }

  public String getError() {
    return error;
  }
}
