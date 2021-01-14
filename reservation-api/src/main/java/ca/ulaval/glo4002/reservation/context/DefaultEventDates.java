package ca.ulaval.glo4002.reservation.context;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DefaultEventDates {
  public static final OffsetDateTime RESERVATION_START_DATE =
      OffsetDateTime.of(2150, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
  public static final OffsetDateTime RESERVATION_END_DATE =
      OffsetDateTime.of(2150, 7, 15, 23, 59, 59, 0, ZoneOffset.UTC);
  public static final OffsetDateTime DINNER_DATE_START_DATE =
      OffsetDateTime.of(2150, 7, 20, 0, 0, 0, 0, ZoneOffset.UTC);
  public static final OffsetDateTime DINNER_DATE_END_DATE =
      OffsetDateTime.of(2150, 7, 30, 23, 59, 59, 0, ZoneOffset.UTC);
}
