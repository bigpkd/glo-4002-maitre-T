package ca.ulaval.glo4002.reservation.reservation.repository;

import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.domain.ReservationNumber;
import ca.ulaval.glo4002.reservation.reservation.domain.ReservationRepository;
import ca.ulaval.glo4002.reservation.utils.Money;

import java.util.HashMap;

public class ReservationRepositoryInMemory implements ReservationRepository {
  private HashMap<ReservationNumber, Reservation> reservations;

  public ReservationRepositoryInMemory() {
    this.reservations = new HashMap<>();
  }

  public int getNumberOfReservations() {
    return reservations.size();
  }

  public boolean containsRestriction(String restrictionName) {
    for (Reservation reservation : reservations.values()) {
      if (reservation.containsRestriction(restrictionName)) {
        return true;
      }
    }
    return false;
  }

  public void store(Reservation reservation) {
    reservations.put(reservation.getNumber(), reservation);
  }

  public Reservation getReservation(ReservationNumber number) {
    return reservations.get(number);
  }

  public Money computeTotalPriceOfAllReservations() {
    Money AllReservationsTotalPrice = new Money(0.0d);
    for (Reservation reservation : reservations.values()) {
      AllReservationsTotalPrice = AllReservationsTotalPrice.add(reservation.getReservationPrice());
    }
    return AllReservationsTotalPrice;
  }
}
