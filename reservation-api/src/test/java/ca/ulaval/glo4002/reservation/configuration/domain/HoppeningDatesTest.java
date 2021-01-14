package ca.ulaval.glo4002.reservation.configuration.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import ca.ulaval.glo4002.reservation.report.domain.exception.InvalidReportDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class HoppeningDatesTest {
  private static final OffsetDateTime RESERVATION_START_DATE =
      OffsetDateTime.of(2030, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime RESERVATION_END_DATE =
      OffsetDateTime.of(2030, 7, 16, 23, 59, 59, 0, ZoneOffset.UTC);
  private static final OffsetDateTime RESERVATION_DINNER_START_DATE =
      OffsetDateTime.of(2030, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime RESERVATION_DINNER_END_DATE =
      OffsetDateTime.of(2030, 7, 30, 23, 59, 59, 0, ZoneOffset.UTC);
  private static final OffsetDateTime ANOTHER_RESERVATION_START_DATE =
      OffsetDateTime.of(2050, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime ANOTHER_RESERVATION_END_DATE =
      OffsetDateTime.of(2050, 5, 16, 23, 59, 59, 0, ZoneOffset.UTC);
  private static final OffsetDateTime ANOTHER_RESERVATION_DINNER_START_DATE =
      OffsetDateTime.of(2050, 6, 15, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime ANOTHER_RESERVATION_DINNER_END_DATE =
      OffsetDateTime.of(2050, 6, 19, 23, 59, 59, 0, ZoneOffset.UTC);

  private HoppeningDates hoppeningDates;
  private HoppeningDates otherHoppeningDates;

  @BeforeEach
  void setUp() {
    hoppeningDates = new HoppeningDates(RESERVATION_START_DATE, RESERVATION_END_DATE,
        RESERVATION_DINNER_START_DATE, RESERVATION_DINNER_END_DATE);
    otherHoppeningDates = new HoppeningDates(ANOTHER_RESERVATION_START_DATE, ANOTHER_RESERVATION_END_DATE,
        ANOTHER_RESERVATION_DINNER_START_DATE, ANOTHER_RESERVATION_DINNER_END_DATE);
  }

  @Test
  void givenHoppeningSchedule_whenValidatingValidReservationDate_thenTrueIsObtained() {
    OffsetDateTime validReservationDate = RESERVATION_END_DATE.minusDays(1);

    assertTrue(hoppeningDates.validateReservationDate(validReservationDate));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingReservationDateBeforePeriod_thenFalseIsObtained() {
    OffsetDateTime earlyReservationDate = RESERVATION_START_DATE.minusDays(1);

    assertFalse(hoppeningDates.validateReservationDate(earlyReservationDate));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingReservationDateAfterPeriod_thenInvalidReservationDateExceptionIsThrown() {
    OffsetDateTime lateReservationDate = RESERVATION_END_DATE.plusDays(1);

    assertFalse(hoppeningDates.validateReservationDate(lateReservationDate));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingValidDinnerDate_thenTrueIsObtained() {
    OffsetDateTime validDinnerDate = RESERVATION_DINNER_END_DATE.minusDays(1);

    assertTrue(hoppeningDates.validateDinnerDate(validDinnerDate));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingDinnerDateBeforePeriod_thenShouldThrowInvalidDinnerDateException() {
    OffsetDateTime earlyDinnerDate = RESERVATION_DINNER_START_DATE.minusDays(1);

    assertFalse(hoppeningDates.validateDinnerDate(earlyDinnerDate));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingDinnerDateAfterPeriod_thenShouldThrowInvalidReservationDateException() {
    OffsetDateTime lateReservationDate = RESERVATION_DINNER_END_DATE.plusDays(1);

    assertFalse(hoppeningDates.validateDinnerDate(lateReservationDate));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingDinnerDateEqualToStartOfPeriod_thenShouldReturnTrue() {
    assertTrue(hoppeningDates.validateDinnerDate(RESERVATION_DINNER_START_DATE));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingDinnerDateEqualToEndOfPeriod_thenShouldReturnTrue() {
    assertTrue(hoppeningDates.validateDinnerDate(RESERVATION_DINNER_END_DATE));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingReservationDateEqualToStartOfPeriod_theShouldReturnTrue() {
    assertTrue(hoppeningDates.validateReservationDate(RESERVATION_START_DATE));
  }

  @Test
  void givenHoppeningSchedule_whenValidatingReservationDateEqualToEndOfPeriod_thenShouldReturnTrue() {
    assertTrue(hoppeningDates.validateReservationDate(RESERVATION_END_DATE));
  }

  @Test
  void whenChangingHoppeningDates_thenShouldChangeTheHoppeningEventDates() {
    hoppeningDates.changeHoppeningDates(otherHoppeningDates);

    assertEquals(hoppeningDates.getReservationBeginDate(), ANOTHER_RESERVATION_START_DATE);
    assertEquals(hoppeningDates.getReservationEndDate(), ANOTHER_RESERVATION_END_DATE);
  }

  @Test
  void whenChangingHoppeningDates_thenShouldChangeTheHoppeningReservationDates() {
    hoppeningDates.changeHoppeningDates(otherHoppeningDates);

    assertEquals(hoppeningDates.getHoppeningBeginDate(), ANOTHER_RESERVATION_DINNER_START_DATE);
    assertEquals(hoppeningDates.getHoppeningEndDate(), ANOTHER_RESERVATION_DINNER_END_DATE);
  }

  @Test
  void givenStartDateAfterEndDate_whenValidatingTimePeriod_thenInvalidReportDateExceptionShouldBeThrown() {
    LocalDate startDate = LocalDate.of(2020, 1, 25);
    LocalDate endDate = LocalDate.of(2020, 1, 15);

    assertThrows(InvalidReportDateException.class, () -> hoppeningDates.validateTimePeriod(startDate, endDate));
  }

  @Test
  void givenStartDateBeforeHoppeningEvent_whenValidatingTimePeriod_thenInvalidReportDateExceptionShouldBeThrown() {
    LocalDate startDate = LocalDate.of(2020, 1, 15);
    LocalDate endDate = LocalDate.of(2020, 1, 25);

    assertThrows(InvalidReportDateException.class, () -> hoppeningDates.validateTimePeriod(startDate, endDate));
  }

  @Test
  void givenEndDateAfterHoppeningEvent_whenValidatingTimePeriod_thenInvalidReportDateExceptionShouldBeThrown() {
    LocalDate startDate = LocalDate.of(2020, 1, 15);
    LocalDate endDate = LocalDate.of(2020, 1, 25);

    assertThrows(InvalidReportDateException.class, () -> hoppeningDates.validateTimePeriod(startDate, endDate));
  }
}
