package ca.ulaval.glo4002.reservation.reservation.servable;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidDinnerDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooManyPeopleException;
import java.time.OffsetDateTime;
import java.util.List;
import javax.inject.Inject;

public class ReservationFactory {
  private HoppeningDates hoppeningDates;
  private int maximumCustomersPerReservation = 6;

  @Inject
  public ReservationFactory(HoppeningDates hoppeningDates) {
    this.hoppeningDates = hoppeningDates;
  }

  public Reservation create(ReservationRequest reservationRequest) {
    String vendorCode = reservationRequest.getVendorCode();
    OffsetDateTime dinnerDate = reservationRequest.getDinnerDate();
    OffsetDateTime reservationDate = reservationRequest.getReservationDate();
    Country country = reservationRequest.getCountry();
    List<Table> tables = reservationRequest.getTables();

    validateCustomersPerReservation(tables);
    validateDinnerDate(dinnerDate);
    validateReservationDate(reservationDate);

    return new Reservation(vendorCode, dinnerDate, reservationDate, country, tables);
  }


  private void validateCustomersPerReservation(List<Table> tables) {
    int numberOfCustomers = countCustomers(tables);

    if (numberOfCustomers > maximumCustomersPerReservation) {
      throw new TooManyPeopleException();
    }
  }

  private int countCustomers(List<Table> tables) {
    int count = 0;

    for (Table table : tables) {
      count += table.countCustomers();
    }

    return count;
  }

  private void validateReservationDate(OffsetDateTime reservationDate) {
    if (!hoppeningDates.validateReservationDate(reservationDate)) {
      throw new InvalidReservationDateException(hoppeningDates.getReservationBeginDate(),
          hoppeningDates.getReservationEndDate());
    }
  }

  private void validateDinnerDate(OffsetDateTime dinnerDate) {
    if (!hoppeningDates.validateDinnerDate(dinnerDate)) {
      throw new InvalidDinnerDateException(hoppeningDates.getHoppeningBeginDate(),
          hoppeningDates.getHoppeningEndDate());
    }
  }

}
