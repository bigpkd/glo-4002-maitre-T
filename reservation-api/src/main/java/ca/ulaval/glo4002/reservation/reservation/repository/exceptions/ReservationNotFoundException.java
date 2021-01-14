package ca.ulaval.glo4002.reservation.reservation.repository.exceptions;

import ca.ulaval.glo4002.reservation.exceptions.InfrastructureException;
import ca.ulaval.glo4002.reservation.reservation.domain.ReservationNumber;

public class ReservationNotFoundException extends InfrastructureException {
  private static final String message = "Reservation with number %s not found";
  private static final String error = "RESERVATION_NOT_FOUND";

  public ReservationNotFoundException(ReservationNumber number) {
    super(String.format(message, number.toString()));
  }

  public String getError() {
    return error;
  }
}