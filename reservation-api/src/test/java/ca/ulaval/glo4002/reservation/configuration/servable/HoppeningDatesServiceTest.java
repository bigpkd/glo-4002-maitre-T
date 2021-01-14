package ca.ulaval.glo4002.reservation.configuration.servable;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.configuration.rest.dto.DatesDto;
import ca.ulaval.glo4002.reservation.configuration.rest.dto.HoppeningDatesDto;
import ca.ulaval.glo4002.reservation.hoppening.domain.HoppeningDaysRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.*;

public class HoppeningDatesServiceTest {
  private static String RESERVATION_START_DATE = "2020-01-20";
  private static String RESERVATION_END_DATE = "2020-03-30";
  private static String HOPPENING_START_DATE = "2020-05-20";
  private static String HOPPENING_END_DATE = "2020-05-25";
  public static final OffsetDateTime DINNER_DATE_START_DATE =
      OffsetDateTime.of(2020, 5, 20, 0, 0, 0, 0, ZoneOffset.UTC);
  public static final OffsetDateTime DINNER_DATE_END_DATE =
      OffsetDateTime.of(2020, 5, 25, 23, 59, 59, 0, ZoneOffset.UTC);
  public static final LocalDate DINNER_START_LOCAL_DATE = DINNER_DATE_START_DATE.toLocalDate();
  public static final LocalDate DINNER_END_LOCAL_DATE = DINNER_DATE_END_DATE.toLocalDate();

  private HoppeningDates hoppeningDates;
  private HoppeningDatesDto hoppeningDatesDto;
  private HoppeningDatesFactory hoppeningDatesFactory;
  private HoppeningDatesService hoppeningDatesService;
  private HoppeningDaysRepository hoppeningDaysRepository;

  @BeforeEach
  void setupHoppeningDatesService() {
    hoppeningDates = mock(HoppeningDates.class);
    hoppeningDatesDto = new HoppeningDatesDto(new DatesDto(RESERVATION_START_DATE, RESERVATION_END_DATE),
        new DatesDto(HOPPENING_START_DATE, HOPPENING_END_DATE));
    hoppeningDatesFactory = mock(HoppeningDatesFactory.class);
    when(hoppeningDatesFactory.create(hoppeningDatesDto)).thenReturn(hoppeningDates);
    when(hoppeningDates.getHoppeningBeginDate()).thenReturn(DINNER_DATE_START_DATE);
    when(hoppeningDates.getHoppeningEndDate()).thenReturn(DINNER_DATE_END_DATE);
    hoppeningDaysRepository = mock(HoppeningDaysRepository.class);
    hoppeningDatesService = new HoppeningDatesService(hoppeningDatesFactory, hoppeningDates, hoppeningDaysRepository);
  }

  @Test
  void whenChangingDates_thenShouldCallCreateFromHoppeningDatesFactory() {
    hoppeningDatesService.changeDates(hoppeningDatesDto);

    verify(hoppeningDatesFactory).create(hoppeningDatesDto);
  }

  @Test
  void whenChangingDates_thenShouldCallHoppeningScheduleToChangeHoppeningDates() {
    when(hoppeningDatesFactory.create(hoppeningDatesDto)).thenReturn(hoppeningDates);

    hoppeningDatesService.changeDates(hoppeningDatesDto);

    verify(hoppeningDates).changeHoppeningDates(hoppeningDates);
  }

  @Test
  void whenChangingDates_thenShouldCallHoppeningRepoToConfigureHoppeningDates() {
    when(hoppeningDatesFactory.create(hoppeningDatesDto)).thenReturn(hoppeningDates);

    hoppeningDatesService.changeDates(hoppeningDatesDto);

    verify(hoppeningDaysRepository).configureEventDates(DINNER_START_LOCAL_DATE, DINNER_END_LOCAL_DATE);
  }
}
