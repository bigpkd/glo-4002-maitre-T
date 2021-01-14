package ca.ulaval.glo4002.reservation.reservation.domain.exceptions;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;

public class TooManyPeopleException extends DomainException {
  private static final String message =
      "The reservation tries to bring a number of people which does not comply with recent government laws.";
  private static final String error = "TOO_MANY_PEOPLE";

  public TooManyPeopleException() {
    super(message);
  }

  public String getError() {
    return error;
  }
}