package ca.ulaval.glo4002.reservation.hoppening.domain;

import ca.ulaval.glo4002.reservation.utils.Money;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public interface HoppeningDaysRepository {
  void update(HoppeningDay hoppeningDay);

  HoppeningDay getHoppeningDay(LocalDate date);

  List<HoppeningDay> getHoppeningDaysForTimePeriod(LocalDate startDate, LocalDate endDate);

  HoppeningDay getPreviousDayWithReservation(LocalDate date);

  void configureEventDates(LocalDate startDate, LocalDate endDate);

  public Money computeTotalPriceOfChefs(OffsetDateTime startOffsetDateTime, OffsetDateTime endOffsetDateTime);
  }
