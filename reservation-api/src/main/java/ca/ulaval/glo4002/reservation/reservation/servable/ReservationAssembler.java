package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.rest.response.ReservationResponse;

public class ReservationAssembler {
  public ReservationResponse toReservationResponse(Reservation reservation) {
    return new ReservationResponse(reservation.getDinnerDate(), reservation.getCustomers(),
        reservation.getReservationPrice());
  }

/*  public String toReservationStringId(Reservation reservation) {
    return Long.toString(reservation.getId());
  }*/

  public String toReservationStringNumber(Reservation reservation) {
    return reservation.getNumber().toString();
  }
}
