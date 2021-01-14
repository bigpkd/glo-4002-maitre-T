package ca.ulaval.glo4002.reservation.reservation.servable;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.configuration.domain.HoppeningDates;
import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidDinnerDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.InvalidReservationDateException;
import ca.ulaval.glo4002.reservation.reservation.domain.exceptions.TooManyPeopleException;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationFactoryTest {
  private static final String VENDOR_CODE = "TEAM";
  private static final OffsetDateTime RESERVATION_DATE = OffsetDateTime.of(2020, 1, 6,
      0, 0, 0, 0, ZoneOffset.UTC);
  private static final OffsetDateTime DINNER_DATE = OffsetDateTime.of(2020, 7, 21,
      0, 0, 0, 0, ZoneOffset.UTC);

  private static final int MORE_CUSTOMERS_THAN_ALLOWED_PER_RESERVATION = 7;

  private static final OffsetDateTime INVALID_DINNER_DATE = OffsetDateTime.MAX;
  private static final OffsetDateTime INVALID_RESERVATION_DATE = OffsetDateTime.MAX;
  private static final Money ONE_DOLLAR = new Money(1.0);
  private static final int ONE_CUSTOMER = 1;

  private ReservationFactory reservationFactory;
  private HoppeningDates hoppeningDates;
  private Table table;
  private List<Table> tables = new ArrayList<>();
  private Country country;

  @BeforeEach
  void setUp() {
    table = mock(Table.class);
    when(table.getTablePrice()).thenReturn(ONE_DOLLAR);
    country = mock(Country.class);
    hoppeningDates = mock(HoppeningDates.class);
    when(hoppeningDates.validateDinnerDate(any())).thenReturn(true);
    when(hoppeningDates.validateReservationDate(any())).thenReturn(true);
    reservationFactory =
        new ReservationFactory(hoppeningDates);
    tables = Arrays.asList(table, table);
  }

  @Test
  void whenCreatingReservation_thenReservationIsCreatedWithNumber() {
    ReservationRequest reservationRequest =
            new ReservationRequest(VENDOR_CODE, DINNER_DATE, RESERVATION_DATE, country, tables);

    Reservation reservation = reservationFactory.create(reservationRequest);

    assertNotNull(reservation.getNumber());
  }

  @Test
  void givenReservationRequestWithInvalidDinnerDate_whenCreatingReservation_thenInvalidDinnerDateExceptionIsThrown() {
    ReservationRequest reservationRequestInvalidDinnerDate =
        new ReservationRequest(VENDOR_CODE, INVALID_DINNER_DATE, RESERVATION_DATE, country, tables);
    when(hoppeningDates.validateDinnerDate(INVALID_DINNER_DATE)).thenReturn(false);

    assertThrows(InvalidDinnerDateException.class, () -> reservationFactory.create(
        reservationRequestInvalidDinnerDate));
  }

  @Test
  void givenReservationRequestWithInvalidReservationDate_whenCreatingReservation_thenInvalidReservationDateExceptionIsThrown() {
    ReservationRequest reservationRequestInvalidDinnerDate =
        new ReservationRequest(VENDOR_CODE, DINNER_DATE, INVALID_RESERVATION_DATE, country, tables);
    when(hoppeningDates.validateReservationDate(INVALID_RESERVATION_DATE)).thenReturn(false);

    assertThrows(InvalidReservationDateException.class,
        () -> reservationFactory.create(reservationRequestInvalidDinnerDate));
  }

  @Test
  void whenValidatingCustomersPerReservationWithTooManyCustomersPerReservation_thenTooManyPeopleExceptionIsThrown() {
    List<Table> tableWithExcessCustomers =
        setUpTableWithNCustomers(MORE_CUSTOMERS_THAN_ALLOWED_PER_RESERVATION);
    ReservationRequest reservationRequestTooManyCustomers =
        new ReservationRequest(VENDOR_CODE, DINNER_DATE, INVALID_RESERVATION_DATE, country, tableWithExcessCustomers);

    assertThrows(TooManyPeopleException.class,
        () -> reservationFactory.create(reservationRequestTooManyCustomers));
  }

  private List<Table> setUpTableWithNCustomers(int nbCustomer) {
    List<Table> tables = new ArrayList<>();
    Table table = mock(Table.class);
    when(table.countCustomers()).thenReturn(ONE_CUSTOMER);
    for(int i = 0; i < nbCustomer; i++){
      tables.add(table);
    }
    return tables;
  }

}