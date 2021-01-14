package ca.ulaval.glo4002.reservation.configuration.servable;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.configuration.exception.InvalidTimeFramesException;
import ca.ulaval.glo4002.reservation.configuration.rest.dto.DatesDto;
import ca.ulaval.glo4002.reservation.configuration.rest.dto.HoppeningDatesDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class HoppeningDatesFactory {
  public HoppeningDates create(HoppeningDatesDto hoppeningDatesDto) {
    validate(hoppeningDatesDto);
    return new HoppeningDates(OffsetDateTime.of(hoppeningDatesDto.reservationPeriodDto.beginDate, LocalTime.MIN, ZoneOffset.UTC),
        OffsetDateTime.of(hoppeningDatesDto.reservationPeriodDto.endDate, LocalTime.of(23, 59, 59), ZoneOffset.UTC),
        OffsetDateTime.of(hoppeningDatesDto.hoppening.beginDate, LocalTime.MIN, ZoneOffset.UTC),
        OffsetDateTime.of(hoppeningDatesDto.hoppening.endDate, LocalTime.of(23, 59, 59), ZoneOffset.UTC));
  }

  private void validate(HoppeningDatesDto hoppeningDatesDto) {
    validateReservationPeriod(hoppeningDatesDto.reservationPeriodDto);
    validateHoppeningPeriod(hoppeningDatesDto.hoppening);
    validateChronology(hoppeningDatesDto.reservationPeriodDto.endDate, hoppeningDatesDto.hoppening.beginDate);
  }

  private void validateReservationPeriod(DatesDto reservationPeriodDto) {
    if (reservationPeriodDto.beginDate.isAfter(reservationPeriodDto.endDate)
        || reservationPeriodDto.beginDate.isEqual(reservationPeriodDto.endDate)) {
      throw new InvalidTimeFramesException();
    }
  }

  private void validateHoppeningPeriod(DatesDto hoppening) {
    if (hoppening.beginDate.isAfter(hoppening.endDate) || hoppening.beginDate.isEqual(hoppening.endDate)) {
      throw new InvalidTimeFramesException();
    }
  }

  private void validateChronology(LocalDate reservationEndDate, LocalDate hoppeningBeginDate) {
    if (reservationEndDate.isAfter(hoppeningBeginDate) || reservationEndDate.isEqual(hoppeningBeginDate)) {
      throw new InvalidTimeFramesException();
    }
  }
}
