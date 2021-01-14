package ca.ulaval.glo4002.reservation.configuration.servable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


import ca.ulaval.glo4002.reservation.configuration.exception.InvalidTimeFramesException;
import ca.ulaval.glo4002.reservation.configuration.rest.dto.DatesDto;
import ca.ulaval.glo4002.reservation.configuration.rest.dto.HoppeningDatesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HoppeningDatesFactoryTest {
  private static String RESERVATION_START_DATE = "2020-01-20";
  private static String RESERVATION_END_DATE = "2020-03-30";
  private static String HOPPENING_START_DATE = "2020-05-20";
  private static String HOPPENING_END_DATE = "2020-05-25";

  private DatesDto validReservationPeriodDto;
  private DatesDto validHoppeningPeriodDto;
  private HoppeningDatesFactory hoppeningDatesFactory;

  @BeforeEach
  void setupHoppeningConfiguration() {
    validReservationPeriodDto = new DatesDto(RESERVATION_START_DATE, RESERVATION_END_DATE);
    validHoppeningPeriodDto = new DatesDto(HOPPENING_START_DATE, HOPPENING_END_DATE);
    hoppeningDatesFactory = new HoppeningDatesFactory();
  }

  @Test
  void givenInvalidReservationPeriod_whenValidation_thenShouldThrowInvalidTimeFramesException() {
    String invalidReservationStartDate = "2020-05-30";
    DatesDto invalidReservationPeriodDto = new DatesDto(invalidReservationStartDate, RESERVATION_END_DATE);
    HoppeningDatesDto hoppeningDatesDto = new HoppeningDatesDto(invalidReservationPeriodDto, validHoppeningPeriodDto);

    assertThrows(InvalidTimeFramesException.class, () -> hoppeningDatesFactory.create(hoppeningDatesDto));
  }

  @Test
  void givenValidReservationPeriod_whenValidation_thenShouldNotThrow() {
    HoppeningDatesDto hoppeningDatesDto = new HoppeningDatesDto(validReservationPeriodDto, validHoppeningPeriodDto);

    assertDoesNotThrow(() -> hoppeningDatesFactory.create(hoppeningDatesDto));
  }

  @Test
  void givenInvalidHoppeningPeriod_whenValidation_thenShouldThrowInvalidTimeFramesException() {
    String invalidHoppeningStartDate = "2020-05-30";
    DatesDto invalidHoppeningPeriodDto = new DatesDto(invalidHoppeningStartDate, HOPPENING_END_DATE);
    HoppeningDatesDto hoppeningDatesDto = new HoppeningDatesDto(validReservationPeriodDto, invalidHoppeningPeriodDto);

    assertThrows(InvalidTimeFramesException.class, () -> hoppeningDatesFactory.create(hoppeningDatesDto));
  }

  @Test
  void givenValidHoppeningPeriod_whenValidation_thenShouldNotThrow() {
    HoppeningDatesDto hoppeningDatesDto = new HoppeningDatesDto(validReservationPeriodDto, validHoppeningPeriodDto);

    assertDoesNotThrow(() -> hoppeningDatesFactory.create(hoppeningDatesDto));
  }

  @Test
  void givenInvalidChronology_whenValidation_thenShouldThrowInvalidTimeFramesException() {
    DatesDto reservationPeriodAfterHoppeningPeriod = new DatesDto(HOPPENING_START_DATE, HOPPENING_END_DATE);
    DatesDto hoppeningPeriodBeforeReservationPeriod = new DatesDto(RESERVATION_START_DATE, RESERVATION_END_DATE);
    HoppeningDatesDto hoppeningDatesDto = new HoppeningDatesDto(reservationPeriodAfterHoppeningPeriod,
        hoppeningPeriodBeforeReservationPeriod);

    assertThrows(InvalidTimeFramesException.class, () -> hoppeningDatesFactory.create(hoppeningDatesDto));
  }

  @Test
  void givenValidChronology_whenValidation_thenShouldNotThrow() {
    HoppeningDatesDto hoppeningDatesDto = new HoppeningDatesDto(validReservationPeriodDto, validHoppeningPeriodDto);

    assertDoesNotThrow(() -> hoppeningDatesFactory.create(hoppeningDatesDto));
  }
}
