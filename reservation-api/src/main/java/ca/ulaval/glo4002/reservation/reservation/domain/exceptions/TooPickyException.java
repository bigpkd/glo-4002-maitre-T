package ca.ulaval.glo4002.reservation.reservation.domain.exceptions;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;

public class TooPickyException extends DomainException {
  private static final String message =
      "You seem to be too picky and now, you cannot make a reservation for this date.";
  private static final String error = "TOO_PICKY";

  public TooPickyException() {
    super(message);
  }

  public String getError() {
    return error;
  }
}