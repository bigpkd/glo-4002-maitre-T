package ca.ulaval.glo4002.reservation.reservation.domain.exceptions;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;

public class InvalidReservationQuantityException extends DomainException {
  private static final String message = "Reservations must include tables and customers";
  private static final String error = "INVALID_RESERVATION_QUANTITY";

  public InvalidReservationQuantityException() {
    super(message);
  }

  public String getError() {
    return error;
  }
}