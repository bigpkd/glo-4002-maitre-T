package ca.ulaval.glo4002.reservation.reservation.domain;

import ca.ulaval.glo4002.reservation.utils.Money;

public interface ReservationRepository {
  void store(Reservation reservation);

  int getNumberOfReservations();

  boolean containsRestriction(String restrictionName);

  Reservation getReservation(ReservationNumber number);

  Money computeTotalPriceOfAllReservations();
}
