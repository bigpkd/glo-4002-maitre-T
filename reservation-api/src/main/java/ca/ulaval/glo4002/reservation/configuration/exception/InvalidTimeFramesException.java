package ca.ulaval.glo4002.reservation.configuration.exception;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;

public class InvalidTimeFramesException extends DomainException {
  private static final String message = "Invalid time frames, please use better ones.";
  private static final String error = "INVALID_TIME_FRAMES";

  public InvalidTimeFramesException() {
    super(message);
  }

  @Override
  public String getError() {
    return error;
  }
}
