package ca.ulaval.glo4002.reservation.reservation.domain.exceptions;

import ca.ulaval.glo4002.reservation.exceptions.DomainException;
import java.time.OffsetDateTime;

public class InvalidReservationDateException extends DomainException {
  private static final String message = "Reservation date should be between %tB %te %tY and %tB %te %tY";
  private static final String error = "INVALID_RESERVATION_DATE";

  public InvalidReservationDateException(OffsetDateTime startDate, OffsetDateTime endDate) {
    super(String.format(message, startDate, startDate, startDate, endDate, endDate, endDate));
  }

  public String getError() {
    return error;
  }
}
