package ca.ulaval.glo4002.reservation.hoppening.repository;

import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDay;
import ca.ulaval.glo4002.reservation.reservation.servable.ChefBooklet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HoppeningDayRepositoryInMemoryTest {
  private static final LocalDate DINNER_START_DATE = LocalDate.of(2020, 10, 28);
  private static final LocalDate DINNER_END_DATE = LocalDate.of(2020, 11, 28);
  private static final LocalDate ANOTHER_DINNER_START_DATE = LocalDate.of(2022, 10, 28);
  private static final LocalDate ANOTHER_DINNER_END_DATE = LocalDate.of(2022, 11, 28);
  private static final LocalDate DATE_AFTER_HOPPENING_SCHEDULE = DINNER_END_DATE.plusDays(1);
  private static final LocalDate DATE_BEFORE_HOPPENING_SCHEDULE = DINNER_START_DATE.minusDays(1);
  private static final int TWO_DAYS = 2;

  private HoppeningDay hoppeningDay;
  private HoppeningDayRepositoryInMemory hoppeningDayRepository;

  @BeforeEach
  void SetUp() {
    hoppeningDay = mock(HoppeningDay.class);
    ChefBooklet chefBooklet = mock(ChefBooklet.class);
    hoppeningDayRepository = new HoppeningDayRepositoryInMemory(DINNER_START_DATE, DINNER_END_DATE, chefBooklet);
  }

  @Test
  void givenHoppeningDayRepository_thenAllHoppeningDatesAreCreated() {
    int count = 0;
    long nbOfDays = DAYS.between(DINNER_START_DATE, DINNER_END_DATE) + 1;

    for (LocalDate date = DINNER_START_DATE; date.isBefore(DINNER_END_DATE) || date.isEqual(DINNER_END_DATE);
         date = date.plusDays(1)) {
      if (hoppeningDayRepository.getHoppeningDay(date) != null) {
        count++;
      }
    }

    assertEquals(nbOfDays, count);
  }

  @Test
  void givenHoppeningDayRepository_whenConfigureWithDifferentDates_thenNewHoppeningDatesAreCreated() {
    int count = 0;
    long nbOfDays = DAYS.between(ANOTHER_DINNER_START_DATE, ANOTHER_DINNER_END_DATE) + 1;

    hoppeningDayRepository.configureEventDates(ANOTHER_DINNER_START_DATE, ANOTHER_DINNER_END_DATE);

    for (LocalDate date = ANOTHER_DINNER_START_DATE;
         date.isBefore(ANOTHER_DINNER_END_DATE) || date.isEqual(ANOTHER_DINNER_END_DATE);
         date = date.plusDays(1)) {
      if (hoppeningDayRepository.getHoppeningDay(date) != null) {
        count++;
      }
    }

    assertEquals(nbOfDays, count);
  }

  @Test
  void givenHoppeningDayRepository_whenConfigureWithDifferentDates_thenOldHoppeningDatesAreRemoved() {
    hoppeningDayRepository.configureEventDates(ANOTHER_DINNER_START_DATE, ANOTHER_DINNER_END_DATE);

    HoppeningDay obtainedHoppeningDay = hoppeningDayRepository.getHoppeningDay(DINNER_START_DATE);

    assertNull(obtainedHoppeningDay);
  }

  @Test
  void givenHoppeningDayOutOfHoppeningSchedule_whenGettingHoppeningDay_thenNullIsObtained() {
    assertNull(hoppeningDayRepository.getHoppeningDay(DATE_AFTER_HOPPENING_SCHEDULE));
  }

  @Test
  void givenHoppeningDayWithinHoppeningSchedule_whenGettingHoppeningDay_thenHoppeningDayIsObtained() {
    HoppeningDay returnedHoppeningDay = hoppeningDayRepository.getHoppeningDay(DINNER_START_DATE);

    assertEquals(DINNER_START_DATE, returnedHoppeningDay.getDate());
  }

  @Test
  void whenGettingHoppeningDaysForTimePeriod_thenAsMuchHoppeningDaysThanDaysInTimePeriodAreObtained() {
    LocalDate dinnerStartDatePlus = DINNER_START_DATE.plusDays(TWO_DAYS);
    int expectedNbOfDays = TWO_DAYS + 1;

    List<HoppeningDay> hoppeningDays = hoppeningDayRepository
        .getHoppeningDaysForTimePeriod(DINNER_START_DATE, dinnerStartDatePlus);

    assertEquals(expectedNbOfDays, hoppeningDays.size());
  }

  @Test
  void whenGettingHoppeningDaysForTimePeriod_thenDaysAreReturnerInChronologicalOrder() {
    LocalDate dinnerStartDatePlusThree = DINNER_START_DATE.plusDays(3);

    List<HoppeningDay> hoppeningDays = hoppeningDayRepository
        .getHoppeningDaysForTimePeriod(DATE_BEFORE_HOPPENING_SCHEDULE, dinnerStartDatePlusThree);

    LocalDate firstReturnedHoppeningDay = hoppeningDays.get(0).getDate();
    LocalDate secondReturnedHoppeningDay = hoppeningDays.get(1).getDate();
    LocalDate lastReturnedHoppeningDay = hoppeningDays.get(hoppeningDays.size() - 1).getDate();

    assertTrue(firstReturnedHoppeningDay.isBefore(secondReturnedHoppeningDay));
    assertTrue(secondReturnedHoppeningDay.isBefore(lastReturnedHoppeningDay));
  }

  @Test
  void whenGettingHoppeningDaysForTimePeriodExtendingAfterLastDay_thenOnlyDaysInsideRepositoryPeriodAreObtained() {
    long expectedNbOfDays = DAYS.between(DINNER_END_DATE, DINNER_END_DATE) + 1;

    List<HoppeningDay> hoppeningDays = hoppeningDayRepository
        .getHoppeningDaysForTimePeriod(DINNER_END_DATE, DATE_AFTER_HOPPENING_SCHEDULE);

    assertEquals(expectedNbOfDays, hoppeningDays.size());
  }

  @Test
  void whenGettingHoppeningDaysForTimePeriodStartingBeforeFirstDay_thenOnlyDaysInsideRepositoryPeriodAreObtained() {
    long expectedNbOfDays = DAYS.between(DINNER_START_DATE, DINNER_START_DATE) + 1;

    List<HoppeningDay> hoppeningDays = hoppeningDayRepository
        .getHoppeningDaysForTimePeriod(DATE_BEFORE_HOPPENING_SCHEDULE, DINNER_START_DATE);

    assertEquals(expectedNbOfDays, hoppeningDays.size());
  }

  @Test
  void whenGettingHoppeningDaysForInvalidTimePeriod_thenNoDaysAreObtained() {
    long expectedNbOfDays = 0;

    List<HoppeningDay> hoppeningDays = hoppeningDayRepository
        .getHoppeningDaysForTimePeriod(DATE_AFTER_HOPPENING_SCHEDULE, DATE_AFTER_HOPPENING_SCHEDULE);

    assertEquals(expectedNbOfDays, hoppeningDays.size());
  }

  @Test
  void givenHoppeningDayRepository_whenUpdatingHoppeningDay_thenHoppeningDayIsReplacedInMemory() {
    HoppeningDay defaultHoppeningDay = hoppeningDayRepository.getHoppeningDay(DINNER_START_DATE);
    when(hoppeningDay.getDate()).thenReturn(DINNER_START_DATE);

    hoppeningDayRepository.update(hoppeningDay);
    HoppeningDay returnedHoppeningDay = hoppeningDayRepository.getHoppeningDay(DINNER_START_DATE);

    assertNotEquals(defaultHoppeningDay, returnedHoppeningDay);
    assertEquals(returnedHoppeningDay, hoppeningDay);
  }

  @Test
  void givenOneHoppeningDayContainingReservation_whenGettingPreviousDayWithReservation_thenReturnHoppeningDayWithReservation() {
    LocalDate expectedPreviousDate = DINNER_END_DATE.minusDays(2);
    when(hoppeningDay.hasReservations()).thenReturn(true);
    when(hoppeningDay.getDate()).thenReturn(expectedPreviousDate);
    hoppeningDayRepository.update(hoppeningDay);

    HoppeningDay obtainedDay = hoppeningDayRepository.getPreviousDayWithReservation(DINNER_END_DATE);

    assertEquals(expectedPreviousDate, obtainedDay.getDate());
  }

  @Test
  void givenNoHoppeningDayContainingReservation_whenGettingPreviousDayWithReservation_thenNullIsObtained() {
    HoppeningDay obtainedDay = hoppeningDayRepository.getPreviousDayWithReservation(DINNER_END_DATE);

    assertEquals(DINNER_END_DATE.minusDays(1), obtainedDay.getDate());
  }

}