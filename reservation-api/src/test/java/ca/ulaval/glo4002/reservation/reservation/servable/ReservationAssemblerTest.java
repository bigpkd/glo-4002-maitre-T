package ca.ulaval.glo4002.reservation.reservation.servable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4002.reservation.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.reservation.rest.response.ReservationResponse;
import ca.ulaval.glo4002.reservation.utils.Money;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationAssemblerTest {
  private static String A_VENDOR_CODE = "TEAM";
  private static OffsetDateTime A_DINNER_DATE =
      OffsetDateTime.of(2150, 7, 21, 0, 0, 0, 0, ZoneOffset.UTC);
  private static OffsetDateTime A_RESERVATION_DATE =
      OffsetDateTime.of(2150, 4, 21, 15, 23, 20, 0, ZoneOffset.UTC);
  private static Money AN_AMOUNT = new Money(10);

  private ReservationAssembler reservationAssembler;
  private Reservation reservation;
  private List<Customer> customers;

  @BeforeEach
  void setupReservationAssemblerTest() {
    Country aCountry = mock(Country.class);
    Table aTable = mock(Table.class);
    Customer customer = mock(Customer.class);
    customers = Arrays.asList(customer);
    List<Table> tables = Arrays.asList(aTable);
    when(aTable.getTablePrice()).thenReturn(AN_AMOUNT);
    when(aTable.getCustomers()).thenReturn(customers);
    reservationAssembler = new ReservationAssembler();
    reservation = new Reservation(A_VENDOR_CODE, A_DINNER_DATE, A_RESERVATION_DATE, aCountry, tables);
  }

  @Test
  void givenAReservation_whenToReservationResponse_thenShouldObtainCorrespondingReservationResponse() {
    ReservationResponse reservationResponse = reservationAssembler.toReservationResponse(reservation);

    assertEquals(reservationResponse.dinnerDate, A_DINNER_DATE);
    assertEquals(reservationResponse.reservationPrice, AN_AMOUNT.setRounding(2, RoundingMode.HALF_UP));
  }

  @Test
  void givenAReservation_whenReservationCreated_thenShouldObtainReservationNumber() {
    String expectedNumber = reservationAssembler.toReservationStringNumber(reservation);

    assertEquals(expectedNumber, reservation.getNumber().toString());
  }
}
