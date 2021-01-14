package ca.ulaval.glo4002.reservation.configuration.domain;

import ca.ulaval.glo4002.reservation.report.domain.exception.InvalidReportDateException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class HoppeningDates {
  private OffsetDateTime reservationBeginDate;
  private OffsetDateTime reservationEndDate;
  private OffsetDateTime hoppeningBeginDate;
  private OffsetDateTime hoppeningEndDate;

  public HoppeningDates(OffsetDateTime reservationBeginDate, OffsetDateTime reservationEndDate,
                        OffsetDateTime hoppeningBeginDate, OffsetDateTime hoppeningEndDate) {
    this.reservationBeginDate = reservationBeginDate;
    this.reservationEndDate = reservationEndDate;
    this.hoppeningBeginDate = hoppeningBeginDate;
    this.hoppeningEndDate = hoppeningEndDate;
  }

  public boolean validateReservationDate(OffsetDateTime reservationDate) {
    if (reservationDate.isBefore(reservationEndDate) && reservationDate.isAfter(reservationBeginDate)) {
      return true;
    }

    return reservationDate.isEqual(reservationEndDate) || reservationDate.isEqual(reservationBeginDate);
  }

  public boolean validateDinnerDate(OffsetDateTime dinnerDate) {
    if (dinnerDate.isBefore(hoppeningEndDate) && dinnerDate.isAfter(hoppeningBeginDate)) {
      return true;
    }

    return dinnerDate.isEqual(hoppeningEndDate) || dinnerDate.isEqual(hoppeningBeginDate);
  }

  public void changeHoppeningDates(HoppeningDates hoppeningDates) {
    reservationBeginDate = hoppeningDates.reservationBeginDate;
    reservationEndDate = hoppeningDates.reservationEndDate;
    hoppeningBeginDate = hoppeningDates.hoppeningBeginDate;
    hoppeningEndDate = hoppeningDates.hoppeningEndDate;
  }

  public OffsetDateTime getReservationBeginDate() {
    return reservationBeginDate;
  }

  public OffsetDateTime getReservationEndDate() {
    return reservationEndDate;
  }

  public OffsetDateTime getHoppeningBeginDate() {
    return hoppeningBeginDate;
  }

  public OffsetDateTime getHoppeningEndDate() {
    return hoppeningEndDate;
  }

  public LocalDate parseDate(String dateString) {
    try {
      return LocalDate.parse(dateString);
    } catch (Exception e) {
      throw new InvalidReportDateException(getHoppeningBeginDate(),
          getHoppeningEndDate());
    }
  }

  public void validateTimePeriod(LocalDate startDate, LocalDate endDate) {
    boolean isStarDateBeforeEndDate = startDate.isBefore(endDate);
    boolean isStartDinnerDateValid = validateDinnerDate(OffsetDateTime.of(startDate, LocalTime.MIDNIGHT, ZoneOffset.UTC));
    boolean isEndDinnerDateValid = validateDinnerDate(OffsetDateTime.of(endDate, LocalTime.MIDNIGHT, ZoneOffset.UTC));
    boolean isValidTimePeriod = isStarDateBeforeEndDate && isStartDinnerDateValid && isEndDinnerDateValid;

    if (!isValidTimePeriod) {
      throw new InvalidReportDateException(getHoppeningBeginDate(),
          getHoppeningEndDate());
    }
  }
}
