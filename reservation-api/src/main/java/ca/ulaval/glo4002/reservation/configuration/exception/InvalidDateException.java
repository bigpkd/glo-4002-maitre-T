package ca.ulaval.glo4002.reservation.configuration.exception;

import ca.ulaval.glo4002.reservation.exceptions.RestException;

public class InvalidDateException extends RestException {
  private static final String message = "Invalid dates, please use the format yyyy-mm-dd";
  private static final String error = "INVALID_DATE";

  public InvalidDateException() {
    super(message);
  }

  public String getError() {
    return error;
  }
}
